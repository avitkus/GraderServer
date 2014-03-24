package server.com.graderHandler;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;
import server.com.graderHandler.sql.DatabaseReader;
import server.com.graderHandler.sql.DatabaseWriter;
import server.com.graderHandler.sql.IDatabaseReader;
import server.com.graderHandler.sql.IDatabaseWriter;
import server.com.graderHandler.util.FileTreeManager;
import server.com.gradingProgram.GraderPool;
import server.com.gradingProgram.GraderSetup;
import server.com.gradingProgram.IGraderSetup;
import server.com.gradingProgram.IJSONReader;
import server.com.gradingProgram.JSONReader;
import server.utils.ConfigReader;
import server.utils.IConfigReader;
import server.utils.ZipReader;

/**
 * @author Andrew Vitkus
 *
 */
public class GraderHandler extends Thread {

    private final int BUFFER_SIZE = 4096;

    private final SSLSocket clientSocket;
    private String title;
    private String course;
    private String onyen;
    private String uid;
    private String first;
    private String last;
    private String pid;
    private File zip;
    private Path assignmentRoot;
    private Path submissionPath;

    public GraderHandler(SSLSocket clientSocket) {
        this.clientSocket = clientSocket;
        assignmentRoot = Paths.get("graderProgram", "data");
    }

