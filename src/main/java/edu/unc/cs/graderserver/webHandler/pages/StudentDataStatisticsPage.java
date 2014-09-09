package edu.unc.cs.graderServer.webHandler.pages;

import edu.unc.cs.graderServer.utils.ConfigReader;
import edu.unc.cs.graderServer.utils.IConfigReader;
import edu.unc.cs.graderServer.webHandler.pages.helpers.AverageTableBuilder;
import edu.unc.cs.graderServer.webHandler.pages.helpers.ITableBuilder;
import edu.unc.cs.graderServer.webHandler.pages.parts.StudentDataNavBar;
import edu.unc.cs.graderServer.webHandler.sql.DatabaseReader;
import edu.unc.cs.graderServer.webHandler.sql.IDatabaseReader;
import edu.unc.cs.htmlBuilder.HTMLFile;
import edu.unc.cs.htmlBuilder.attributes.LinkTarget;
import edu.unc.cs.htmlBuilder.body.Body;
import edu.unc.cs.htmlBuilder.body.Division;
import edu.unc.cs.htmlBuilder.body.Header;
import edu.unc.cs.htmlBuilder.body.HorizontalRule;
import edu.unc.cs.htmlBuilder.body.Hyperlink;
import edu.unc.cs.htmlBuilder.body.IBody;
import edu.unc.cs.htmlBuilder.body.IDivision;
import edu.unc.cs.htmlBuilder.body.IHyperlink;
import edu.unc.cs.htmlBuilder.body.IParagraph;
import edu.unc.cs.htmlBuilder.body.ISpan;
import edu.unc.cs.htmlBuilder.body.LineBreak;
import edu.unc.cs.htmlBuilder.body.Paragraph;
import edu.unc.cs.htmlBuilder.body.Span;
import edu.unc.cs.htmlBuilder.body.Text;
import edu.unc.cs.htmlBuilder.doctype.HTML5Doctype;
import edu.unc.cs.htmlBuilder.form.Form;
import edu.unc.cs.htmlBuilder.form.IForm;
import edu.unc.cs.htmlBuilder.form.ILabel;
import edu.unc.cs.htmlBuilder.form.IOption;
import edu.unc.cs.htmlBuilder.form.ISelect;
import edu.unc.cs.htmlBuilder.form.Label;
import edu.unc.cs.htmlBuilder.form.Option;
import edu.unc.cs.htmlBuilder.form.Select;
import edu.unc.cs.htmlBuilder.form.input.ISubmitButton;
import edu.unc.cs.htmlBuilder.form.input.SubmitButton;
import edu.unc.cs.htmlBuilder.head.Head;
import edu.unc.cs.htmlBuilder.head.ILink;
import edu.unc.cs.htmlBuilder.head.IMetaAttr;
import edu.unc.cs.htmlBuilder.head.ITitle;
import edu.unc.cs.htmlBuilder.head.Link;
import edu.unc.cs.htmlBuilder.head.MetaAttr;
import edu.unc.cs.htmlBuilder.head.Title;
import edu.unc.cs.htmlBuilder.table.ITable;
import edu.unc.cs.htmlBuilder.table.Table;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            IConfigReader properties = new ConfigReader("config/auth.properties");
            properties.getString("database.view.authKey").ifPresent((key) -> {
                auth.setLength(0);
                auth.append(key);
            });
            properties = new ConfigReader("config/config.properties");
            adminOnyen = properties.getBoolean("database.view.showAdminOnyen", false).get();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
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
