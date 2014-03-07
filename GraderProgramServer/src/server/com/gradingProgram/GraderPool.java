package server.com.gradingProgram;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Andrew
 *
 */
public class GraderPool {
	private static final int maxGraders = 50;
	
	private static final ExecutorService graderPool;
	
	static {
		graderPool = Executors.newFixedThreadPool(maxGraders);
	}
	
	public static Future<String> runGrader(String[] args) throws IOException, InterruptedException {
		GraderCallable grader = new GraderCallable();
		grader.setArgs(args);
		return graderPool.submit(grader);
	}
}
