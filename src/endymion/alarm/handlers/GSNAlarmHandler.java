package endymion.alarm.handlers;

import endymion.alarm.senders.GSNAlarmSender;
import endymion.exception.EndymionException;
import endymion.time.GSNTimeManager;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Nikola on 14.04.2015.
 * This abstract class represents an alarm handler class.
 * It's responsibility is to check if the alarm should be raised (based on condition)
 * and send alarm using GSNAlarmSender
 */
public abstract class GSNAlarmHandler {

    /**
     * Time period that has to pass before the alarm is raised - from configuration
     * Format: (0-9)+(h|m)
     * Examples: 30m - 30 minutes, 12h - 12 hours
     */
    protected String timePeriod;

    /**
     * The time when the last alarm was raised.
     * It is used for continuous alarming (if the repeat is true)
     */
    protected String lastSentTimestamp;

    /**
     * The flag that tells if the alarm should be repeated - from configuration
     */
    protected boolean repeat;

    /**
     * The list of recipients of the alarm (emails)
     */
    protected List<String> sendToList;

    /**
     * Alarm sender object that is responsible for sending alarm
     */
    protected GSNAlarmSender alarmSender;

    /**
     * The name of the alarm
     */
    protected String alarmName;

    /**
     * Constructor
     * @param alarmName - the name of the alarm
     */
    public GSNAlarmHandler (String alarmName) {
        sendToList = new ArrayList<String>();
        lastSentTimestamp = null;
        this.alarmName = alarmName;
    }

    /**
     * timePeriod setter
     * @param timePeriod
     */
    public void setTimePeriod (String timePeriod) {
        this.timePeriod = timePeriod;
    }

    /**
     * repeat setter
     * @param repeat
     */
    public void setRepeat (boolean repeat) {
        this.repeat = repeat;
    }

    /**
     * send-to list adder
     * @param sendTo
     */
    public void addSendTo (String sendTo) {
        this.sendToList.add(sendTo);
    }

    /**
     * This method is used for checking if the alarm should be send
     * @return - true if the alarm should be sent, false otherwise
     */
    public abstract boolean checkTimestamp ();

    /**
     * This method uses AlarmSender object to send alarm
     * @throws EndymionException - error occurred when sending alarm
     */
    public void raiseAlarm () throws EndymionException {
        initializeSender();
        alarmSender.sendAlarm("GSN Endymion Alarm", composeMessage());
        setLastSentTimestamp(GSNTimeManager.dateFormat.format(new Date()));
    }

    /**
     * Setter for AlarmSender object
     * @param alarmSender
     */
    public void setAlarmSender (GSNAlarmSender alarmSender) {
        this.alarmSender = alarmSender;
    }

    /**
     * Initialization of the sender includes passing the recipient list
     * as a string with ';' separated elements
     * @throws EndymionException
     */
    protected void initializeSender () throws EndymionException {
        alarmSender.setSendParameters(StringUtils.join(this.sendToList.iterator(), ";"));
    }

    /**
     * This method is used for generating alarm message
     * @return - alarm message
     */
    protected abstract String composeMessage ();

    /**
     * Getter for alarm name
     * @return - alarm name
     */
    public String getAlarmName () {
        return alarmName;
    }

    /**
     * Getter for GSNId
     * @return - GSN ID
     */
    public abstract String getGSNId ();

    /**
     * Getter for GSNId
     * @return - GSN ID
     */
    public abstract String getVSensor ();

    protected void setLastSentTimestamp (String lastSentTimestamp) {
        this.lastSentTimestamp = lastSentTimestamp;
    }
}
