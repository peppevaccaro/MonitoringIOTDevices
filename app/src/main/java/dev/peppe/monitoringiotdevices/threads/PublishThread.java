package dev.peppe.monitoringiotdevices.threads;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

import dev.peppe.monitoringiotdevices.helpers.MQTTHelper;
import dev.peppe.monitoringiotdevices.utils.Topic;

public class PublishThread extends Thread {
    private MQTTHelper mqttHelper;
    private Topic topic;
    volatile boolean running = true;
    public PublishThread(MQTTHelper mqttHelper,Topic topic){
        this.mqttHelper = mqttHelper;
        this.topic = topic;
    }

    public void run() {
        while (mqttHelper != null && running) {
            try {
                mqttHelper.publishMessage("Prova testo", topic.qos, topic.topicPath, topic.retain);
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel(){
        running = false;
    }
}
