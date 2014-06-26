package server.com.graderHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.com.graderHandler.sql.DatabaseReader;
import server.com.graderHandler.sql.DatabaseWriter;
import server.com.graderHandler.sql.IDatabaseReader;
import server.com.graderHandler.sql.IDatabaseWriter;
import server.com.graderHandler.util.CSVGradeWriter;
import server.com.graderHandler.util.FileTreeManager;
import server.com.graderHandler.util.GradingData;
import server.com.graderHandler.util.GradingFailureException;
import server.com.graderHandler.util.IGradeWriter;
import server.com.graderHandler.util.IGradingData;
import server.com.graderHandler.util.IJSONReader;
import server.com.graderHandler.util.JSONReader;
import server.com.gradingProgram.GraderPool;
import server.com.gradingProgram.GraderSetup;
import server.com.gradingProgram.IGraderSetup;
import server.com.webHandler.pages.helpers.GradePageManager;
import server.htmlBuilder.IHTMLFile;
import server.utils.ConfigReader;
import server.utils.IConfigReader;

public class InputBasedGraderHandler {

    private static final Logger LOG = Logger.getLogger(InputBasedGraderHandler.class.getName());

    private Path assignmentRoot;
    private String course;
    private String first;
    private String firstName;
    private String last;
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

    public void setFirst(String first) {
        this.first = first;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOnyen(String onyen) {
        this.onyen = onyen;
    }

    public void setOyen(String onyen) {
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
                try {
                    grade();
                } catch (InterruptedException e1) {
                    LOG.log(Level.FINER, null, e1);
                }
                try {
                    try {
                        // write to grades.csv of old zip if allowed
                        if (underSubmitLimit()) {
                            saveToGradesFile();
                        }
                    } catch (FileNotFoundException | InterruptedException | ExecutionException ex) {
                        LOG.log(Level.FINER, null, ex);
                    }
                    postToDatabase();
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
        File jsonFile = userPath.resolve(Paths.get("Feedback Attachment(s)", "results.json")).toFile();
        try {
            IResponseWriter responseWriter = new JSONBasedResponseWriter(jsonFile, true);
            responseWriter.setAssignmentName(title);
            return responseWriter.getResponse();
        } catch (FileNotFoundException e) {
            LOG.log(Level.WARNING, "Unable to access JSON file at: {0}", jsonFile.getAbsolutePath());
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

    private void postToDatabase() throws SQLException, FileNotFoundException, IOException {
        IConfigReader config = new ConfigReader("./config/config.properties");
        String username = config.getString("database.username").orElseThrow(IllegalArgumentException::new);
        String password = config.getString("database.password").orElseThrow(IllegalArgumentException::new);
        String url = config.getString("database.url").orElseThrow(IllegalArgumentException::new);
        if (config.getBoolean("database.ssl", false).get()) {
            url += "?verifyServerCertificate=true&useSSL=true&requireSSL=true";
        }

        try (IDatabaseWriter dw = new DatabaseWriter(username, password, "jdbc:" + url);
                IDatabaseReader dr = new DatabaseReader(username, password, "jdbc:" + url)) {

            //dw.writeUser(onyen, uid, pid, first, last);
            String num = title.substring(title.lastIndexOf(' ') + 1, title.length() - 1);
            String type = title.substring(title.lastIndexOf('(') + 1, title.lastIndexOf(' '));
            int courseID = dr.readCurrentCourseID(course, section);
            String assignmentName = dr.readAssignmentCatalogName(num, type, Integer.toString(courseID));
            int assignmentCatalogID = dr.readAssignmentCatalogID(num, assignmentName, type, Integer.toString(courseID));
            dw.writeAssignment(assignmentCatalogID, Integer.parseInt(uid));

            int assignmentSubmissionID = dr.readLatestAssignmentSubmissionID(Integer.parseInt(uid), assignmentCatalogID);
            dw.writeResult(assignmentSubmissionID);

            File jsonFile = userPath.resolve(Paths.get("Feedback Attachment(s)", "results.json")).toFile();
            if (!jsonFile.exists()) {
                LOG.log(Level.WARNING, "Can't read json outpt from file: {0}", jsonFile.getAbsolutePath());
            } else {
                IJSONReader reader = new JSONReader(jsonFile);
                int resultID = dr.readLatestResultID(assignmentSubmissionID);
                dw.writeGradingParts(reader.getGrading(), reader.getExtraCredit(), resultID);

                List<List<String>> testResults = reader.getGradingTests();

                for (List<String> test : testResults) {
                    String gradingPartName = test.get(0);
                    int gradingPartID = dr.readLatestGradingPartID(gradingPartName, resultID);
                    dw.writeGradingTest(test.get(1), test.get(2), test.get(3), Integer.toString(gradingPartID));
                    int gradingTestID = dr.readLatestGradingTestID(gradingPartID);
                    String[] notes = test.get(4).split(";");
                    if (notes.length > 0 && !notes[0].isEmpty()) {
                        dw.writeTestNotes(notes, gradingTestID);
                    }
                }

                dw.writeComments(reader.getComments(), resultID);
            }
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

    private void saveToGradesFile() throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
        File json = userPath.resolve(Paths.get("Feedback Attachment(s)", "results.json")).toFile();
        int points = 0;
        int possible = 0;
        if (json.exists()) {
            IJSONReader reader = new JSONReader(json);
            String[][] grading = reader.getGrading();
            for (String[] grade : grading) {
                points += Integer.parseInt(grade[2]);
                possible += Integer.parseInt(grade[3]);
            }
        }
        IGradingData gradingData = new GradingData(onyen, first, last, points, possible);
        int parenStart = title.indexOf('(');
        int parenEnd = title.indexOf(')');
        String assignmentName = title.substring(parenStart + 1, parenEnd);

        IGradeWriter gradeWriter = new CSVGradeWriter(assignmentName, assignmentRoot.resolve("grades.csv"));
        gradeWriter.write(gradingData);
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
