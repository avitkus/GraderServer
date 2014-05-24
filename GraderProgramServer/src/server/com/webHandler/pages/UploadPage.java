package server.com.webHandler.pages;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import server.htmlBuilder.form.input.FileField;
import server.htmlBuilder.form.input.IFileField;
import server.htmlBuilder.form.input.ISubmitButton;
import server.htmlBuilder.form.input.SubmitButton;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.ILink;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.ITitle;
import server.htmlBuilder.head.Link;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;
import server.utils.ConfigReader;
import server.utils.IConfigReader;

public class UploadPage extends HTMLFile implements IUploadPage {

    private static final String authKey;
    private static final Logger LOG = Logger.getLogger(UploadPage.class.getName());

    static {
        StringBuilder auth = new StringBuilder(50);
        Random rand = new Random();
        while (auth.length() < 50) {
            auth.append((char) rand.nextInt());
        }

        try {
            IConfigReader config = new ConfigReader("config/auth.properties");
            auth.setLength(0);
            auth.append(config.getString("database.view.authKey"));
        } catch (IOException ex) {
            Logger.getLogger(UploadPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        authKey = auth.toString();
    }
    private String onyen = "";
    private String user = "";
    private String year = "";
    private String season = "";
    private String auth = "";
    private boolean isAdmin;

    @Override
    public void setArgs(String args) {
        System.out.println(args);
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
        IDatabaseReader dr = new DatabaseReader();
        try {
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            dr.connect(config.getString("database.username"), config.getString("database.password"), "jdbc:" + config.getString("database.url"));

            try (ResultSet admins = dr.getAdminForUser(user)) {
                if (admins.isBeforeFirst()) {
                    isAdmin = true;
                }
            }
        } catch (SQLException | IOException e) {
            LOG.log(Level.FINER, null, e);
        } finally {
            try {
                dr.disconnect();
            } catch (SQLException ex) {
                Logger.getLogger(UploadPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        LOG.log(Level.INFO, "Authenticated as user {0}", user);
    }
    
    @Override
    public void setAuth(String auth) {
        this.auth = auth.replace("+", " ");
    }

    @Override
    public String getHTML() {
        System.out.println(user);
        System.out.println(auth);
        System.out.println(authKey);
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
        ITitle title = new Title("Upload to Grading Database");

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
        IDatabaseReader dr = new DatabaseReader();
        try {
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
        } finally {
            try {
                dr.disconnect();
            } catch (SQLException ex) {
                Logger.getLogger(UploadPage.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        title.addContent(new Header("Upload to Database", 1));
        bodyWrapper.addContent(title);

        IDivision content = new Division();
        content.setClass("content");
        content.addContent(new StudentDataNavBar());
        content.addContent(buildSubmitForm());
        bodyWrapper.addContent(content);
        body.addElement(bodyWrapper);

        setBody(body);
    }
    
    private IForm buildSubmitForm() throws FileNotFoundException, IOException {
        IDatabaseReader dr = new DatabaseReader();
        IForm form = new Form();
        ResultSet results = null;
        try {  
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            dr.connect(config.getString("database.username"), config.getString("database.password"), "jdbc:" + config.getString("database.url"));
            

            form.setMethod("post");
            form.setEncoding("multipart/form-data");
            form.setAction("upload_handler.php");
            form.setID("upload-form");
            form.setClassName("center");
            
            if (isAdmin) {
                form.addElement(buildDropDown(dr.getUsers(), "onyen", "onyen", "Onyen", onyen, true));
            } else {
                form.addElement(buildDropDown(new String[]{onyen}, "onyen", "Onyen", onyen, true));
            }
            form.addElement(new LineBreak());

            form.addElement(buildDropDown(getCourseAndSectionList(), "course", "Course", "", true));
            
            IFileField file = new FileField();
            file.addAcceptType("application/zip", "application/java-archive", "application/json");
            file.setForm("upload-form");
            file.setName("file");
            file.setID("file-select");
            file.setRequired(true);

            ILabel fileLabel = new Label();
            fileLabel.setLabel(new Text("File"));
            fileLabel.setForm("upload-form");
            fileLabel.setElementID("file-select");
            
            form.addElement(new LineBreak());

            ISubmitButton upload = new SubmitButton();
            upload.setValue("Upload");
            upload.setForm("upload-form");
            upload.setName("upload");

            form.addElement(fileLabel);
            form.addElement(file);
            form.addElement(new LineBreak());
            form.addElement(upload);
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
            try {
                dr.disconnect();
            } catch (SQLException ex) {
                Logger.getLogger(UploadPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return form;
    }
    
    private String[] getCourseAndSectionList() throws FileNotFoundException, IOException {
        IDatabaseReader dr = new DatabaseReader();
        ResultSet results = null;
        IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
        try {
            dr.connect(config.getString("database.username"), config.getString("database.password"), "jdbc:" + config.getString("database.url"));
            results = dr.getTerms();
            while(results.next()) {
                if (results.getBoolean("current")) {
                    year = Integer.toString(results.getInt("year"));
                    season = results.getString("season");
                }
            }
            results.close();
            results = dr.getCourses(year, season);
            ArrayList<String> courses = new ArrayList<>(5);
            while(results.next()) {
                courses.add(results.getString("name"));
            }
            results.close();
            String[] courseNameArr = courses.toArray(new String[courses.size()]);
            courses.clear();
            for(String name : courseNameArr) {
                results = dr.getSections(name, year, season);
                while(results.next()) {
                    courses.add(name + "-" + results.getString("section"));
                }
            }
            String[] courseArr = courses.toArray(new String[courses.size()]);
            Arrays.sort(courseArr, String::compareToIgnoreCase);
            return courses.toArray(new String[courses.size()]);
        } catch (SQLException e) {
            LOG.log(Level.FINER, null, e);
            return new String[]{};
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException e) {
                    LOG.log(Level.FINER, null, e);
                }
            }
            try {
                dr.disconnect();
            } catch (SQLException ex) {
                Logger.getLogger(UploadPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
}
