package endymion.configuration.processor;

import endymion.configuration.data.GSNConfiguration;
import endymion.exception.EndymionException;

import java.util.List;

/**
 * Created by Nikola on 24.02.2015.
 */
public abstract class GSNConfigurationDataProcessor {

    public abstract List<GSNConfiguration> processConfiguration (List<String> configuration) throws EndymionException;
}