    @Override
    public void run() {
        try {
            if (isAuthenticated(readVfykey())) {
                FileTreeManager.checkPurgeRoot();
                boolean success = readSubmission();
                replyBoolean(success);
                if (success) {
                    try {
                        grade();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    sendResponse(title);
                    try {
                        postToDatabase();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return;
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readVfykey() throws IOException {
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        return dis.readUTF();
    }

    private boolean readSubmission() {
        try {
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

            course = dis.readUTF();

            String assignmentName = dis.readUTF();
            title = assignmentName;

            int parenStart = title.indexOf('(');
            int parenEnd = title.indexOf(')');
            assignmentName = title.substring(parenStart + 1, parenEnd);
            String assignmentNameRaw = assignmentName;
            assignmentName = assignmentName.replace(" ", "");

            String[] courseParts = course.split("-");

            int year = Calendar.getInstance().get(Calendar.YEAR);

            assignmentRoot = assignmentRoot.resolve(Paths.get(Integer.toString(year), courseParts[0].replace(" ", ""), courseParts[1], assignmentName));
            submissionPath = assignmentRoot.resolve("(" + onyen + ")");
            Path zipPath = submissionPath.resolve(Paths.get("Submission attachment(s)", assignmentName + ".zip"));

            IConfigReader config = new ConfigReader("./config/config.properties");
            String username = config.getString("database.username");
            String password = config.getString("database.password");
            String url = config.getString("database.url");
            if (config.getBoolean("database.ssl")) {
                url += "?verifyServerCertificate=true&useSSL=true&requireSSL=true";
            }
            try (DatabaseReader dr = new DatabaseReader(username, password, "jdbc:" + url)) {
                int num = Integer.parseInt(assignmentNameRaw.substring(assignmentNameRaw.lastIndexOf(' ') + 1)); // assignment number
                String type = assignmentNameRaw.substring(0, assignmentNameRaw.lastIndexOf(' ')); // assignment type
                int courseID = dr.readCurrentCourseID(courseParts[0], courseParts[1]); // sql id of course
                String name = dr.readAssignmentCatalogName(num, type, courseID); // sql name of assignment
                int assignmentCatalogID = dr.readAssignmentCatalogID(num, name, type, courseID); // sql id from assignment catalog
                int assignmentSubmissionID = dr.readLatestAssignmentSubmissionID(Integer.parseInt(uid), assignmentCatalogID); // sql id of assignment submissions

                int subCount = dr.readCountForSubmission(assignmentSubmissionID); // number of submissions
                int subLimit = dr.readSubmissionLimitForAssignment(assignmentCatalogID); // max limit of saved submissions
                if (zipPath.toFile().exists() && (subCount < subLimit || subLimit == 0)) { // write backup of old zip if allowed
                    Path zipBackupPath = submissionPath.resolve(Paths.get("Submission attachment(s)", assignmentName + ".zip.bak"));
                    FileTreeManager.backup(zipPath, zipBackupPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            FileTreeManager.purgeSubmission(submissionPath);

            Files.createDirectories(submissionPath.resolve("Submission attachment(s)"));
            zip = zipPath.toFile();

            zip.createNewFile();

            long fileSize = dis.readLong();

            if (!zip.canWrite()) {
                return false;
            }
            try (FileOutputStream fos = new FileOutputStream(zip)) {
                byte[] data = new byte[BUFFER_SIZE];
                int bytesRead = -1;
                while (fileSize > 0 && (bytesRead = dis.read(data)) != -1) {
                    fos.write(data, 0, bytesRead);
                    fileSize -= bytesRead;
                }

                fos.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendResponse(String title) throws FileNotFoundException, IOException {
        IResponseWriter responseWriter = new JSONBasedResponseWriter(submissionPath.resolve(Paths.get("Feedback Attachment(s)", "results.json")).toFile());
        responseWriter.setAssignmentName(title);
        replyUTF(responseWriter.getResponse());
    }

    private void replyUTF(String response) {
        try {
            //System.out.println(response);
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            dos.writeUTF(response);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void replyBoolean(boolean response) {
        try {
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            dos.writeBoolean(response);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isAuthenticated(String vfykey) throws MalformedURLException, IOException {
        URL authURL = new URL("https", "onyen.unc.edu", 443, "/cgi-bin/unc_id/authenticator.pl/" + vfykey);
        HttpsURLConnection auth = (HttpsURLConnection) authURL.openConnection();

        auth.setDoOutput(true);
        auth.setDoInput(true);
        auth.getOutputStream().write(42);

        /*
         * build a map of the auth server's key-value pair response
         */
        String response = getResponse(auth);
        HashMap<String, String> responseMap = new HashMap<>(7);
        Arrays.stream(response.split("\n")).forEach((String responseLine) -> {
            String[] lineParts = responseLine.split(": ");
            responseMap.put(lineParts[0], lineParts[1]);
        });

        int colon1Loc = response.indexOf(':');
        int endLnLoc = response.indexOf('\n');
        String authStatus;
        if (endLnLoc > 0) {
            authStatus = response.substring(colon1Loc + 2, endLnLoc);
        } else {
            authStatus = response.substring(colon1Loc + 2);
        }
        //System.out.println(authStatus);
        if (authStatus.equals("pass")) {
            onyen = response.substring(0, colon1Loc);
            uid = responseMap.get("uid");
            pid = responseMap.get("pid");
            first = responseMap.get("givenName");
            last = responseMap.get("uncPreferredSurname");
            return true;
        } else {
            return false;
        }
    }

    private String getResponse(HttpsURLConnection connection) throws IOException {
        byte[] bytes = new byte[512];
        try (BufferedInputStream bis = new BufferedInputStream(connection.getInputStream())) {
            StringBuilder response = new StringBuilder();
            int in;
            while ((in = bis.read(bytes)) != -1) {
                response.append(new String(bytes, 0, in));
            }
            return response.toString();
        }
    }

    private void postToDatabase() throws SQLException, FileNotFoundException, IOException {
        IConfigReader config = new ConfigReader("./config/config.properties");
        String username = config.getString("database.username");
        String password = config.getString("database.password");
        String url = config.getString("database.url");
        if (config.getBoolean("database.ssl")) {
            url += "?verifyServerCertificate=true&useSSL=true&requireSSL=true";
        }

        try (IDatabaseWriter dw = new DatabaseWriter(username, password, "jdbc:" + url);
                IDatabaseReader dr = new DatabaseReader(username, password, "jdbc:" + url)) {

            dw.writeUser(onyen, uid, pid, first, last);

            String num = title.substring(title.lastIndexOf(' ') + 1, title.length() - 1);
            String type = title.substring(title.lastIndexOf('(') + 1, title.lastIndexOf(' '));
            String[] courseParts = course.split("-");
            int courseID = dr.readCurrentCourseID(courseParts[0], courseParts[1]);
            String assignmentName = dr.readAssignmentCatalogName(num, type, Integer.toString(courseID));
            int assignmentCatalogID = dr.readAssignmentCatalogID(num, assignmentName, type, Integer.toString(courseID));
            dw.writeAssignment(assignmentCatalogID, Integer.parseInt(uid));

            int assignmentSubmissionID = dr.readLatestAssignmentSubmissionID(Integer.parseInt(uid), assignmentCatalogID);
            dw.writeResult(assignmentSubmissionID);

            File jsonFile = submissionPath.resolve(Paths.get("Feedback Attachment(s)", "results.json")).toFile();
            if (!jsonFile.exists()) {
                System.err.println("Unable to read json grading output. Not attempting to post to database.");
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

    private void grade() throws IOException, InterruptedException {
        int parenStart = title.indexOf('(');
        int parenEnd = title.indexOf(')');
        String assignmentName = title.substring(parenStart + 1, parenEnd);
        assignmentName = assignmentName.replace(" ", "");
        IGraderSetup setup = new GraderSetup(onyen, assignmentRoot, assignmentName);
        Path submission = setup.setupFiles();
        ZipReader.read(zip, submission.toFile());
        //setup.writeConfig();
        try {
            Future<String> grader = GraderPool.runGrader(setup.getCommandArgs());

            // print grader program output with *** before each line
            Arrays.stream(grader.get().split("\n")).forEach((line) -> System.out.println("*** " + line));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
