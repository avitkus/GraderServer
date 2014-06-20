package server.htmlBuilder.util;

import java.util.Map;
import java.util.Set;

/**
 * @author Andrew Vitkus
 *
 */
public interface IEventManager {
	public void addEvent(String trigger, ScriptGenerator value);
	
	public void removeEvent(String trigger);
	
	public boolean hasEvent(String trigger);
	
	public ScriptGenerator getEvent(String trigger);
	
	public Set<Map.Entry<String,ScriptGenerator>> getEvents();
	
	public String getHTML();
}
