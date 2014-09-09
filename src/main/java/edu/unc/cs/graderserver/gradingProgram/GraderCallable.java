/**
 *
 */
package edu.unc.cs.graderServer.gradingProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author Andrew
 *
 */
public class GraderCallable implements Callable<String> {

    private final String[] args;
    
    public GraderCallable(String[] args) {
        this.args = Arrays.copyOf(args, args.length);
    }

    @Override
    public String call() throws Exception {
        ProcessBuilder lsPb;
        if (System.getProperty("os.name").contains("Windows")) {
            lsPb = new ProcessBuilder("dir", "target");
        } else {
            lsPb = new ProcessBuilder("ls", "target");
        }
        lsPb.redirectErrorStream(true);
        lsPb.directory(new File("graderProgram"));
        Process lspr = lsPb.start();
        lspr.waitFor();
        StringBuilder sb = new StringBuilder(100);
        try (InputStream is = lspr.getInputStream()) {
            while (is.available() != 0) {
                sb.append((char) is.read());
            }
        }

        String sbstr = sb.toString();

        int start = sbstr.indexOf("comp401-grader-");
        int end = sbstr.indexOf(".jar", start);

        String jar = Paths.get("target", sbstr.substring(start, end + 4)).toString();

        ArrayList<String> command = new ArrayList<>(5);
        command.add("java");
        command.add("-jar");
        command.add(jar);
        command.addAll(Arrays.asList(args));

        ProcessBuilder jarPb = new ProcessBuilder(command);
        jarPb.inheritIO();
        //jarPb.redirectErrorStream(true);
        jarPb.directory(new File("graderProgram"));

        Process pr = jarPb.start();
        pr.waitFor();
        StringBuilder output = new StringBuilder(500);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()))) {
            while (br.ready()) {
                output.append(br.readLine()).append("\n");
            }
        }
        return output.toString();
    }
}
