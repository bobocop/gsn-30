package endymion.alarm.handlers;

import endymion.logger.timestamp.EndymionTimestampManager;
import endymion.time.GSNTimeManager;

import java.util.Date;

/**
 * Created by Nikola on 14.04.2015.
 * This class extends GSNAlarmHandler and it's used for alarms on
 * GSN level
 */
public class GSNGSNAlarmHandler extends GSNAlarmHandler {

    /**
     * GSN ID
     */
    protected String GSNId;

    /**
     * Timestamp that stores the last timestamp received from GSN
     */
    protected String gsnTimestamp;

    /**
     * Empty constructor
     */
    public GSNGSNAlarmHandler() {
        super(null);
    }

    /**
     * Constructor
     *
     * @param GSNId
     * @param alarmName
     */
    public GSNGSNAlarmHandler(String GSNId, String alarmName) {
        super(alarmName);
        this.GSNId = GSNId;
        lastSentTimestamp = EndymionTimestampManager.getTimestampManager().getTimestampAlarm(this.GSNId, alarmName);
    }

    /**
     * Checks the last timestamp received from GSN. If the GSN hasn't responded
     * since system start, the system start timestamp is used.
     *
     * @return - If the last timestamp received occurred before the timePeriod or the last sent timestamp
     * occurred before the time period and the timestamp hasn't been received, and the repeat is true this method
     * returns true, else false.
     */
    @Override
    public boolean checkTimestamp() {

        Date gsnDate;
        Date lastSentDate;

        gsnTimestamp = getLastTimestamp();
        if (gsnTimestamp == null) {
            gsnTimestamp = GSNTimeManager.getTimeManager().getSystemStartTimestamp();
        }

        if (lastSentTimestamp != null) {

            try {
                gsnDate = GSNTimeManager.dateFormat.parse(gsnTimestamp);
                lastSentDate = GSNTimeManager.dateFormat.parse(lastSentTimestamp);

                if (gsnDate.after(lastSentDate)) {
                    lastSentTimestamp = null;
                } else {
                    return repeat && (GSNTimeManager.getTimeManager().compareDateTime(lastSentTimestamp, timePeriod));
                }


            } catch (Exception e) {
                return false;
            }
        }

        try {
            if (GSNTimeManager.getTimeManager().compareDateTime(gsnTimestamp, timePeriod)) {
                return true;
            }
        } catch (Exception e) {

        }

        return false;
    }

    /**
     * This method generates an alarm message that will be send
     *
     * @return - composed message
     */
    @Override
    protected String composeMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("The alarm was raised in GSN " + GSNId + ".\n");
        builder.append("The cause of the alarm is expiration of the waiting time period of " + timePeriod + ".\n");

        if (getLastTimestamp() != null)
            builder.append("Last timestamp value received from GSN is " + getLastTimestamp() + ".\n");
        else
            builder.append("Timestamp value was not received from GSN since Endymion started at "
                    + GSNTimeManager.getTimeManager().getSystemStartTimestamp() + "\n");

        if (lastSentTimestamp != null) {
            builder.append("Last alarm was raised at " + lastSentTimestamp + "\n");
        }

        if (repeat) {
            builder.append("If the GSN doesn't respond in " + timePeriod + " time period, this message will be sent again.");
        } else {
            builder.append("This message will not be repeated!");
        }

        return builder.toString();
    }

    @Override
    public String getGSNId() {
        return GSNId;
    }

    @Override
    public String getVSensor() {
        return null;
    }

    /**
     * This method returns the last timestamp received from GSN
     *
     * @return - timestamp
     */
    protected String getLastTimestamp() {
        return GSNTimeManager.getTimeManager().getLastTimestamp(GSNId);
    }

    /**
     * Setter for lastSentTimestamp - timestamp is stored
     * @param lastSentTimestamp
     */
    protected void setLastSentTimestamp (String lastSentTimestamp) {
        this.lastSentTimestamp = lastSentTimestamp;
        EndymionTimestampManager.getTimestampManager().setTimestampAlarm(GSNId, alarmName, this.lastSentTimestamp);
    }
}