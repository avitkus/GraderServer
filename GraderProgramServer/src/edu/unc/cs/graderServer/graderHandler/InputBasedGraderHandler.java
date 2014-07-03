package edu.unc.cs.graderServer.graderHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.unc.cs.graderServer.graderHandler.sql.DatabaseReader;
import edu.unc.cs.graderServer.graderHandler.sql.DatabaseWriter;
import edu.unc.cs.graderServer.graderHandler.sql.IDatabaseWriter;
import edu.unc.cs.graderServer.graderHandler.util.FileTreeManager;
import edu.unc.cs.graderServer.graderHandler.util.GradingFailureException;
import edu.unc.cs.graderServer.graderHandler.util.IGradePublisher;
import edu.unc.cs.graderServer.graderHandler.util.JSONGradePublisher;
import edu.unc.cs.graderServer.gradingProgram.GraderPool;
import edu.unc.cs.graderServer.gradingProgram.GraderSetup;
import edu.unc.cs.graderServer.gradingProgram.IGraderSetup;
import edu.unc.cs.graderServer.webHandler.pages.helpers.GradePageManager;
import edu.unc.cs.htmlBuilder.IHTMLFile;
import edu.unc.cs.graderServer.utils.ConfigReader;
import edu.unc.cs.graderServer.utils.IConfigReader;

public class InputBasedGraderHandler {

    private static final Logger LOG = Logger.getLogger(InputBasedGraderHandler.class.getName());

    private Path assignmentRoot;
    private String course;
    private String firstName;
    private Path jsonPath;
    private String lastName;
    private String onyen;
    private String pageUUID;
    private String pid;
    private String section;
    private Path submission;
    private Path submissionPath;
    private String title;
    private String uid;
    private Path userPath;

    public InputBasedGraderHandler() {
        assignmentRoot = Paths.get("graderProgram", "data");
    }

