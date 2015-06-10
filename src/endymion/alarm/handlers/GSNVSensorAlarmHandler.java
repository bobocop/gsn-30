package endymion.alarm.handlers;

import endymion.logger.timestamp.EndymionTimestampManager;
import endymion.time.GSNTimeManager;


/**
 * Created by Nikola on 14.04.2015.
 * This class extends GSNGSNAlarmHandler and is used for handling
 * alarms on vSensor level
 */
public class GSNVSensorAlarmHandler extends GSNGSNAlarmHandler {

    /**
     * vSensor name
     */
    String vSensor;

    /**
     * Constructor
     * @param GSNId
     * @param vSensor
     * @param alarmName
     */
    public GSNVSensorAlarmHandler (String GSNId, String vSensor, String alarmName) {
        super(GSNId, alarmName);
        this.vSensor = vSensor;
        lastSentTimestamp = EndymionTimestampManager.getTimestampManager().
                getTimestampAlarm(this.GSNId, this.vSensor, this.alarmName);
    }

    /**
     * This method generates an alarm message that will be send
     * @return - composed message
     */
    @Override
    protected String composeMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append("The alarm was raised in GSN " + GSNId + " for sensor " + vSensor + ".\n");
        builder.append("The cause of the alarm is expiration of the waiting time period of " + timePeriod + ".\n");

        if (getLastTimestamp() != null)
            builder.append("Last timestamp value received from GSN sensor " + vSensor + " is " + getLastTimestamp() + ".\n");
        else
            builder.append("Timestamp value was not received from" + vSensor + " since Endymion started at "
                    + GSNTimeManager.getTimeManager().getSystemStartTimestamp() + "\n");


        if (lastSentTimestamp != null) {
            builder.append("Last alarm was raised at " + lastSentTimestamp + "\n");
        }
        if (repeat) {
            builder.append("If the sensor doesn't respond in " + timePeriod + " time period, this message will be sent again.");
        } else {
            builder.append("This message will not be repeated!");
        }

        return builder.toString();
    }

    /**
     * This method overrides getLastTimestamp from GSN alarm handler.
     * @return - the last timestamp recevide from vSensor
     */
    protected String getLastTimestamp () {
        return GSNTimeManager.getTimeManager().getLastTimestamp(GSNId, vSensor);
    }

    /**
     * vSensor getter
     * @return
     */
    public String getVSensor () {
        return vSensor;
    }

    protected void setLastSentTimestamp (String lastSentTimestamp) {
        this.lastSentTimestamp = lastSentTimestamp;
        EndymionTimestampManager.getTimestampManager().setTimestampAlarm(GSNId, vSensor ,alarmName, this.lastSentTimestamp);
    }
}
