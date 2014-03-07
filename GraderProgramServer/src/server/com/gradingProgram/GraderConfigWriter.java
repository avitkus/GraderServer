package server.com.gradingProgram;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author Andrew Vitkus
 *
 */
public class GraderConfigWriter implements IGraderConfigWriter {
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");

	private String projectRequirements;
	private String assignmentName;
	private String controller;
	private String path;
	private String startOnyen;
	private String endOnyen;
	private int logging;
	private String spreadsheetPath;
	private boolean frameworkGUI;
	
	public GraderConfigWriter() {
		projectRequirements = "";
		assignmentName = "";
		controller = "";
		path = "";
		startOnyen = "";
		endOnyen = "";
		logging = 0;
		spreadsheetPath = "";
		frameworkGUI = false;
	}
	
	@Override
	public void setProjectRequirements(String requirements) {
		projectRequirements = requirements;
	}

	@Override
	public void setAssignmentName(String name) {
		assignmentName = name;
	}

	@Override
	public void setController(String name) {
		controller = name;
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public void setStartOnyen(String start) {
		startOnyen = start;
	}
	
	@Override
	public void setEndOnyen(String end) {
		endOnyen = end;
	}
	
	@Override
	public void setLogging(int... logging) {
		this.logging = 0;
		for(int type : logging) {
			this.logging += type;
		}
	}
	
	@Override
	public void setSpreadsheet(String path) {
		spreadsheetPath = path;
	}
	
	@Override
	public void setFrameworkGUI(boolean frameworkGUI) {
		this.frameworkGUI = frameworkGUI;
	}
	
	@Override
	public String getProjectRequirements() {
		return projectRequirements;
	}
	
	@Override
	public String getAssignmentName() {
		return assignmentName;
	}
	
	@Override
	public String getController() {
		return controller;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public String getStartOnyen() {
		return startOnyen;
	}
	
	@Override
	public String getEndOnyen() {
		return endOnyen;
	}

	@Override
	public int getLogging() {
		return logging;
	}
	
	@Override
	public String getSpreadsheet() {
		return spreadsheetPath;
	}

	@Override
	public boolean getFrameworkGUI() {
		return frameworkGUI;
	}

	@Override
	public String getConfigText() {
		StringBuilder config = new StringBuilder();

		config.append("project.requirements = ").append(projectRequirements).append("\n");
		config.append("project.name = ").append(assignmentName).append("\n");
		config.append("grader.controller = ").append(controller).append("\n");
		if(path != "") {
			config.append("grader.headless.path = ").append(path).append("\n");
		}
		if(startOnyen != "") {
			config.append("grader.headless.start = ").append(startOnyen).append("\n");
		}
		if(endOnyen != "") {
			config.append("grader.headless.end = ").append(endOnyen).append("\n");
		}
		if(logging != 0) {
			config.append("grader.logger = ").append(getLoggingStr()).append("\n");
		}
		if(spreadsheetPath != "") {
			config.append("grader.logger.spreadsheetFilename = ").append(spreadsheetPath).append("\n");
		}
		config.append("grader.controller.useFrameworkGUI = ").append(frameworkGUI);
		
		return config.toString();
	}

	@Override
	public void write(File file) throws FileNotFoundException, IOException {
		if (file.exists()) {
			file.delete();
		}
		int folderEnd = file.toPath().toString().lastIndexOf(FILE_SEPARATOR);
		Files.createDirectories(Paths.get(file.toPath().toString().substring(0, folderEnd)));
		Files.createFile(file.toPath());
		try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
			bos.write(getConfigText().getBytes());
			bos.flush();
		}
	}

	private String getLoggingStr() {
		StringBuilder str = new StringBuilder();
		
		if ((logging | IGraderConfigWriter.FEEDBACK_TXT) == logging) {
			str.append("feedback-txt");
		}
		if ((logging | IGraderConfigWriter.FEEDBACK_JSON) == logging) {
			if (str.length() != 0) {
				str.append("+");
			}
			str.append("feedback-json");
		}
		if ((logging | IGraderConfigWriter.LOCAL_TXT) == logging) {
			if (str.length() != 0) {
				str.append("+");
			}
			str.append("local-txt");
		}
		if ((logging | IGraderConfigWriter.LOCAL_JSON) == logging) {
			if (str.length() != 0) {
				str.append("+");
			}
			str.append("local-json");
		}
		if ((logging | IGraderConfigWriter.SPREADSHEET) == logging) {
			if (str.length() != 0) {
				str.append("+");
			}
			str.append("spreadsheet");
		}
		
		return str.toString();
	}

	@Override
	public String[] getCommandArgs() {
		ArrayList<String> args = new ArrayList<>();
		args.add("--project-requirements");
		args.add(projectRequirements);
		args.add("--project-name ");
		args.add(assignmentName);
		args.add("--grader-controller");
		args.add(controller);
		if(path != "") {
			args.add("--headless-path");
			args.add(path);
		}
		if(startOnyen != "") {
			args.add("--headless-start");
			args.add(startOnyen);
		}
		if(endOnyen != "") {
			args.add("--headless-end");
			args.add(endOnyen);
		}
		if(logging != 0) {
			args.add("--logger");
			args.add(getLoggingStr());
		}
		if (frameworkGUI) {
			args.add("--use-framework-gui");
		} else {
			args.add("--no-framework-gui");
		}
		
		return args.toArray(new String[args.size()]);
	}
}
