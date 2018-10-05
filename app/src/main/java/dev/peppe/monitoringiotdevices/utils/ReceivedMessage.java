package dev.peppe.monitoringiotdevices.utils;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;
import java.util.Date;

public class ReceivedMessage implements Serializable {
    public MqttMessage message;
    public String topic;
    public Date timestamp;

    public ReceivedMessage(MqttMessage mess,String topic,Date date){
        this.message = mess;
        this.topic = topic;
        this.timestamp = date;
    }

    public MqttMessage getMessage() {
        return message;
    }

    public String getTopic() {
        return topic;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setMessage(MqttMessage message) {
        this.message = message;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
