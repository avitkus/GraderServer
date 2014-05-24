package server.com.webHandler.pages;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.com.webHandler.pages.parts.StudentDataNavBar;
import server.com.webHandler.sql.DatabaseReader;
import server.com.webHandler.sql.IDatabaseReader;
import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.attributes.LinkTarget;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.Division;
import server.htmlBuilder.body.Header;
import server.htmlBuilder.body.HorizontalRule;
import server.htmlBuilder.body.Hyperlink;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.body.IDivision;
import server.htmlBuilder.body.IHyperlink;
import server.htmlBuilder.body.ISpan;
import server.htmlBuilder.body.LineBreak;
import server.htmlBuilder.body.Span;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.doctype.HTML5Doctype;
import server.htmlBuilder.form.Form;
import server.htmlBuilder.form.IForm;
import server.htmlBuilder.form.ILabel;
import server.htmlBuilder.form.IOption;
import server.htmlBuilder.form.ISelect;
import server.htmlBuilder.form.Label;
import server.htmlBuilder.form.Option;
import server.htmlBuilder.form.Select;
import server.htmlBuilder.form.input.ISubmitButton;
import server.htmlBuilder.form.input.SubmitButton;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.ILink;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.ITitle;
import server.htmlBuilder.head.Link;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;
import server.htmlBuilder.table.ITable;
import server.htmlBuilder.table.ITableData;
import server.htmlBuilder.table.ITableHeader;
import server.htmlBuilder.table.ITableRow;
import server.htmlBuilder.table.Table;
import server.htmlBuilder.table.TableData;
import server.htmlBuilder.table.TableHeader;
import server.htmlBuilder.table.TableRow;
import server.utils.ConfigReader;
import server.utils.IConfigReader;

public class StudentDataLookupPage extends HTMLFile implements IStudentDataLookupPage {

    private static final String authKey;
    private static final boolean showUserOnyen;
    private static final boolean showAdminOnyen;
    private static final Logger LOG = Logger.getLogger(StudentDataLookupPage.class.getName());

