package server.com.gradingProgram;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Andrew Vitkus
 *
 */
public class GraderSetup implements IGraderSetup {

    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private final String onyen;
    private final String assignmentName;
    private final Path root;

    public GraderSetup(String onyen, Path root, String assignmentName) {
        this.onyen = onyen;
        this.root = root;
        this.assignmentName = assignmentName;
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
        Path onyenFolder = root.resolve("(" + onyen + ")");
        Files.createDirectories(onyenFolder.resolve("Submission attachment(s)"));
        return onyenFolder.resolve(Paths.get("Submission attachment(s)", assignmentName));
    }

    @Override
    public void writeConfig() throws FileNotFoundException, IOException {
        IGraderConfigWriter cw = buildConfigWriter();
        cw.write(Paths.get("graderProgram", "config", "config.properties").toFile());
    }

    @Override
    public String[] getCommandArgs() {
        IGraderConfigWriter cw = buildConfigWriter();
        return cw.getCommandArgs();
    }

    private IGraderConfigWriter buildConfigWriter() {
        IGraderConfigWriter cw = new GraderConfigWriter();
        cw.setAssignmentName(assignmentName);
        cw.setProjectRequirements("gradingTools." + Character.toLowerCase(assignmentName.charAt(0)) + assignmentName.substring(1) + "." + Character.toUpperCase(assignmentName.charAt(0)) + assignmentName.substring(1) + "ProjectRequirements");
        cw.setController(IGraderConfigWriter.HEADLESS_GRADING_MANAGER);
        cw.setPath(root.subpath(1, root.getNameCount()).toString());
        System.out.println("path = " + root.subpath(1, root.getNameCount()).toString() + ".zip");
        cw.setStartOnyen(onyen);
        cw.setEndOnyen(onyen);
        cw.setLogging(IGraderConfigWriter.FEEDBACK_JSON, IGraderConfigWriter.FEEDBACK_TXT, IGraderConfigWriter.LOCAL_JSON, IGraderConfigWriter.LOCAL_TXT);
        return cw;
    }
}
