package edu.unc.cs.graderServer.webHandler.pages;

import edu.unc.cs.htmlBuilder.HTMLFile;
import edu.unc.cs.htmlBuilder.attributes.LinkTarget;
import edu.unc.cs.htmlBuilder.attributes.TextStyle;
import edu.unc.cs.htmlBuilder.body.Body;
import edu.unc.cs.htmlBuilder.body.Division;
import edu.unc.cs.htmlBuilder.body.Header;
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
import edu.unc.cs.htmlBuilder.form.FieldSet;
import edu.unc.cs.htmlBuilder.form.Form;
import edu.unc.cs.htmlBuilder.form.IFieldSet;
import edu.unc.cs.htmlBuilder.form.IForm;
import edu.unc.cs.htmlBuilder.form.ILabel;
import edu.unc.cs.htmlBuilder.form.IOption;
import edu.unc.cs.htmlBuilder.form.ISelect;
import edu.unc.cs.htmlBuilder.form.Label;
import edu.unc.cs.htmlBuilder.form.Option;
import edu.unc.cs.htmlBuilder.form.Select;
import edu.unc.cs.htmlBuilder.form.input.FileField;
import edu.unc.cs.htmlBuilder.form.input.IFileField;
import edu.unc.cs.htmlBuilder.form.input.ISubmitButton;
import edu.unc.cs.htmlBuilder.form.input.SubmitButton;
import edu.unc.cs.htmlBuilder.global.INoScript;
import edu.unc.cs.htmlBuilder.global.IScript;
import edu.unc.cs.htmlBuilder.global.NoScript;
import edu.unc.cs.htmlBuilder.global.Script;
import edu.unc.cs.htmlBuilder.head.Head;
import edu.unc.cs.htmlBuilder.head.ILink;
import edu.unc.cs.htmlBuilder.head.IMetaAttr;
import edu.unc.cs.htmlBuilder.head.ITitle;
import edu.unc.cs.htmlBuilder.head.Link;
import edu.unc.cs.htmlBuilder.head.MetaAttr;
import edu.unc.cs.htmlBuilder.head.Title;
import edu.unc.cs.htmlBuilder.util.JavaScriptGenerator;
import edu.unc.cs.htmlBuilder.util.Offsetter;
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
import edu.unc.cs.graderServer.webHandler.pages.parts.StudentDataNavBar;
import edu.unc.cs.graderServer.webHandler.sql.DatabaseReader;
import edu.unc.cs.graderServer.webHandler.sql.IDatabaseReader;
import edu.unc.cs.graderServer.utils.ConfigReader;
import edu.unc.cs.graderServer.utils.IConfigReader;

public class UploadPage extends HTMLFile implements IUploadPage {

    private static final Logger LOG = Logger.getLogger(UploadPage.class.getName());

    private static final String authKey;

