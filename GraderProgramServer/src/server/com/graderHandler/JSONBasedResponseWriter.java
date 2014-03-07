package server.com.graderHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import server.com.gradingProgram.IJSONReader;
import server.com.gradingProgram.JSONReader;

/**
 * @author Andrew Vitkus
 *
 */
public class JSONBasedResponseWriter extends ResponseWriter {
	
	public JSONBasedResponseWriter(String jsonFileLoc) throws FileNotFoundException, IOException {
		this(new File(jsonFileLoc));
	}
	
	public JSONBasedResponseWriter(File json) throws FileNotFoundException, IOException {
		IJSONReader reader = new JSONReader(json);
		setGrading(reader.getGrading());
		setNotes(reader.getNotes());
		setComments(reader.getComments());
	}
}
