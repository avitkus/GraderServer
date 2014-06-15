package server.htmlBuilder.util;

import java.util.Map;
import java.util.Set;

/**
 * @author Andrew Vitkus
 *
 */
public interface IEventManager {
	public void addEvent(String trigger, JavaScriptGenerator value);
	
	public void removeEvent(String trigger);
	
	public boolean hasEvent(String trigger);
	
	public JavaScriptGenerator getEvent(String trigger);
	
	public Set<Map.Entry<String,JavaScriptGenerator>> getEvents();
	
	public String getHTML();
}
