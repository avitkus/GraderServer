package server.com.webHandler.sql;

import java.sql.SQLException;
import java.sql.ResultSet;

/**
 * @author Andrew Vitkus
 *
 */
public interface IDatabaseReader {
	public void connect(String username, String password, String server) throws SQLException;
	
	public ResultSet getResultsForUser(String onyen) throws SQLException;
	
	public ResultSet getResultsForAssignment(String course, String assingment) throws SQLException;
	
	public ResultSet getResultsForAll(String onyen, String assignment, String type, String course, String section, String year, String season) throws SQLException;
	
	public ResultSet getGradingForResult(int id) throws SQLException;
	
	public ResultSet getCommentsForResult(int id) throws SQLException;
	
	public ResultSet getUserForResult(int id) throws SQLException;

	public ResultSet getAssignmentForResult(int id) throws SQLException;
	
	public ResultSet getTypeForAssignment(int id) throws SQLException;
	
	public ResultSet getCourseForResult(int id) throws SQLException;
	
	public ResultSet getTestsForGrading(int id) throws SQLException;
	
	public ResultSet getNotesForTest(int id) throws SQLException;
	
	public ResultSet getUsers() throws SQLException;
	
	public ResultSet getAssignments() throws SQLException;
	
	public ResultSet getTypes() throws SQLException;
	
	public ResultSet getAssignments(String type, String course, String section, String year, String season) throws SQLException;

	public ResultSet getCourses() throws SQLException;

	public ResultSet getCourses(String year, String season) throws SQLException;
	
	public ResultSet getSections(String course, String year, String season) throws SQLException;

	public ResultSet getTerms() throws SQLException;
	
	public ResultSet getAdminForUser(String onyen) throws SQLException;
	
	public void disconnect() throws SQLException;
}
