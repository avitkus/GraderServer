package edu.unc.cs.graderServer.gradingProgram;

import edu.unc.cs.graderServer.utils.ConfigReader;
import edu.unc.cs.graderServer.utils.IConfigReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraderPool {

    private static final int defaultGraders = 25;
    private static final ExecutorService graderPool;

    static {
        int graders;
        try {
            IConfigReader config = new ConfigReader("./config/config.properties");
            graders = config.getInt("grader.maxGraders").orElse(defaultGraders);
        } catch (IOException ex) {
            Logger.getLogger(GraderPool.class.getName()).log(Level.SEVERE, null, ex);
            graders = defaultGraders;
        }
        graderPool = Executors.newFixedThreadPool(graders);
    }

    public static Future<String> runGrader(String[] args) throws IOException, InterruptedException {
        return graderPool.submit(new GraderCallable(args));
    }

    private GraderPool() {
    }
}
