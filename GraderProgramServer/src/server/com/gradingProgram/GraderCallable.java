/**
 *
 */
package server.com.gradingProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        pb.directory(new File("graderProgram"));

        Process pr = pb.start();
        pr.waitFor();
        StringBuilder output = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()))) {
            while (br.ready()) {
                output.append(br.readLine()).append("\n");
            }
        }
        return output.toString();
    }

}
