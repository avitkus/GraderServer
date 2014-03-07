package server.com.graderHandler.sql;

import java.sql.SQLException;

/**
 * @author Andrew Vitkus
 *
 */
public interface IDatabaseWriter {
	public void connect(String username, String password, String server) throws SQLException;
	
	public void writeUser(String ONYEN, String UID) throws NumberFormatException, SQLException;
	public void writeUser(String ONYEN, int UID) throws SQLException;
	
	public void writeAssignment(String number, String course, String userID) throws NumberFormatException, SQLException;
	public void writeAssignment(int number, int course_id, int userID) throws SQLException;
	
	public void writeAssignment(String assignmenCatalogID, String userID) throws NumberFormatException, SQLException;
	public void writeAssignment(int assignmenCatalogID, int userID) throws SQLException;
	
	public void writeResult(String assignmenSubmissionID) throws NumberFormatException, SQLException;
	public void writeResult(int assignmenSubmissionID) throws SQLException;
	
	public void writeComments(String[] comments, String resusltID) throws NumberFormatException, SQLException;
	public void writeComments(String[] comments, int resusltID) throws SQLException;
	
	public void writeGradingParts(String[][] grading, Boolean[] extraCredit, String resultID) throws NumberFormatException, SQLException;
	public void writeGradingParts(String[][] grading, Boolean[] extraCredit, int resultID) throws SQLException;
	
	public void writeGradingTest(String name, String percent, String autoGraded, String gradingPartID) throws NumberFormatException, SQLException;
	public void writeGradingTest(String name, double percent, boolean autoGraded, int gradingPartID) throws SQLException;

	public void writeTestNotes(String[] notes, String gradingTestID) throws NumberFormatException, SQLException;
	public void writeTestNotes(String[] notes, int gradingTestID) throws SQLException;
	
	public void disconnect() throws SQLException;
}
