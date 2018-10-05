package dev.peppe.monitoringiotdevices.utils;

import java.io.Serializable;

public class Subscription implements Serializable {
    public String device;
    public String topic;
    public String topicSubscription;
    public int qosSubscription;

    public Subscription(String device,String topic,int qos){
        this.device = device;
        this.topic = topic;
        this.qosSubscription = qos;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopicPath() {
        return topicSubscription;
    }

    public int getQos() {
        return qosSubscription;
    }

    public void setTopicPath() {
        if(!this.device.isEmpty())
            this.topicSubscription = device+"/"+topic;
        else
            this.topicSubscription = "+/"+topic;
    }

    public void setQos(int qos) {
        this.qosSubscription = qos;
    }
}

