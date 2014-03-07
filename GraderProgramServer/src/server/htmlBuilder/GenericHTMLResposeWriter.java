package server.htmlBuilder;

/**
 * @author Andrew Vitkus
 *
 */
public class GenericHTMLResposeWriter {
	private String DOCTYPE = "html";
	private String CHARSET = "UTF-8";
	private String TITLE = "";
	
	private String buildHead() {
		String head = "<head>\n";
		head += "<meta charset=\"" + CHARSET + "\">\n";
		head += buildTitle() + "\n";
		head += "</head>";
		return head;
	}
	
	private String buildTitle() {
		return "<title>" + TITLE.toUpperCase() + "</title>";
	}
	
	private String buildBody() {
		String body = "<body>\n";
		body += "<h1>I have no idea...</h1>\n";
		body += "<p>\n";
		body += "...what to put here.\n";
		body += "</p>\n";
		body += "<h2>" + TITLE + " recieved!</h2>\n";
		body += "</body>";
		return body;
	}
	
	public String buildFile() {
		String file = "<!DOCTYPE " + DOCTYPE + ">\n";
		file += "<html>\n";
		file += buildHead() + "\n";
		file += buildBody() + "\n";
		file += "</html>";
		return file;
	}
	
	public void setTitle(String title) {
		TITLE = title;
	}
}
