package endymion.configuration.data;

import endymion.exception.EndymionException;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Nikola on 23.02.2015.
 * Abstract element that contains GSN configuration.
 */
public abstract class GSNConfiguration {

    /**
     * Unique identifier of GSN
     */
    protected String GSNId;

    /**
     * IP address of the GSN
     */
    protected String GSNIpAddress;

    /**
     * Port of the GSN (usually 22001 or 1443 for SSL)
     */
    protected String port;

    /**
     * Authorization - username
     */
    protected String username;

    /**
     * Authorization - password
     */
    protected String password;

    /**
     * Constructor
     * @param ipAddress
     * @param port
     * @param username
     * @param password
     */
    public GSNConfiguration (String ipAddress, String port, String username, String password) {
        this.GSNIpAddress = ipAddress;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getGSNIpAddress() {
        return GSNIpAddress;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getID () {
        return GSNId;
    }

    public void setGSNIpAddress(String GSNIpAddress) {
        this.GSNIpAddress = GSNIpAddress;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method used for fetching configuration parameter
     * @param keys - identifier of the parameter
     * @return - parameter value
     * @throws EndymionException - wrong keys or non-existent parameter
     */
    public abstract String getConfigurationParameter (String... keys) throws EndymionException;

    /**
     * Method used for setting configuration parameters
     * @param keys - identifier and value of parameter
     * @throws EndymionException - wrong keys
     */
    public abstract void setConfigurationParameter (String... keys) throws EndymionException;

    public abstract List <String> getVSensors ();

    public abstract List <String> getAlarms ();

    public abstract List <String> getSensorFields (String sensor) throws EndymionException;

    public abstract List <String> getAlarms (String sensor) throws EndymionException;

    public abstract void addVSensor (String vSensor, HashMap <String, String> parameters) throws EndymionException;

    public abstract void addField (String vSensor, HashMap<String, String> parameters) throws EndymionException;

    public abstract void addAlarm (String vSensor, String alarmName, HashMap<String, String> parameters, List<String> sendToList)
            throws EndymionException;

    public abstract void addAlarm (String alarmName, HashMap<String, String> parameters, List<String> sendToList)
            throws EndymionException;
}
