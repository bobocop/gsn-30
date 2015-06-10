package endymion.configuration.data;

import endymion.exception.EndymionException;
import endymion.logger.EndymionLoggerEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nikola on 28.02.2015.
 */
public class GSNConfigurationVSensor {

    protected String name;
    HashMap<String, String> parameters;
    List <GSNConfigurationField> fields;
    List <GSNConfigurationAlarm> alarms;

    private final int VSENSOR_PARAM_NUMBER = 2;
    private final int FIELD_PARAM_NUMBER_NEW = 2;
    private final int FIELD_PARAM_NUMBER = 4;
    private final int ALARM_PARAM_NUMBER_NEW = FIELD_PARAM_NUMBER_NEW;
    private final int ALARM_PARAM_NUMBER = FIELD_PARAM_NUMBER;
    private final String FIELD_ID = "field";
    private final String ALARM_ID = "alarm";

    private final int VSENSOR_READ_PARAM_NUMBER = 1;
    private final int FIELD_READ_PARAM_NUMBER = 3;
    private final int ALARM_READ_PARAM_NUMBER = FIELD_READ_PARAM_NUMBER;

    public GSNConfigurationVSensor (String name) {
        this.name = name;
        parameters = new HashMap<String, String>();
        fields = new ArrayList<GSNConfigurationField>();
        alarms = new ArrayList<GSNConfigurationAlarm>();
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getParameter (String... keys) throws EndymionException {
        if (keys.length == VSENSOR_READ_PARAM_NUMBER) {
            return parameters.get(keys[0]);
        } else if (keys.length == FIELD_READ_PARAM_NUMBER && keys[0].equalsIgnoreCase(FIELD_ID)){
            GSNConfigurationField field = getFieldByName(keys[1]);
            return field.getParameter(keys[2]);
        } else if (keys.length == ALARM_READ_PARAM_NUMBER && keys[0].equalsIgnoreCase(ALARM_ID)){
            GSNConfigurationAlarm alarm = getAlarmByName(keys[1]);
            return alarm.getParameter(keys[2]);
        } else {
            throw new EndymionException("Cannot get parameter with key: " + Arrays.toString(keys), EndymionLoggerEnum.WARNING);
        }
    }

    public void setParameters (String... params) throws EndymionException {

        if (params.length == VSENSOR_PARAM_NUMBER && !params[0].equalsIgnoreCase(FIELD_ID)
                && !params[0].equalsIgnoreCase(ALARM_ID)) {
            parameters.put(params[0], params[1]);
        } else if (params.length == FIELD_PARAM_NUMBER_NEW && params[0].equalsIgnoreCase(FIELD_ID)) {
            if (!getAllFields().contains(params[1]))
                fields.add(new GSNConfigurationField(params[1]));
            else
                throw new EndymionException("Field with name " + params[1] + " already exists in "
                        + getName() + " sensor", EndymionLoggerEnum.WARNING);
        } else if (params.length == ALARM_PARAM_NUMBER_NEW && params[0].equalsIgnoreCase(ALARM_ID)) {
            if (!getAllAlarms().contains(params[1])) {
                alarms.add(new GSNConfigurationAlarm(params[1]));
            } else {
                throw new EndymionException("Alarm with name " + params[1] + " already exists in "
                        + getName() + " sensor", EndymionLoggerEnum.WARNING);
            }
        } else if (params.length == FIELD_PARAM_NUMBER && params[0].equalsIgnoreCase(FIELD_ID)) {
            GSNConfigurationField field = getFieldByName(params[1]);
            field.addParameter(params[2], params[3]);
        } else if (params.length == ALARM_PARAM_NUMBER && params[0].equalsIgnoreCase(ALARM_ID)) {
            GSNConfigurationAlarm alarm = getAlarmByName(params[1]);
            alarm.addParameter(params[2], params[3]);
        } else {
            throw new EndymionException("Cannot set parameter: " + Arrays.toString(params), EndymionLoggerEnum.WARNING);
        }
    }

    protected GSNConfigurationField getFieldByName (String name) throws EndymionException {

        for (GSNConfigurationField field : fields) {
            if (field.getParameter("name").equalsIgnoreCase(name)) {
                return field;
            }
        }

        throw new EndymionException("No field with name: " + name, EndymionLoggerEnum.WARNING);
    }

    protected GSNConfigurationAlarm getAlarmByName (String name) throws EndymionException {

        for (GSNConfigurationAlarm alarm : alarms) {
            if (alarm.getName().equalsIgnoreCase(name)) {
                return alarm;
            }
        }

        throw new EndymionException("No alarm with name " + name, EndymionLoggerEnum.WARNING);
    }

    public List <String> getAllFields () {
        List<String> fieldsS = new ArrayList<String>();

        for (GSNConfigurationField field : fields) {
            fieldsS.add(field.getParameter("name"));
        }

        return fieldsS;
    }

    public List <String> getAllAlarms () {
        List<String> alarmsS = new ArrayList<String>();

        for (GSNConfigurationAlarm alarm : alarms) {
            alarmsS.add(alarm.getName());
        }

        return alarmsS;
    }


}
