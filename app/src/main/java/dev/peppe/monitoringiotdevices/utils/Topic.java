package dev.peppe.monitoringiotdevices.utils;

public class Topic {
    public String topicPath;
    public int qos;
    public boolean retain;
    public String payload;

    public Topic(String topic,int qos,boolean retain){
        this.topicPath = topic;
        this.qos = qos;
        this.retain = retain;
    }

    public String getTopicPath() {
        return topicPath;
    }

    public void setTopicPath(String topic) {
        this.topicPath = topic;
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
