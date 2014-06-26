package server.com.webHandler.pages.parts;

import server.htmlBuilder.meta.ListNavigationBar;

/**
 *
 * @author Andrew Vitkus
 */
public class StudentDataNavBar extends ListNavigationBar implements IStudentDataNavBar {

    public StudentDataNavBar() {
        super();
        setID("navbar");
        setClassName("center");

        addLink("Lookup", "lookup.php");
        addLink("Statistics", "statistics.php");
        addLink("Upload", "upload.php");
    }
}
