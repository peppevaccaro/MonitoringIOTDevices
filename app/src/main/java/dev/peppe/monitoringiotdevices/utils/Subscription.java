package dev.peppe.monitoringiotdevices.utils;

public class Subscription {
    public String topicSubscription;
    public int qosSubscription;

    public Subscription(String topic,int qos){
        this.topicSubscription = topic;
        this.qosSubscription = qos;
    }

    public String getTopic() {
        return topicSubscription;
    }

    public int getQos() {
        return qosSubscription;
    }

    public void setTopic(String topic) {
        this.topicSubscription = topic;
    }

    public void setQos(int qos) {
        this.qosSubscription = qos;
    }
}

