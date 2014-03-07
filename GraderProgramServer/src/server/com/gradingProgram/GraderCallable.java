/**
 * 
 */
package server.com.gradingProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author Andrew
 *
 */
public class GraderCallable implements Callable<String> {
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private String[] args;
	
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	@Override
	public String call() throws Exception {
		ProcessBuilder lspb = null;
		if (System.getProperty("os.name").contains("Windows")) {
			lspb = new ProcessBuilder("dir", "target");
		} else {
			lspb = new ProcessBuilder("ls", "target");
		}
		lspb.redirectErrorStream(true);
		lspb.directory(new File("graderProgram"));
		Process lspr = lspb.start();
		lspr.waitFor();
		StringBuilder sb = new StringBuilder();
		try(InputStream is = lspr.getInputStream()) {
			while(is.available() != 0) {
				sb.append((char)is.read());
			}
		}
	
		String sbstr = sb.toString();
		
		int start = sbstr.indexOf("comp401-grader-");
		int end = sbstr.indexOf(".jar", start);
		
		String jar = "target" + FILE_SEPARATOR + sbstr.substring(start, end + 4);
		
		ArrayList<String> command = new ArrayList<>();
		command.add("java");
		command.add("-jar");
		command.add(jar);
		command.addAll(Arrays.asList(args));
		
		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		pb.directory(new File("graderProgram"));
	
		Process pr = pb.start();
		pr.waitFor();
		StringBuilder output = new StringBuilder();;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			while(br.ready()) {
				output.append(br.readLine()).append("\n");
			}
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return output.toString();
	}

}
