package edu.unc.cs.graderServer.graderHandler.util;

import java.util.List;

/**
 * @author Andrew Vitkus
 *
 */
public interface IJSONReader {

    public String[][] getGrading();

    public String[] getComments();

    public INoteData getNotes();

    public Boolean[] getExtraCredit();

    public List<List<String>> getGradingTests();
}