    static {
        StringBuilder auth = new StringBuilder(50);
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
        } catch (IOException ex) {
            Logger.getLogger(UploadPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        authKey = auth.toString();
    }
    private String auth = "";
    private boolean isAdmin;
    private String user = "";

    @Override
    public void setArgs(String args) {
        //System.out.println(args);
        String[] temp = args.split("\\s");
        args = temp[temp.length - 1];
        args = args.trim();
        //System.out.println("Args: " + args);
        String[] argList = args.split("&");
        //Arrays.stream(argList).forEach((str) -> System.out.println(str.length() + ": " + str));
        for (String arg : argList) {
            if (arg.startsWith("user=")) {
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
    public void setAuth(String auth) {
        this.auth = auth.replace("+", " ");
    }

    @Override
    public String getHTML() {
        //System.out.println(user);
        //System.out.println(auth);
        //System.out.println(authKey);
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
    public void setUser(String user) {
        this.user = user.replace("+", " ");
        IDatabaseReader dr = new DatabaseReader();
        try {
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
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
        } finally {
            try {
                dr.disconnect();
            } catch (SQLException ex) {
                Logger.getLogger(UploadPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        LOG.log(Level.INFO, "Authenticated as user {0}", user);
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

        INoScript noScriptNoteHolder = new NoScript();
        IParagraph noScriptNote = new Paragraph();
        noScriptNote.setClassName("center");
        noScriptNote.addContent(new Text("The upload form relies on JavaScript to display the correct submission options. Either enable JavaScript for this page or use the Eclipe plugin to submit.", TextStyle.ITALIC));
        noScriptNoteHolder.addElement(content);
        bodyWrapper.addContent(content);
        body.addElement(bodyWrapper);

        body.addElement(getUpdateScript());

        body.setOnload(() -> "document.upload_form.upload.disabled = false;");

        setBody(body);
    }

    private ISelect buildDropDown(ResultSet results, String id, String key, String defaultVal, boolean required, String placeholder) throws SQLException {
        ISelect select = new Select();
        select.setRequired(required);
        select.setName(id);
        select.setID(id);

        if (required) {
            IOption option = new Option();
            option.setText(placeholder);
            option.setValue("");
            select.addOption(option);
        }

        boolean selected = false;

        while (results.next()) {
            String result = results.getString(key);
            IOption option = new Option();
            option.setText(result);
            option.setValue(result);
            if (!selected && result.equals(defaultVal)) {
                //System.out.println("Default selection: " + result);
                option.setSelected(true);
                selected = true;
            }
            select.addOption(option);
        }
        results.close();
        //System.out.println("**" + id);
        return select;
    }

    private ISelect buildDropDown(String[] options, String id, String defaultVal, boolean required, String placeholder) throws SQLException {
        ISelect select = new Select();
        select.setRequired(required);
        select.setName(id);
        select.setID(id);

        if (required) {
            IOption option = new Option();
            option.setText(placeholder);
            option.setValue("");
            select.addOption(option);
        }

        boolean selected = false;

        for (String optionValue : options) {
            IOption option = new Option();
            option.setText(optionValue);
            option.setValue(optionValue);
            if (!selected && optionValue.equals(defaultVal)) {
                //System.out.println("Default selection: " + optionValue);
                option.setSelected(true);
                selected = true;
            }
            select.addOption(option);
        }
        //System.out.println("**" + id);
        return select;
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

    private void buildParts() throws FileNotFoundException, IOException {
        buildHead();
        buildBody();
    }

    private IForm buildSubmitForm() throws FileNotFoundException, IOException {
        IDatabaseReader dr = new DatabaseReader();
        IForm form = new Form();
        ResultSet results = null;
        try {
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            dr.connect(config.getString("database.username").orElseThrow(IllegalArgumentException::new),
                    config.getString("database.password").orElseThrow(IllegalArgumentException::new),
                    "jdbc:" + config.getString("database.url").orElseThrow(IllegalArgumentException::new));

            form.setMethod("post");
            form.setEncoding("multipart/form-data");
            form.setAction("grading.php");
            form.setID("upload_form");
            form.setName("upload_form");
            form.setClassName("center");

            IFieldSet submissionDataSet = new FieldSet();
            submissionDataSet.setForm("upload_form");
            submissionDataSet.setLegend("Submission Data");
            submissionDataSet.setName("submission_data_set");
            submissionDataSet.setClassName("center");
            ISelect onyenSelect;

            if (isAdmin) {
                onyenSelect = buildDropDown(dr.getUsers(), "onyen", "onyen", "", true, "Choose an onyen.");
            } else {
                onyenSelect = buildDropDown(new String[]{user}, "onyen", user, true, "");
            }

            onyenSelect.setForm("upload_form");

            ILabel onyenLabel = new Label();
            onyenLabel.setLabel(new Text("Onyen"));
            onyenLabel.setElementID("onyen");
            onyenLabel.setForm("upload_form");

            submissionDataSet.addField(onyenLabel);
            submissionDataSet.addField(onyenSelect);
            submissionDataSet.addField(new LineBreak());

            ISelect courseSelect = buildDropDown(getCourseAndSectionList(), "course", "", true, "Choose a course.");
            courseSelect.setOnchange(() -> "updateAssignments()");
            courseSelect.setForm("upload_form");

            ILabel courseLabel = new Label();
            courseLabel.setLabel(new Text("Course"));
            courseLabel.setElementID("course");
            courseLabel.setForm("upload_form");

            submissionDataSet.addField(courseLabel);
            submissionDataSet.addField(courseSelect);
            submissionDataSet.addField(new LineBreak());

            ISelect typeSelect = buildDropDown(dr.getTypes(), "type", "name", "", true, "Choose an assignment type.");
            typeSelect.setOnchange(() -> "updateAssignments()");
            typeSelect.setForm("upload_form");

            ILabel typeLabel = new Label();
            typeLabel.setLabel(new Text("Type"));
            typeLabel.setElementID("type");
            typeLabel.setForm("upload_form");

            submissionDataSet.addField(typeLabel);
            submissionDataSet.addField(typeSelect);
            submissionDataSet.addField(new LineBreak());

            ISelect assignmentSelect = buildDropDown(new String[]{}, "assignment", "", true, "Choose an assignment.");
            assignmentSelect.setForm("upload_form");

            ILabel assignmentLabel = new Label();
            assignmentLabel.setLabel(new Text("Assignment"));
            assignmentLabel.setElementID("assignment");
            assignmentLabel.setForm("upload_form");

            submissionDataSet.addField(assignmentLabel);
            submissionDataSet.addField(assignmentSelect);
            submissionDataSet.addField(new LineBreak());

            form.addElement(submissionDataSet);

            IFileField file = new FileField();
            file.setForm("upload_form");
            file.setName("file");
            file.setID("file_select");
            file.setRequired(true);

            ILabel fileLabel = new Label();
            fileLabel.setLabel(new Text("File"));
            fileLabel.setForm("upload_form");
            fileLabel.setElementID("file_select");
            fileLabel.setForm("upload_form");

            form.addElement(new LineBreak());

            ISubmitButton upload = new SubmitButton();
            upload.setValue("Upload");
            upload.setForm("upload_form");
            upload.setName("upload");
            upload.setDisabled(true);

            form.addElement(fileLabel);
            form.addElement(file);
            form.addElement(new LineBreak());
            form.addElement(upload);
        } catch (SQLException e) {
            e.printStackTrace();
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

    private String getAssignmentTable() {
        StringBuilder assignments = new StringBuilder(500);
        assignments.append("[");
        ResultSet results = null;
        try (IDatabaseReader dr = new DatabaseReader()) {
            IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
            dr.connect(config.getString("database.username").orElseThrow(IllegalArgumentException::new),
                    config.getString("database.password").orElseThrow(IllegalArgumentException::new),
                    "jdbc:" + config.getString("database.url").orElseThrow(IllegalArgumentException::new));
            ArrayList<String> typesList = new ArrayList<>(3);
            results = dr.getTypes();
            while (results.next()) {
                typesList.add(results.getString("name"));
            }
            results.close();
            String[] types = typesList.toArray(new String[typesList.size()]);
            String[] coursesAndSections = getCourseAndSectionList();

            for (String courseAndSection : coursesAndSections) {
                String[] parts = courseAndSection.split("-", 2);
                String course = parts[0];
                String section = parts[1];
                assignments.append("[");
                for (String type : types) {
                    assignments.append("[");
                    results = dr.getCurrentAssignments(type, course, section);
                    String firstCapType = type.toLowerCase();
                    firstCapType = firstCapType.substring(0, 1).toUpperCase().concat(firstCapType.substring(1));
                    while (results.next()) {
                        assignments.append("\"").append(results.getString("name")).append(" (").append(firstCapType).append(" ").append(results.getInt("number")).append(")\", ");
                    }
                    if (assignments.charAt(assignments.length() - 1) != '[') {
                        assignments.delete(assignments.length() - 2, Integer.MAX_VALUE);
                    }
                    assignments.append("], ");
                }
                if (assignments.charAt(assignments.length() - 1) != '[') {
                    assignments.delete(assignments.length() - 2, Integer.MAX_VALUE);
                }
                assignments.append("], ");
            }
            if (assignments.charAt(assignments.length() - 1) != '[') {
                assignments.delete(assignments.length() - 2, Integer.MAX_VALUE);
            }
        } catch (Exception e) {
            Logger.getLogger(UploadPage.class.getName()).log(Level.SEVERE, null, e);
            return "[]";
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (SQLException ex) {
                    Logger.getLogger(UploadPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        assignments.append("]");
        return assignments.toString();
    }

    private String[] getCourseAndSectionList() throws FileNotFoundException, IOException {
        IDatabaseReader dr = new DatabaseReader();
        ResultSet results = null;
        IConfigReader config = new ConfigReader(Paths.get("config", "config.properties").toString());
        try {
            dr.connect(config.getString("database.username").orElseThrow(IllegalArgumentException::new),
                    config.getString("database.password").orElseThrow(IllegalArgumentException::new),
                    "jdbc:" + config.getString("database.url").orElseThrow(IllegalArgumentException::new));
            results = dr.getCurrentCourses();
            ArrayList<String> courses = new ArrayList<>(5);
            while (results.next()) {
                courses.add(results.getString("name"));
            }
            results.close();
            String[] courseNameArr = courses.toArray(new String[courses.size()]);
            courses.clear();
            for (String name : courseNameArr) {
                results = dr.getCurrentSections(name);
                while (results.next()) {
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

    private IScript getUpdateScript() {
        IScript script = new Script();
        JavaScriptGenerator scriptGen = () -> {
            StringBuilder text = new StringBuilder(200);
            text.append("var assignmentList = ").append(getAssignmentTable()).append(";\n")
                    .append("var assignments = document.upload_form.assignment;\n")
                    .append("var courses = document.upload_form.course;\n")
                    .append("var types = document.upload_form.type;\n\n");
            text.append("function updateAssignments() {\n")
                    .append(Offsetter.indent(1)).append("\"use strict\";\n")
                    .append(Offsetter.indent(1)).append("var i, assignmentName, defaultOption, courseSelection, typeSelection;\n\n")
                    .append(Offsetter.indent(1)).append("typeSelection = types.selectedIndex - 1;\n")
                    .append(Offsetter.indent(1)).append("courseSelection = courses.selectedIndex - 1;\n")
                    .append(Offsetter.indent(1)).append("if (assignments.options.length === 1 && (typeSelection < 0 || courseSelection < 0)) {\n")
                    .append(Offsetter.indent(2)).append("return;\n")
                    .append(Offsetter.indent(1)).append("}\n")
                    .append(Offsetter.indent(1)).append("defaultOption = assignments.options[0];\n")
                    .append(Offsetter.indent(1)).append("assignments.options.length = 0;\n")
                    .append(Offsetter.indent(1)).append("assignments.options[0] = defaultOption;\n")
                    .append(Offsetter.indent(1)).append("if (typeSelection >= 0 && courseSelection >= 0) {\n")
                    .append(Offsetter.indent(2)).append("for (i = 0; i < assignmentList[courseSelection][typeSelection].length; i += 1) {\n")
                    .append(Offsetter.indent(3)).append("assignmentName = assignmentList[courseSelection][typeSelection][i];\n")
                    .append(Offsetter.indent(3)).append("assignments.options[assignments.options.length] = new Option(assignmentName, assignmentName);\n")
                    .append(Offsetter.indent(2)).append("}\n")
                    .append(Offsetter.indent(1)).append("}\n")
                    .append("}\n");
            return text.toString();
        };

        script.setScript(scriptGen);

        return script;
    }
}
