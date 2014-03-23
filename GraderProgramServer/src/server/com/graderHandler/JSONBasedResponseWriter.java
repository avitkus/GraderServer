package server.com.graderHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import server.com.graderHandler.pages.FailPage;
import server.com.graderHandler.pages.ISuccessPage;
import server.com.graderHandler.pages.SuccessPage;
import server.com.gradingProgram.IJSONReader;
import server.com.gradingProgram.INoteData;
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
            if (json.exists()) {
		IJSONReader reader = new JSONReader(json);
                response = new SuccessPage();
                        
                String[][] grading = reader.getGrading();
                INoteData notes = reader.getNotes();
                String[] comments = reader.getComments();
                ((ISuccessPage)response).setGrading(grading);
		((ISuccessPage)response).setNotes(notes);
		((ISuccessPage)response).setComments(comments);
                if (grading != null && notes != null && comments != null) {
                    return;
                }
            }
            response = new FailPage();
	}
}
