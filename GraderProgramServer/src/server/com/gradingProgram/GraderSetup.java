package server.com.gradingProgram;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Andrew Vitkus
 *
 */
public class GraderSetup implements IGraderSetup {
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	private String onyen;
	private String assignmentName;
	
	public GraderSetup(String onyen, String assignmentName) {
		this.onyen = onyen;
		this.assignmentName = assignmentName;
	}
	
	@Override
	public Path setupFiles() throws IOException {
		if (!Files.exists(Paths.get("graderProgram" + FILE_SEPARATOR + ".gradersettings"))) {
			Files.createFile(Paths.get("graderProgram" + FILE_SEPARATOR + ".gradersettings"));
		}
		if (!Files.exists(Paths.get("graderProgram" + FILE_SEPARATOR + "data"))) {
			Files.createDirectories(Paths.get("graderProgram" + FILE_SEPARATOR + "data"));
		}
		if (!Files.exists(Paths.get("graderProgram" + FILE_SEPARATOR + "data" + FILE_SEPARATOR + "grades.csv"))) {
			Files.createFile(Paths.get("graderProgram" + FILE_SEPARATOR + "data" + FILE_SEPARATOR + "grades.csv"));
		}
		Path onyenFolder = Paths.get("graderProgram" + FILE_SEPARATOR + "data" + FILE_SEPARATOR + "(" + onyen + ")");
		if (Files.exists(onyenFolder)) {
			Files.walkFileTree(onyenFolder, new SimpleFileVisitor<Path>() {
		         @Override
		         public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
		             throws IOException
		         {
		             Files.delete(file);
		             return FileVisitResult.CONTINUE;
		         }
		         @Override
		         public FileVisitResult postVisitDirectory(Path dir, IOException e)
		             throws IOException
		         {
		             if (e == null) {
		                 Files.delete(dir);
		                 return FileVisitResult.CONTINUE;
		             } else {
		                 // directory iteration failed
		                 throw e;
		             }
		         }
		     });
		}
		Files.createDirectory(Paths.get("graderProgram" + FILE_SEPARATOR + "data" + FILE_SEPARATOR + "(" + onyen + ")"));
		if (!Files.exists(Paths.get("graderProgram" + FILE_SEPARATOR + "data" + FILE_SEPARATOR + "(" + onyen + ")" + FILE_SEPARATOR + "Submission attachment(s)"))) {
			Files.createDirectory(Paths.get("graderProgram" + FILE_SEPARATOR + "data" + FILE_SEPARATOR + "(" + onyen + ")" + FILE_SEPARATOR + "Submission attachment(s)"));
		}
		return Paths.get("graderProgram" + FILE_SEPARATOR + "data" + FILE_SEPARATOR + "(" + onyen + ")" + FILE_SEPARATOR + "Submission attachment(s)" + FILE_SEPARATOR + "" + assignmentName);
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
		cw.setPath(Paths.get("data").toString());
		cw.setStartOnyen(onyen);
		cw.setEndOnyen(onyen);
		cw.setLogging(IGraderConfigWriter.FEEDBACK_JSON, IGraderConfigWriter.FEEDBACK_TXT, IGraderConfigWriter.LOCAL_JSON, IGraderConfigWriter.LOCAL_TXT);
		return cw;
	}
}
