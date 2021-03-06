package edu.unc.cs.graderServer.gradingProgram;

import edu.unc.cs.graderServer.utils.TimestampUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Andrew Vitkus
 *
 */
public class GraderSetup implements IGraderSetup {

    private final String assignmentName;
    private final String courseName;
    private final String first;
    private final String last;
    private final String onyen;
    private final Path root;

    public GraderSetup(String onyen, Path root, String courseName, String assignmentName, String first, String last) {
        this.onyen = onyen;
        this.first = first;
        this.last = last;
        this.root = root;
        this.courseName = courseName.replace(" ", "");
        this.assignmentName = assignmentName;
    }

    @Override
    public String[] getCommandArgs() {
        IGraderConfigWriter cw = buildConfigWriter();
        return cw.getCommandArgs();
    }

    @Override
    public Path setupFiles() throws IOException {
        if (!Files.exists(Paths.get("graderProgram", ".gradersettings"))) {
            Files.createFile(Paths.get("graderProgram", ".gradersettings"));
        }
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
        if (!Files.exists(root.resolve("grades.csv"))) {
            Files.createFile(root.resolve("grades.csv"));
        }
        
        Path onyenFolder = root.resolve(last + ", " + first + " (" + onyen + ")");
        
        Files.write(onyenFolder.resolve("timestamp.txt"), 
                TimestampUtil.getCurrentSakaiTimestamp().getBytes(StandardCharsets.UTF_8), 
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        
        Files.createDirectories(onyenFolder.resolve("Submission attachment(s)"));
        Files.createDirectories(onyenFolder.resolve("Feedback Attachment(s)"));
        Files.createDirectories(Paths.get("graderProgram", "log", "AssignmentData", assignmentName));
        return onyenFolder.resolve(Paths.get("Submission attachment(s)", assignmentName));
    }

    @Override
    public void writeConfig() throws FileNotFoundException, IOException {
        IGraderConfigWriter cw = buildConfigWriter();
        cw.write(Paths.get("graderProgram", "config", "config.properties").toFile());
    }

    private IGraderConfigWriter buildConfigWriter() {
        IGraderConfigWriter cw = new GraderConfigWriter();
        cw.setAssignmentName(assignmentName);
        cw.setProjectRequirements("gradingTools." + Character.toLowerCase(assignmentName.charAt(0)) + assignmentName.substring(1) + "." + Character.toUpperCase(assignmentName.charAt(0)) + assignmentName.substring(1) + "ProjectRequirements");
        cw.setController(IGraderConfigWriter.HEADLESS_GRADING_MANAGER);
        cw.setPath(root.subpath(1, root.getNameCount()).toString());
        cw.setStartOnyen(onyen);
        cw.setEndOnyen(onyen);
        cw.setCourseName(courseName);
        cw.setLogging(IGraderConfigWriter.FEEDBACK_JSON, IGraderConfigWriter.FEEDBACK_TXT, IGraderConfigWriter.LOCAL_JSON, IGraderConfigWriter.LOCAL_TXT);
        return cw;
    }
}
