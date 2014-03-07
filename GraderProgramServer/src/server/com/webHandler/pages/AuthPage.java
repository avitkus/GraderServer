package server.com.webHandler.pages;

import java.io.IOException;

import server.htmlBuilder.HTMLFile;
import server.htmlBuilder.body.Body;
import server.htmlBuilder.body.Division;
import server.htmlBuilder.body.Header;
import server.htmlBuilder.body.HorizontalRule;
import server.htmlBuilder.body.IBody;
import server.htmlBuilder.body.IDivision;
import server.htmlBuilder.body.IText;
import server.htmlBuilder.body.LineBreak;
import server.htmlBuilder.body.Text;
import server.htmlBuilder.doctype.HTML5Doctype;
import server.htmlBuilder.form.Form;
import server.htmlBuilder.form.IForm;
import server.htmlBuilder.form.IPasswordField;
import server.htmlBuilder.form.ISubmitButton;
import server.htmlBuilder.form.ITextField;
import server.htmlBuilder.form.PasswordField;
import server.htmlBuilder.form.SubmitButton;
import server.htmlBuilder.form.TextField;
import server.htmlBuilder.head.Head;
import server.htmlBuilder.head.ILink;
import server.htmlBuilder.head.IMetaAttr;
import server.htmlBuilder.head.ITitle;
import server.htmlBuilder.head.Link;
import server.htmlBuilder.head.MetaAttr;
import server.htmlBuilder.head.Title;
import server.htmlBuilder.table.ITable;
import server.htmlBuilder.table.ITableData;
import server.htmlBuilder.table.ITableRow;
import server.htmlBuilder.table.Table;
import server.htmlBuilder.table.TableData;
import server.htmlBuilder.table.TableRow;
import server.htmlBuilder.util.IColors;
import server.utils.OnyenAuthenticator;

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
		args = args.substring(args.indexOf("\r\n\r\n")+4);
		args.trim();
		System.out.println("Args: " + args);
		String[] argList = args.split("&");
		for(String arg : argList) {
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
		
		if(failed) {
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
