package server.com.webHandler.pages;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.com.webHandler.pages.helpers.AverageTableBuilder;
import server.com.webHandler.pages.helpers.ITableBuilder;
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
import server.htmlBuilder.body.IParagraph;
import server.htmlBuilder.body.ISpan;
import server.htmlBuilder.body.LineBreak;
import server.htmlBuilder.body.Paragraph;
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
import server.htmlBuilder.table.Table;
import server.utils.ConfigReader;
import server.utils.IConfigReader;

public class StudentDataStatisticsPage extends HTMLFile implements IStudentDataStatisticsPage {

    private static final Logger LOG = Logger.getLogger(StudentDataStatisticsPage.class.getName());

    private static final String authKey;
    private static final boolean showAdminOnyen;

    static {
        StringBuilder auth = new StringBuilder(50);
        boolean adminOnyen = false;
        Random rand = new Random();
        while (auth.length() < 50) {
            auth.append((char) rand.nextInt());
        }

        try {
            IConfigReader config = new ConfigReader("config/auth.properties");
            config.getString("database.view.authKey").ifPresent((key) -> {
                auth.setLength(0);
                auth.append(key);
            });
            adminOnyen = config.getBoolean("database.view.showAdminOnyen", false).get();
        } catch (IOException ex) {
            Logger.getLogger(StudentDataLookupPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        authKey = auth.toString();
        showAdminOnyen = adminOnyen;
    }
    private String assignment = "";
    private String auth = "";
    private String course = "";
    private boolean isAdmin;
    private String onyen = "";
    private String season = "";
    private String section = "";
    private String type = "";
    private String user = "";
    private String year = "";

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
    public void setAssignment(String assignment) {
        assignment = assignment.replace("%2C", ",");
        this.assignment = assignment.replace("+", " ");
    }

    @Override
    public void setAuth(String auth) {
        this.auth = auth.replace("+", " ");
    }

    @Override
    public void setCourse(String name) {
        course = name.replace("+", " ");
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

    @Override
    public void setOnyen(String onyen) {
        this.onyen = onyen.replace("+", " ");
    }

    @Override
    public void setSeason(String season) {
        this.season = season.replace("+", " ");
    }

    @Override
    public void setSection(String section) {
        this.section = section.replace("+", " ");
    }

    @Override
    public void setType(String type) {
        type = type.replace("%2C", ",");
        this.type = type.replace("+", " ");
    }

    @Override
    public void setUser(String user) {
        this.user = user.replace("+", " ");
        try {
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            IDatabaseReader dr = new DatabaseReader();
            dr.connect(config.getString("database.username").orElseThrow(IllegalArgumentException::new),
                    config.getString("database.password").orElseThrow(IllegalArgumentException::new),
                    "jdbc:" + config.getString("database.url").orElseThrow(IllegalArgumentException::new));

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
    public void setYear(String year) {
        this.year = year.replace("+", " ");
    }

    private ITable buildAverageTable() throws FileNotFoundException, IOException {
        IDatabaseReader dr = new DatabaseReader();
        ITable table = new Table();
        table.setClassName("center");
        ResultSet results = null;
        try {
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            dr.connect(config.getString("database.username").orElseThrow(IllegalArgumentException::new),
                    config.getString("database.password").orElseThrow(IllegalArgumentException::new),
                    "jdbc:" + config.getString("database.url").orElseThrow(IllegalArgumentException::new));

            if (!assignment.isEmpty() && !course.isEmpty()) {
                results = dr.getResultsForAll(doShowOnyen() ? onyen : "", assignment, type, course, section, year, season);
            }

            ITableBuilder tb = new AverageTableBuilder(results, dr);
            table = tb.getTable();
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

    private void buildBody() throws FileNotFoundException, IOException {
        IBody body = new Body();

        IDivision header = new Division();
        header.setClass("header-bar");

        ISpan userInfo = new Span();
        userInfo.setID("user-info");
        try {
            IDatabaseReader dr = new DatabaseReader();
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            dr.connect(config.getString("database.username").orElseThrow(IllegalArgumentException::new),
                    config.getString("database.password").orElseThrow(IllegalArgumentException::new),
                    "jdbc:" + config.getString("database.url").orElseThrow(IllegalArgumentException::new));
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
        title.addContent(new Header("Student Grading Database Statistics", 1));
        bodyWrapper.addContent(title);

        IDivision content = new Division();
        content.setClass("content");
        content.addContent(new StudentDataNavBar());
        content.addContent(buildForm());
        content.addContent(new HorizontalRule());
        if (!assignment.isEmpty() && !course.isEmpty()) {
            content.addContent(buildAverageTable());
        } else {
            IParagraph p = new Paragraph();
            p.addContent(new Text("Please select an assignment name and course to view statistics."));
            content.addContent(p);
        }
        bodyWrapper.addContent(content);
        body.addElement(bodyWrapper);

        setBody(body);
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

    private IForm buildForm() throws FileNotFoundException, IOException {
        IDatabaseReader dr = new DatabaseReader();
        IForm form = new Form();
        ResultSet results = null;
        try {
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            dr.connect(config.getString("database.username").orElseThrow(IllegalArgumentException::new),
                    config.getString("database.password").orElseThrow(IllegalArgumentException::new),
                    "jdbc:" + config.getString("database.url").orElseThrow(IllegalArgumentException::new));
            form.setMethod("post");
            form.setName("assignment_data");
            form.setAction("https://classroom.cs.unc.edu/~vitkus/grader/statistics.php");

            if (doShowOnyen()) {
                form.addElement(buildDropDown(dr.getUsers(), "onyen", "onyen", "Onyen", onyen));
            }

            form.addElement(buildDropDown(dr.getTypes(), "type", "name", "Type", type));
            form.addElement(buildDropDown(dr.getAssignments(type, course, section, year, season), "assignment", "name", "Name", assignment, true));
            form.addElement(buildDropDown(dr.getCourses(year, season), "course", "name", "Course", course, true));
            if (!course.isEmpty()) {
                form.addElement(buildDropDown(dr.getSections(course, year, season), "section", "section", "Section", section));
            }
            form.addElement(buildDropDown(dr.getTerms(), "year", "year", "Year", year));
            form.addElement(buildDropDown(dr.getTerms(), "season", "season", "Season", season));

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

    private void buildParts() throws FileNotFoundException, IOException {
        buildHead();
        buildBody();
    }

    private boolean doShowOnyen() {
        return showAdminOnyen && isAdmin;
    }
}
