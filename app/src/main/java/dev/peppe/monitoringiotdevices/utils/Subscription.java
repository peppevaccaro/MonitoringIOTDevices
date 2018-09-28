package dev.peppe.monitoringiotdevices.utils;

public class Subscription {
    public String topic;
    public int qos;

    public Subscription(String topic,int qos){
        this.topic = topic;
        this.qos = qos;
    }

    public String getTopic() {
        return topic;
    }

    public int getQos() {
        return qos;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }
}

