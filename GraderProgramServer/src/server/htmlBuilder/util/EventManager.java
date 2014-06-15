package server.htmlBuilder.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Andrew Vitkus
 *
 */
public class EventManager implements IEventManager {

    private final HashMap<String, JavaScriptGenerator> events;

    public EventManager() {
        events = new HashMap<>(1);
    }

    @Override
    public void addEvent(String trigger, JavaScriptGenerator script) {
        events.put(trigger, script);
    }

    @Override
    public void removeEvent(String trigger) {
        events.remove(trigger);
    }

    @Override
    public boolean hasEvent(String trigger) {
        return events.containsKey(trigger);
    }

    @Override
    public JavaScriptGenerator getEvent(String trigger) {
        return events.get(trigger);
    }

    @Override
    public Set<Map.Entry<String, JavaScriptGenerator>> getEvents() {
        return Collections.unmodifiableSet(events.entrySet());
    }

    @Override
    public String getHTML() {
        if (events.isEmpty()) {
            return "";
        }

        StringBuilder html = new StringBuilder(20);
        
        events.forEach((trigger, script) -> html.append(" ").append(trigger).append("=\"").append(script.getText()).append("\""));
        
        return html.toString();
    }

}
