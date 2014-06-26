package server.htmlBuilder.util;

import java.util.Optional;

/**
 *
 * @author Andrew V0itkus
 */
@FunctionalInterface
public interface JavaScriptGenerator extends ScriptGenerator {

    @Override
    default public Optional<String> type() {
        return Optional.of("application/javascript");
    }
}
