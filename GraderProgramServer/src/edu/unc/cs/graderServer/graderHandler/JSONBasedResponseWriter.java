package edu.unc.cs.graderServer.graderHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import edu.unc.cs.graderServer.graderHandler.pages.FailPage;
import edu.unc.cs.graderServer.graderHandler.pages.ISuccessPage;
import edu.unc.cs.graderServer.graderHandler.pages.StylizedSuccessPage;
import edu.unc.cs.graderServer.graderHandler.pages.SuccessPage;
import edu.unc.cs.graderServer.graderHandler.util.IJSONReader;
import edu.unc.cs.graderServer.graderHandler.util.INoteData;
import edu.unc.cs.graderServer.graderHandler.util.JSONReader;

/**
 * @author Andrew Vitkus
 *
 */
public class JSONBasedResponseWriter extends ResponseWriter {
    public JSONBasedResponseWriter(String jsonFileLoc, boolean stylized) throws FileNotFoundException, IOException {
        this(new File(jsonFileLoc));
    }
    
    public JSONBasedResponseWriter(String jsonFileLoc) throws FileNotFoundException, IOException {
        this(new File(jsonFileLoc), false);
    }
    
    public JSONBasedResponseWriter(File json) throws FileNotFoundException, IOException {
        this(json, false);
    }

    public JSONBasedResponseWriter(File json, boolean stylized) throws FileNotFoundException, IOException {
        if (json.exists()) {
            IJSONReader reader = new JSONReader(json);
            response = stylized ? new StylizedSuccessPage() : new SuccessPage();

            String[][] grading = reader.getGrading();
            INoteData notes = reader.getNotes();
            String[] comments = reader.getComments();
            ((ISuccessPage) response).setGrading(grading);
            ((ISuccessPage) response).setNotes(notes);
            ((ISuccessPage) response).setComments(comments);
            if (grading != null && notes != null && comments != null) {
                return;
            }
        }
        response = new FailPage();
    }
}
