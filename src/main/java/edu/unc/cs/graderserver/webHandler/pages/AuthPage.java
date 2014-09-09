package edu.unc.cs.graderServer.webHandler.pages;

import java.io.IOException;

import edu.unc.cs.htmlBuilder.HTMLFile;
import edu.unc.cs.htmlBuilder.body.Body;
import edu.unc.cs.htmlBuilder.body.Division;
import edu.unc.cs.htmlBuilder.body.Header;
import edu.unc.cs.htmlBuilder.body.HorizontalRule;
import edu.unc.cs.htmlBuilder.body.IBody;
import edu.unc.cs.htmlBuilder.body.IDivision;
import edu.unc.cs.htmlBuilder.body.IText;
import edu.unc.cs.htmlBuilder.body.LineBreak;
import edu.unc.cs.htmlBuilder.body.Text;
import edu.unc.cs.htmlBuilder.doctype.HTML5Doctype;
import edu.unc.cs.htmlBuilder.form.Form;
import edu.unc.cs.htmlBuilder.form.IForm;
import edu.unc.cs.htmlBuilder.form.input.IPasswordField;
import edu.unc.cs.htmlBuilder.form.input.ISubmitButton;
import edu.unc.cs.htmlBuilder.form.input.ITextField;
import edu.unc.cs.htmlBuilder.form.input.PasswordField;
import edu.unc.cs.htmlBuilder.form.input.SubmitButton;
import edu.unc.cs.htmlBuilder.form.input.TextField;
import edu.unc.cs.htmlBuilder.head.Head;
import edu.unc.cs.htmlBuilder.head.ILink;
import edu.unc.cs.htmlBuilder.head.IMetaAttr;
import edu.unc.cs.htmlBuilder.head.ITitle;
import edu.unc.cs.htmlBuilder.head.Link;
import edu.unc.cs.htmlBuilder.head.MetaAttr;
import edu.unc.cs.htmlBuilder.head.Title;
import edu.unc.cs.htmlBuilder.table.ITable;
import edu.unc.cs.htmlBuilder.table.ITableData;
import edu.unc.cs.htmlBuilder.table.ITableRow;
import edu.unc.cs.htmlBuilder.table.Table;
import edu.unc.cs.htmlBuilder.table.TableData;
import edu.unc.cs.htmlBuilder.table.TableRow;
import edu.unc.cs.htmlBuilder.util.IColors;
import edu.unc.cs.graderServer.utils.OnyenAuthenticator;

/**
 * @author Andrew Vitkus
 *
 */
public class AuthPage extends HTMLFile implements IAuthPage {

    private String onyen = null;
    private String password = null;
    private boolean failed = false;

    @Override
    public void setArgs(String args) {
        args = args.substring(args.indexOf("\r\n\r\n") + 4);
        args.trim();
        System.out.println("Args: " + args);
        String[] argList = args.split("&");
        for (String arg : argList) {
            if (arg.startsWith("onyen")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setOnyen(argSplit[1]);
                }
            } else if (arg.startsWith("pwd")) {
                String[] argSplit = arg.split("=");
                if (argSplit.length > 1) {
                    setPassword(arg.split("=")[1]);
                }
            }
        }
    }

    private void setOnyen(String onyen) {
        this.onyen = onyen;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    @Override
    public String getHTML() {
        setDoctype(new HTML5Doctype());
        buildParts();
        return super.getHTML();
    }

    private void buildParts() {
        buildHead();
        buildBody();
    }

    private void buildHead() {
        ITitle title = new Title("ONYEN Login");

        IMetaAttr charset = new MetaAttr();
        charset.addAttribute("charset", "UTF-8");

        ILink faviconLink = new Link();
        faviconLink.setRelation("shortcut icon");
        faviconLink.addLinkAttribtue("href", "favicon.ico");
        faviconLink.addLinkAttribtue("type", "image/vnd.microsoft.icon");

        setHead(new Head(title, charset, faviconLink));
        addCSSFile("auth.css");
    }

    private void buildBody() {
        IBody body = new Body();

        body.addElement(new Header("ONYEN Login", 1));
        body.addElement(new HorizontalRule());

        if (failed) {
            IText failText = new Text("Invalid login information");
            failText.setColor(IColors.RED);
            IDivision failDiv = new Division();
            failDiv.addContent(failText);
            failDiv.setClass("fail-notice");
            body.addElement(failText);
            body.addElement(new LineBreak());
        }

        IForm authForm = new Form();
        //authForm.setAction("https://onyen.unc.edu/cgi-bin/unc_id/authenticator.pl");
        authForm.setMethod("POST");
        authForm.setName("auth_form");

        ITextField onyenField = new TextField();
        onyenField.setName("onyen");
        onyenField.setRequired(true);

        IPasswordField passwordField = new PasswordField();
        passwordField.setName("pwd");
        passwordField.setRequired(true);

        ITable authTable = new Table();
        authTable.addAttribute("class", "auth-table");
        ITableRow onyenRow = new TableRow();
        ITableData onyenLabelData = new TableData(new Text("Onyen"));
        onyenLabelData.addAttribute("class", "form-label");
        ITableData onyenFieldData = new TableData(onyenField);
        onyenFieldData.addAttribute("class", "form-field");
        onyenRow.addDataPart(onyenLabelData);
        onyenRow.addDataPart(onyenFieldData);

        ITableRow passwordRow = new TableRow();
        ITableData passwordLabelData = new TableData(new Text("Password"));
        passwordLabelData.addAttribute("class", "form-label");
        ITableData passwordFieldData = new TableData(passwordField);
        passwordFieldData.addAttribute("class", "form-field");
        passwordRow.addDataPart(passwordLabelData);
        passwordRow.addDataPart(passwordFieldData);

        authTable.addRow(onyenRow);
        authTable.addRow(passwordRow);

        authForm.addElement(authTable);

        authForm.addElement(new LineBreak());

        ISubmitButton submitButton = new SubmitButton();
        submitButton.setName("submit");
        submitButton.setValue("Continue");
        authForm.addElement(submitButton);

        authForm.addAttribute("class", "center");

        body.addElement(authForm);

        setBody(body);
    }

    @Override
    public String checkAuth() {
        if (onyen == null || password == null) {
            return null;
        }
        String vfykey = OnyenAuthenticator.authenticate(onyen, password);
        String authStatus = "";
        try {
            authStatus = OnyenAuthenticator.checkVfyKey(vfykey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authStatus;
    }
}
