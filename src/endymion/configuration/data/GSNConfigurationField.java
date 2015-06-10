package endymion.configuration.data;

import java.util.HashMap;

/**
 * Created by Nikola on 28.02.2015.
 */
public class GSNConfigurationField {

    protected HashMap<String, String> parameters;

    public GSNConfigurationField (String name) {
        parameters = new HashMap<String, String>();
        parameters.put("name" , name);
    }

    public void addParameter (String key, String value) {
        parameters.put(key, value);
    }

    public String getParameter (String key) {
        return parameters.get(key);
    }


}