    public void setAssignment(String name) {
        this.title = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOnyen(String onyen) {
        this.onyen = onyen;
    }

    public void setPID(String pid) {
        this.pid = pid;
    }

    public void setPageUUID(String uuid) {
        this.pageUUID = uuid;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setSubmission(Path submission) {
        this.submission = submission;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public IHTMLFile process() throws GradingFailureException {
        try {
            FileTreeManager.checkPurgeRoot();
            boolean success = readSubmission();
            if (success) {
                if (onyen != null && uid != null && pid != null && firstName != null && lastName != null) {
                    addUser();
                }
                String submissionType = Files.probeContentType(submission);
                if (!submissionType.equals("application/json")) {
                    try {
                        grade();jsonPath = userPath.resolve(Paths.get("Feedback Attachment(s)", "results.json"));
                    } catch (InterruptedException e1) {
                        LOG.log(Level.FINER, null, e1);
                    }
                } else {
                    jsonPath = submission;
                }
                try {
                    IGradePublisher gradeWriter = new JSONGradePublisher();
                    gradeWriter.setAssignment(title).setCourse(course)
                            .setSection(section).setFirstName(firstName)
                            .setLastName(lastName).setPID(pid)
                            .setUID(uid).setOnyen(onyen)
                            .setGradesFile(assignmentRoot.resolve("grades.csv"))
                            .setResultsFile(jsonPath);
                    try {
                        // write to grades.csv of old zip if allowed
                        if (underSubmitLimit()) {
                            gradeWriter.saveToGradesFile();
                        }
                    } catch (FileNotFoundException | InterruptedException | ExecutionException ex) {
                        LOG.log(Level.FINER, null, ex);
                    }
                    gradeWriter.postToDatabase();
                } catch (SQLException e) {
                    LOG.log(Level.FINER, null, e);
                }
                IHTMLFile gradingResult = createResponse(title);
                GradePageManager.refresh(pageUUID);
                GradePageManager.update(pageUUID, gradingResult);
                return createResponse(title);
            } else {
                throw new GradingFailureException();
            }
        } catch (MalformedURLException e1) {
            LOG.log(Level.FINER, null, e1);
            GradingFailureException e = new GradingFailureException();
            e.setStackTrace(e1.getStackTrace());
            throw e;
        } catch (IOException e1) {
            LOG.log(Level.FINER, null, e1);
            GradingFailureException e = new GradingFailureException();
            e.setStackTrace(e1.getStackTrace());
            throw e;
        } catch (SQLException ex) {
            Logger.getLogger(InputBasedGraderHandler.class.getName()).log(Level.SEVERE, null, ex);
            GradingFailureException e = new GradingFailureException();
            e.setStackTrace(ex.getStackTrace());
            throw e;
        }
    }

    public void setName(String first, String last) {
        firstName = first;
        lastName = last;
    }

    private void addUser() throws IOException, SQLException {
        IConfigReader config = new ConfigReader("./config/config.properties");
        String username = config.getString("database.username").orElseThrow(IllegalArgumentException::new);
        String password = config.getString("database.password").orElseThrow(IllegalArgumentException::new);
        String url = config.getString("database.url").orElseThrow(IllegalArgumentException::new);
        if (config.getBoolean("database.ssl", false).get()) {
            url += "?verifyServerCertificate=true&useSSL=true&requireSSL=true";
        }

        try (IDatabaseWriter dw = new DatabaseWriter(username, password, "jdbc:" + url)) {
            dw.writeUser(onyen, uid, pid, firstName, lastName);
        }
    }

    private IHTMLFile createResponse(String title) throws FileNotFoundException, IOException {
        try {
            IResponseWriter responseWriter = new JSONBasedResponseWriter(jsonPath.toFile(), true);
            responseWriter.setAssignmentName(title);
            return responseWriter.getResponse();
        } catch (FileNotFoundException e) {
            LOG.log(Level.WARNING, "Unable to access JSON file at: {0}", jsonPath);
            throw e;
        }
    }

    private void grade() throws IOException, InterruptedException {
        int parenStart = title.indexOf('(');
        int parenEnd = title.indexOf(')');
        String assignmentName = title.substring(parenStart + 1, parenEnd);
        assignmentName = assignmentName.replace(" ", "");
        IGraderSetup setup = new GraderSetup(onyen, assignmentRoot, assignmentName);
        try {
            //LOG.log(Level.INFO, "Args:\n{0}", setup.getCommandArgs());
            Future<String> grader = GraderPool.runGrader(setup.getCommandArgs());

            // log grader program output
            LOG.log(Level.INFO, "Grader_Program: {0}", grader.get());
        } catch (ExecutionException e) {
            LOG.log(Level.FINER, null, e);
        }
    }

    private boolean readSubmission() {
        try {
            int parenStart = title.indexOf('(');
            int parenEnd = title.indexOf(')');
            String assignmentName = title.substring(parenStart + 1, parenEnd);
            assignmentName = assignmentName.replace(" ", "");
            int year = Calendar.getInstance().get(Calendar.YEAR);

            assignmentRoot = assignmentRoot.resolve(Paths.get(Integer.toString(year), course.replaceAll(" ", ""), section, assignmentName));
            userPath = assignmentRoot.resolve("(" + onyen + ")");
            String[] fileSplit = submission.getFileName().toString().split("\\.", 2);
            submissionPath = userPath.resolve(Paths.get("Submission attachment(s)")).resolve(title + (fileSplit.length > 1 ? "." + fileSplit[1] : ""));

            try {
                if (submissionPath.toFile().exists() && underSubmitLimit()) { // write backup of old zip if allowed
                    Path backupPath = userPath.resolve(Paths.get("Submission attachment(s)", submissionPath.getFileName() + ".bak"));
                    FileTreeManager.backup(submissionPath, backupPath);
                }
            } catch (FileNotFoundException e) {
                LOG.log(Level.FINER, null, e);
                e.printStackTrace();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
                LOG.log(Level.FINER, null, e);
            }

            FileTreeManager.purgeSubmission(assignmentRoot);

            Files.createDirectories(submissionPath);

            FileTreeManager.backup(submission, submissionPath);
            return true;
        } catch (IOException e) {
            LOG.log(Level.FINER, null, e);
            e.printStackTrace();
            return false;
        }
    }

    private boolean underSubmitLimit() throws IOException, SQLException {
        int parenStart = title.indexOf('(');
        int parenEnd = title.indexOf(')');
        String assignmentName = title.substring(parenStart + 1, parenEnd);

        IConfigReader config = new ConfigReader("./config/config.properties");
        String username = config.getString("database.username").orElseThrow(IllegalArgumentException::new);
        String password = config.getString("database.password").orElseThrow(IllegalArgumentException::new);
        String url = config.getString("database.url").orElseThrow(IllegalArgumentException::new);
        if (config.getBoolean("database.ssl", false).get()) {
            url += "?verifyServerCertificate=true&useSSL=true&requireSSL=true";
        }
        try (DatabaseReader dr = new DatabaseReader(username, password, "jdbc:" + url)) {
            int num = Integer.parseInt(assignmentName.substring(assignmentName.lastIndexOf(' ') + 1)); // assignment number
            String type = assignmentName.substring(0, assignmentName.lastIndexOf(' ')); // assignment type
            int courseID = dr.readCurrentCourseID(course, section); // sql id of course
            String name = dr.readAssignmentCatalogName(num, type, courseID); // sql name of assignment
            int assignmentCatalogID = dr.readAssignmentCatalogID(num, name, type, courseID); // sql id from assignment catalog
            int assignmentSubmissionID = dr.readLatestAssignmentSubmissionID(Integer.parseInt(uid), assignmentCatalogID); // sql id of assignment submissions

            int subCount = dr.readCountForSubmission(assignmentSubmissionID); // number of submissions
            int subLimit = dr.readSubmissionLimitForAssignment(assignmentCatalogID); // max limit of saved submissions
            return subLimit == 0 || subCount < subLimit;
        }
    }
}
