package server.htmlBuilder.util;

import java.util.Optional;

/**
 *
 * @author Andrew V0itkus
 */
@FunctionalInterface
public interface ScriptGenerator {
    public String getText();
    
    default public Optional<String> type() {
        return Optional.empty();
    }
}