    static {
        StringBuilder auth = new StringBuilder(50);
        boolean userOnyen = false;
        boolean adminOnyen = false;
        Random rand = new Random();
        while (auth.length() < 50) {
            auth.append((char) rand.nextInt());
        }

        try {
            IConfigReader config = new ConfigReader("config/auth.properties");
            auth = new StringBuilder(config.getString("database.view.authKey"));
            userOnyen = config.getBoolean("database.view.showUserOnyen");
            adminOnyen = config.getBoolean("database.view.showAdminOnyen");
        } catch (IOException ex) {
            Logger.getLogger(StudentDataLookupPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        authKey = auth.toString();
        showUserOnyen = userOnyen;
        showAdminOnyen = adminOnyen;

    }
    private String onyen = "";
    private String user = "";
    private String course = "";
    private String section = "";
    private String year = "";
    private String season = "";
    private String assignment = "";
    private String type = "";
    private String view = "";
    private String auth = "";
    private boolean isAdmin;

    @Override
    public void setArgs(String args) {
        String[] temp = args.split("\\s");
        args = temp[temp.length - 1];
        args = args.trim();
        //System.out.println("Args: " + args);
        String[] argList = args.split("&");
        for (String arg : argList) {
            if (arg.startsWith("onyen=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setOnyen(argSplit[1]);
                }
            } else if (arg.startsWith("user=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setUser(argSplit[1]);
                }
            } else if (arg.startsWith("course=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setCourse(argSplit[1]);
                }
            } else if (arg.startsWith("year=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setYear(argSplit[1]);
                }
            } else if (arg.startsWith("season=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setSeason(argSplit[1]);
                }
            } else if (arg.startsWith("assignment=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setAssignment(argSplit[1]);
                }
            } else if (arg.startsWith("type=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setType(argSplit[1]);
                }
            } else if (arg.startsWith("view=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setView(argSplit[1]);
                }
            } else if (arg.startsWith("section=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setSection(argSplit[1]);
                }
            } else if (arg.startsWith("auth=")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setAuth(argSplit[1]);
                }
            }
        }
    }

    @Override
    public void setOnyen(String onyen) {
        this.onyen = onyen.replace("+", " ");
    }

    @Override
    public void setUser(String user) {
        this.user = user.replace("+", " ");
        try {
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            IDatabaseReader dr = new DatabaseReader();
            dr.connect(config.getString("database.username"), config.getString("database.password"), "jdbc:" + config.getString("database.url"));

            try (ResultSet admins = dr.getAdminForUser(user)) {
                if (admins.isBeforeFirst()) {
                    isAdmin = true;
                }
            }
        } catch (SQLException | IOException e) {
            LOG.log(Level.FINER, null, e);
        }
        LOG.log(Level.INFO, "Authenticated as user {0}", user);
    }

    @Override
    public void setCourse(String name) {
        course = name.replace("+", " ");
    }

    @Override
    public void setSection(String section) {
        this.section = section.replace("+", " ");
    }

    @Override
    public void setYear(String year) {
        this.year = year.replace("+", " ");
    }

    @Override
    public void setSeason(String season) {
        this.season = season.replace("+", " ");
    }

    @Override
    public void setAssignment(String assignment) {
        assignment = assignment.replace("%2C", ",");
        this.assignment = assignment.replace("+", " ");
    }

    @Override
    public void setType(String type) {
        type = type.replace("%2C", ",");
        this.type = type.replace("+", " ");
    }

    @Override
    public void setView(String view) {
        this.view = view.replace("+", " ");
    }

    @Override
    public void setAuth(String auth) {
        this.auth = auth.replace("+", " ");
    }

    @Override
    public String getHTML() {
        if (user.isEmpty() || !auth.equals(authKey)) {
            return "<html><head><title>fail</title></head><body><h1>failure</h1></body></html>";
        }
        try {
            setDoctype(new HTML5Doctype());
            buildParts();
        } catch (FileNotFoundException e) {
            LOG.log(Level.FINER, null, e);
        } catch (IOException e) {
            LOG.log(Level.FINER, null, e);
        }
        return super.getHTML();
    }

    private void buildParts() throws FileNotFoundException, IOException {
        buildHead();
        buildBody();
    }

    private void buildHead() {
        ITitle title = new Title("Student Grading Database Lookup");

        IMetaAttr charset = new MetaAttr();
        charset.addAttribute("charset", "UTF-8");

        ILink faviconLink = new Link();
        faviconLink.setRelation("shortcut icon");
        faviconLink.addLinkAttribtue("href", "favicon.ico");
        faviconLink.addLinkAttribtue("type", "image/vnd.microsoft.icon");

        setHead(new Head(title, charset, faviconLink));

        addCSSFile("grader.css");
    }

    private void buildBody() throws FileNotFoundException, IOException {
        IBody body = new Body();

        IDivision header = new Division();
        header.setClass("header-bar");

        ISpan userInfo = new Span();
        userInfo.setID("user-info");
        try {
            IDatabaseReader dr = new DatabaseReader();
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            dr.connect(config.getString("database.username"), config.getString("database.password"), "jdbc:" + config.getString("database.url"));
            ResultSet admins = dr.getAdminForUser(user);
            if (admins.first()) {
                userInfo.addContent(new Text(user + "&ndash;admin"));
            } else {
                userInfo.addContent(new Text(user));
            }
        } catch (SQLException e) {
            LOG.log(Level.FINER, null, e);
        }
        header.addContent(userInfo);

        IDivision logOff = new Division();
        logOff.setID("log-off");
        IHyperlink logOffLink = new Hyperlink();
        logOffLink.addContent(new Text("Log Off"));
        logOffLink.setURL("logoff.php");
        logOffLink.setTarget(LinkTarget.SELF);
        logOff.addContent(logOffLink);

        header.addContent(logOff);

        header.addContent(new LineBreak());

        body.addElement(header);

        IDivision bodyWrapper = new Division();
        bodyWrapper.setClass("body-wrapper");

        IDivision title = new Division();
        title.setClass("title");
        title.addContent(new Header("Student Grading Database Lookup", 1));
        bodyWrapper.addContent(title);

        IDivision content = new Division();
        content.setClass("content");
        content.addContent(new StudentDataNavBar());
        content.addContent(buildForm());
        content.addContent(new HorizontalRule());
        content.addContent(buildAssignmentTable());
        bodyWrapper.addContent(content);
        body.addElement(bodyWrapper);

        setBody(body);
    }


    private ITable buildAssignmentTable() throws FileNotFoundException, IOException {
        IDatabaseReader dr = new DatabaseReader();
        ITable table = new Table();
        table.setClassName("center");
        ResultSet results = null;
        try {
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            dr.connect(config.getString("database.username"), config.getString("database.password"), "jdbc:" + config.getString("database.url"));

            ITableRow headerRow = new TableRow();

            if (view.isEmpty() || view.equals("Submissions")) {
                if (doShowOnyen() && onyen.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Onyen")));
                }
                if (course.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Course")));
                }
                if (section.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Section")));
                }
                if (assignment.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Name")));
                }
                if (type.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Type")));
                }
                ITableHeader dateHeader = new TableHeader(new Text("Date/Time"));
                ITableHeader scoreHeader = new TableHeader(new Text("Score"));
                ITableHeader autoGradeHeader = new TableHeader(new Text("Autograded"));

                headerRow.addDataPart(dateHeader);
                headerRow.addDataPart(scoreHeader);
                headerRow.addDataPart(autoGradeHeader);
            } else if (view.equals("Comments")) {
                if (doShowOnyen() && onyen.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Onyen")));
                }
                if (course.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Course")));
                }
                if (section.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Section")));
                }
                if (assignment.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Name")));
                }
                if (type.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Type")));
                }
                ITableHeader dateHeader = new TableHeader(new Text("Date/Time"));
                ITableHeader numHeader = new TableHeader(new Text("#"));
                ITableHeader commentHeader = new TableHeader(new Text("Comment"));

                headerRow.addDataPart(dateHeader);
                headerRow.addDataPart(numHeader);
                headerRow.addDataPart(commentHeader);
            } else if (view.equals("Grading")) {
                if (doShowOnyen() && onyen.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Onyen")));
                }
                if (course.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Course")));
                }
                if (section.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Section")));
                }
                if (assignment.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Name")));
                }
                if (type.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Type")));
                }
                ITableHeader dateHeader = new TableHeader(new Text("Date/Time"));
                ITableHeader nameHeader = new TableHeader(new Text("Name"));
                ITableHeader pointsHeader = new TableHeader(new Text("Points"));
                ITableHeader possibleHeader = new TableHeader(new Text("Possible"));
                ITableHeader ecHeader = new TableHeader(new Text("Extra Credit"));
                ITableHeader autoGradeHeader = new TableHeader(new Text("Autograded"));

                headerRow.addDataPart(dateHeader);
                headerRow.addDataPart(nameHeader);
                headerRow.addDataPart(pointsHeader);
                headerRow.addDataPart(possibleHeader);
                headerRow.addDataPart(ecHeader);
                headerRow.addDataPart(autoGradeHeader);
            } else if (view.equals("Notes")) {
                if (doShowOnyen() && onyen.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Onyen")));
                }
                if (course.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Course")));
                }
                if (section.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Section")));
                }
                if (assignment.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Name")));
                }
                if (type.isEmpty()) {
                    headerRow.addDataPart(new TableHeader(new Text("Type")));
                }
                ITableHeader dateHeader = new TableHeader(new Text("Date/Time"));
                ITableHeader partHeader = new TableHeader(new Text("Part"));
                ITableHeader testHeader = new TableHeader(new Text("Test"));
                ITableHeader noteHeader = new TableHeader(new Text("Note"));

                headerRow.addDataPart(dateHeader);
                headerRow.addDataPart(partHeader);
                headerRow.addDataPart(testHeader);
                headerRow.addDataPart(noteHeader);
            }
            table.addRow(headerRow);

            results = dr.getResultsForAll(doShowOnyen() ? onyen : "", assignment, type, course, section, year, season);

            int i = 0;
            results.afterLast();
            while (results.previous()) {
                if (view.isEmpty() || view.equals("Submissions")) {
                    ITableRow row = new TableRow();
                    if (i % 2 == 1) {
                        row.setClassName("highlight-row");
                    }
                    if (doShowOnyen() && onyen.isEmpty()) {
                        try (ResultSet users = dr.getUserForResult(results.getInt("id"))) {
                            users.first();
                            row.addDataPart(new TableData(new Text(users.getString("onyen"))));
                        }
                    }
                    if (course.isEmpty()) {
                        try (ResultSet courses = dr.getCourseForResult(results.getInt("id"))) {
                            courses.first();
                            row.addDataPart(new TableData(new Text(courses.getString("name"))));
                        }
                    }
                    if (section.isEmpty()) {
                        try (ResultSet courses = dr.getCourseForResult(results.getInt("id"))) {
                            courses.first();
                            row.addDataPart(new TableData(new Text(Integer.toString(courses.getInt("section")))));
                        }
                    }
                    if (assignment.isEmpty()) {
                        try (ResultSet assignments = dr.getAssignmentForResult(results.getInt("id"))) {
                            assignments.first();
                            row.addDataPart(new TableData(new Text(assignments.getString("name"))));
                        }
                    }
                    if (type.isEmpty()) {
                        try (ResultSet assignments = dr.getAssignmentForResult(results.getInt("id"))) {
                            assignments.first();
                            try (ResultSet assignmentType = dr.getTypeForAssignment(assignments.getInt("id"))) {
                                assignmentType.first();
                                row.addDataPart(new TableData(new Text(assignmentType.getString("name"))));
                            }
                        }
                    }
                    row.addDataPart(new TableData(new Text(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(results.getTimestamp("date")))));

                    try (ResultSet grading = dr.getGradingForResult(results.getInt("id"))) {
                        int points = 0;
                        int possible = 0;
                        double autoGraded = 0;
                        while (grading.next()) {
                            points += grading.getInt("points");
                            possible += grading.getInt("possible");
                            autoGraded += grading.getDouble("auto_graded_percent");
                        }
                        grading.last();
                        double score = (double) points / possible;
                        score = Math.round(score * 1000.) / 10.;
                        autoGraded /= grading.getRow();
                        autoGraded = Math.round(autoGraded * 10.) / 10.;
                        row.addDataPart(new TableData(new Text(score + "%")));
                        row.addDataPart(new TableData(new Text(autoGraded + "%")));
                    }

                    table.addRow(row);
                } else if (view.equals("Comments")) {
                    TableData dateTime = new TableData(new Text(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(results.getTimestamp("date"))));
                    try (ResultSet comments = dr.getCommentsForResult(results.getInt("id"))) {
                        ITableRow row = new TableRow();
                        ITableData date = new TableData();
                        date.addContent(new Text(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(results.getTimestamp("date"))));
                        comments.last();
                        int dateSpan = comments.getRow();
                        comments.beforeFirst();
                        date.setRowSpan(dateSpan);
                        date.setClassName("highlight-row");
                        row.addDataPart(date);
                        int commentNum = 1;
                        while (comments.next()) {
                            if (i % 2 == 1) {
                                row.setClassName("highlight-row");
                            }
                            if (doShowOnyen() && onyen.isEmpty()) {
                                try (ResultSet users = dr.getUserForResult(results.getInt("id"))) {
                                    users.first();
                                    row.addDataPart(new TableData(new Text(users.getString("onyen"))));
                                }
                            }
                            if (course.isEmpty()) {
                                try (ResultSet courses = dr.getCourseForResult(results.getInt("id"))) {
                                    courses.first();
                                    row.addDataPart(new TableData(new Text(courses.getString("name"))));
                                }
                            }
                            if (section.isEmpty()) {
                                try (ResultSet courses = dr.getCourseForResult(results.getInt("id"))) {
                                    courses.first();
                                    row.addDataPart(new TableData(new Text(Integer.toString(courses.getInt("section")))));
                                }
                            }
                            if (assignment.isEmpty()) {
                                try (ResultSet assignments = dr.getAssignmentForResult(results.getInt("id"))) {
                                    assignments.first();
                                    row.addDataPart(new TableData(new Text(assignments.getString("name"))));
                                }
                            }
                            if (type.isEmpty()) {
                                try (ResultSet assignments = dr.getAssignmentForResult(results.getInt("id"))) {
                                    assignments.first();
                                    try (ResultSet assignmentType = dr.getTypeForAssignment(assignments.getInt("id"))) {
                                        assignmentType.first();
                                        row.addDataPart(new TableData(new Text(assignmentType.getString("name"))));
                                    }
                                }
                            }
                            row.addDataPart(dateTime);
                            row.addDataPart(new TableData(new Text("" + commentNum++)));
                            row.addDataPart(new TableData(new Text(comments.getString("comment"))));

                            table.addRow(row);
                            row = new TableRow();
                        }
                    }
                } else if (view.equals("Grading")) {
                    try (ResultSet grading = dr.getGradingForResult(results.getInt("id"))) {
                        ITableRow row = new TableRow();
                        ITableData date = new TableData();
                        date.addContent(new Text(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(results.getTimestamp("date"))));
                        grading.last();
                        int dateSpan = grading.getRow();
                        grading.next();
                        while (grading.previous()) {
                            try (ResultSet tests = dr.getTestsForGrading(grading.getInt("id"))) {
                                tests.last();
                                dateSpan += tests.getRow();
                            }
                        }
                        if (dateSpan > 1) {
                            date.setRowSpan(dateSpan);
                        }
                        date.setClassName("highlight-row");
                        grading.beforeFirst();
                        if (doShowOnyen() && onyen.isEmpty()) {
                            try (ResultSet users = dr.getUserForResult(results.getInt("id"))) {
                                users.first();
                                ITableData userData = new TableData(new Text(users.getString("onyen")));
                                if (dateSpan > 1) {
                                    userData.setRowSpan(dateSpan);
                                }
                                userData.setClassName("highlight-row");
                                row.addDataPart(userData);
                            }
                        }
                        if (course.isEmpty()) {
                            try (ResultSet courses = dr.getCourseForResult(results.getInt("id"))) {
                                courses.first();
                                ITableData courseData = new TableData(new Text(courses.getString("name")));
                                if (dateSpan > 1) {
                                    courseData.setRowSpan(dateSpan);
                                }
                                courseData.setClassName("highlight-row");
                                row.addDataPart(courseData);
                            }
                        }
                        if (section.isEmpty()) {
                            try (ResultSet courses = dr.getCourseForResult(results.getInt("id"))) {
                                courses.first();
                                ITableData sectionData = new TableData(new Text(Integer.toString(courses.getInt("section"))));
                                if (dateSpan > 1) {
                                    sectionData.setRowSpan(dateSpan);
                                }
                                sectionData.setClassName("highlight-row");
                                row.addDataPart(sectionData);
                            }
                        }
                        if (assignment.isEmpty()) {
                            try (ResultSet assignments = dr.getAssignmentForResult(results.getInt("id"))) {
                                assignments.first();
                                ITableData assignmentData = new TableData(new Text(assignments.getString("name")));
                                if (dateSpan > 1) {
                                    assignmentData.setRowSpan(dateSpan);
                                }
                                assignmentData.setClassName("highlight-row");
                                row.addDataPart(assignmentData);
                            }
                        }
                        if (type.isEmpty()) {
                            try (ResultSet assignments = dr.getAssignmentForResult(results.getInt("id"))) {
                                assignments.first();
                                try (ResultSet assignmentType = dr.getTypeForAssignment(assignments.getInt("id"))) {
                                    assignmentType.first();
                                    ITableData typeData = new TableData(new Text(assignmentType.getString("name")));
                                    if (dateSpan > 1) {
                                        typeData.setRowSpan(dateSpan);
                                    }
                                    typeData.setClassName("highlight-row");
                                    row.addDataPart(typeData);
                                }
                            }
                        }
                        row.addDataPart(date);
                        while (grading.next()) {
                            row.setClassName("highlight-row");

                            row.addDataPart(new TableData(new Text(grading.getString("name"))));
                            row.addDataPart(new TableData(new Text("" + grading.getInt("points"))));
                            row.addDataPart(new TableData(new Text("" + grading.getInt("possible"))));
                            row.addDataPart(new TableData(new Text("" + (grading.getBoolean("extra_credit") ? "Yes" : "No"))));
                            row.addDataPart(new TableData(new Text("" + (Math.round(grading.getDouble("auto_graded_percent") * 100.) / 100.) + "%")));

                            table.addRow(row);
                            row = new TableRow();
                            
                            try (ResultSet tests = dr.getTestsForGrading(grading.getInt("id"))) {
                                while (tests.next()) {
                                    row.addDataPart(new TableData(new Text(tests.getString("name"))));
                                    ITableData percentData = new TableData(new Text("" + (Math.round(tests.getDouble("percent") * 1000.) / 10.) + "%"));
                                    percentData.setColSpan(2);
                                    row.addDataPart(percentData);
                                    row.addDataPart(new TableData(new Text("")));
                                    row.addDataPart(new TableData(new Text("" + (tests.getBoolean("auto_graded") ? "Yes" : "No"))));

                                    table.addRow(row);
                                    row = new TableRow();
                                }
                            }
                        }
                    }
                } else if (view.equals("Notes")) {
                    try (ResultSet grading = dr.getGradingForResult(results.getInt("id"))) {
                        ITableRow row = new TableRow();
                        ITableData date = new TableData();
                        date.addContent(new Text(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(results.getTimestamp("date"))));
                        int dateSpan = 0;
                        while (grading.next()) {
                            try (ResultSet tests = dr.getTestsForGrading(grading.getInt("id"))) {
                                while (tests.next()) {
                                    try (ResultSet notes = dr.getNotesForTest(tests.getInt("id"))) {
                                        notes.last();
                                        dateSpan += notes.getRow();
                                    }
                                }
                            }
                        }
                        if (dateSpan == 0) {
                            continue;
                        }
                        if (doShowOnyen() && onyen.isEmpty()) {
                            try (ResultSet users = dr.getUserForResult(results.getInt("id"))) {
                                users.first();
                                ITableData userData = new TableData(new Text(users.getString("onyen")));
                                if (dateSpan > 1) {
                                    userData.setRowSpan(dateSpan);
                                }
                                row.addDataPart(userData);
                            }
                        }
                        if (course.isEmpty()) {
                            try (ResultSet courses = dr.getCourseForResult(results.getInt("id"))) {
                                courses.first();
                                ITableData courseData = new TableData(new Text(courses.getString("name")));
                                if (dateSpan > 1) {
                                    courseData.setRowSpan(dateSpan);
                                }
                                row.addDataPart(courseData);
                            }
                        }
                        if (section.isEmpty()) {
                            try (ResultSet courses = dr.getCourseForResult(results.getInt("id"))) {
                                courses.first();
                                ITableData sectionData = new TableData(new Text(Integer.toString(courses.getInt("section"))));
                                if (dateSpan > 1) {
                                    sectionData.setRowSpan(dateSpan);
                                }
                                row.addDataPart(sectionData);
                            }
                        }
                        if (assignment.isEmpty()) {
                            try (ResultSet assignments = dr.getAssignmentForResult(results.getInt("id"))) {
                                assignments.first();
                                ITableData assignmentData = new TableData(new Text(assignments.getString("name")));
                                if (dateSpan > 1) {
                                    assignmentData.setRowSpan(dateSpan);
                                }
                                row.addDataPart(assignmentData);
                            }
                        }
                        if (type.isEmpty()) {
                            try (ResultSet assignments = dr.getAssignmentForResult(results.getInt("id"))) {
                                assignments.first();
                                try (ResultSet assignmentType = dr.getTypeForAssignment(assignments.getInt("id"))) {
                                    assignmentType.first();
                                    ITableData typeData = new TableData(new Text(assignmentType.getString("name")));
                                    if (dateSpan > 1) {
                                        typeData.setRowSpan(dateSpan);
                                    }
                                    row.addDataPart(typeData);
                                }
                            }
                        }
                        date.setRowSpan(dateSpan);
                        grading.beforeFirst();
                        row.addDataPart(date);
                        while (grading.next()) {
                            try (ResultSet tests = dr.getTestsForGrading(grading.getInt("id"))) {
                                ITableData gradingName = new TableData(new Text(grading.getString("name")));
                                if (tests.isBeforeFirst()) {
                                    int gradingNameSpan = 0;
                                    while (tests.next()) {
                                        try (ResultSet notes = dr.getNotesForTest(tests.getInt("id"))) {
                                            if (notes.isBeforeFirst()) {
                                                notes.last();
                                                gradingNameSpan += notes.getRow();
                                            }
                                        }
                                    }
                                    tests.beforeFirst();
                                    if (gradingNameSpan > 1) {
                                        gradingName.setRowSpan(gradingNameSpan);
                                    }
                                    if (gradingNameSpan > 0) {
                                        row.addDataPart(gradingName);
                                    }
                                }
                                while (tests.next()) {
                                    ITableData test = new TableData(new Text(tests.getString("name")));

                                    try (ResultSet notes = dr.getNotesForTest(tests.getInt("id"))) {
                                        if (notes.isBeforeFirst()) {
                                            notes.last();
                                            int testSpan = notes.getRow();
                                            notes.beforeFirst();
                                            if (testSpan > 1) {
                                                test.setRowSpan(testSpan);
                                            }
                                            if (testSpan != 0) {
                                                row.addDataPart(test);
                                            }
                                        }
                                        while (notes.next()) {
                                            if (i % 2 == 1) {
                                                row.setClassName("highlight-row");
                                            }
                                            row.addDataPart(new TableData(new Text(notes.getString("note"))));
                                            table.addRow(row);
                                            row = new TableRow();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                i++;
            }
        } catch (SQLException e) {
            LOG.log(Level.FINER, null, e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    LOG.log(Level.FINER, null, e);
                }
            }
        }
        return table;
    }

    private IForm buildForm() throws FileNotFoundException, IOException {
        IDatabaseReader dr = new DatabaseReader();
        IForm form = new Form();
        ResultSet results = null;
        try {
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            dr.connect(config.getString("database.username"), config.getString("database.password"), "jdbc:" + config.getString("database.url"));
            form.setMethod("post");
            form.setName("assignment_data");
            form.setAction("https://classroom.cs.unc.edu/~vitkus/grader/lookup.php");

            if (isAdmin && showAdminOnyen) {
                form.addElement(buildDropDown(dr.getUsers(), "onyen", "onyen", "Onyen", onyen));
            } else if (showUserOnyen) {
                form.addElement(buildDropDown(new String[]{onyen}, "onyen", "Onyen", "onyen"));
            }

            form.addElement(buildDropDown(dr.getTypes(), "type", "name", "Type", type));
            form.addElement(buildDropDown(dr.getAssignments(type, course, section, year, season), "assignment", "name", "Name", assignment));
            form.addElement(buildDropDown(dr.getCourses(year, season), "course", "name", "Course", course));
            if (!course.isEmpty()) {
                form.addElement(buildDropDown(dr.getSections(course, year, season), "section", "section", "Section", section));
            }
            form.addElement(buildDropDown(dr.getTerms(), "year", "year", "Year", year));
            form.addElement(buildDropDown(dr.getTerms(), "season", "season", "Season", season));
            form.addElement(buildDropDown(new String[]{"Submissions", "Grading", "Notes", "Comments"}, "view", "View", view));

            ISubmitButton submit = new SubmitButton();
            //submit.setForm("assignment_data");
            submit.setName("assignment_data_submit");
            submit.setValue("Submit");
            form.addElement(submit);

        } catch (SQLException e) {
            LOG.log(Level.FINER, null, e);
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    LOG.log(Level.FINER, null, e);
                }
            }
        }

        return form;
    }

    private ILabel buildDropDown(ResultSet results, String id, String key, String name, String defaultVal) throws SQLException {
        return buildDropDown(results, id, key, name, defaultVal, false);
    }

    private ILabel buildDropDown(ResultSet results, String id, String key, String name, String defaultVal, boolean required) throws SQLException {
        ISelect select = new Select();
        select.setRequired(required);
        select.setName(id);

        IOption blank = new Option();
        blank.setValue("");
        select.addOption(blank);
        while (results.next()) {
            String result = results.getString(key);
            IOption option = new Option();
            option.setText(result);
            option.setValue(result);
            if (result.equals(defaultVal)) {
                option.setSelected(true);
            }
            select.addOption(option);
        }

        ILabel assignmentLabel = new Label();
        assignmentLabel.setLabel(new Text(name));
        assignmentLabel.setElement(select);
        results.close();

        return assignmentLabel;
    }

    private ILabel buildDropDown(String[] options, String id, String name, String defaultVal) throws SQLException {
        return buildDropDown(options, id, name, defaultVal, false);
    }

    private ILabel buildDropDown(String[] options, String id, String name, String defaultVal, boolean required) throws SQLException {
        ISelect select = new Select();
        select.setRequired(required);
        select.setName(id);

        for (String s : options) {
            IOption option = new Option();
            option.setText(s);
            option.setValue(s);
            if (s.equals(defaultVal)) {
                option.setSelected(true);
            }
            select.addOption(option);
        }

        ILabel assignmentLabel = new Label();
        assignmentLabel.setLabel(new Text(name));
        assignmentLabel.setElement(select);

        return assignmentLabel;
    }
    
    private boolean doShowOnyen() {
        return showUserOnyen || (showAdminOnyen && isAdmin);
    }
}
