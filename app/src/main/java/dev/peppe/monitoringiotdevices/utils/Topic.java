package dev.peppe.monitoringiotdevices.utils;

public class Topic {
    public String device;
    public String topic;
    public String topicPath;
    public int qos;
    public boolean retain;
    public String payload;

    public Topic(String device, String topic,int qos,boolean retain){
        this.device = device;
        this.topic = topic;
        this.qos = qos;
        this.retain = retain;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getTopicPath() {
        return topicPath;
    }

    public void setTopicPath() {
            this.topicPath = device+"/"+topic;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public boolean getRetain() {
        return retain;
    }

    public void setRetain(boolean retain) {
        this.retain = retain;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload){
        this.payload = payload;
    }
}
