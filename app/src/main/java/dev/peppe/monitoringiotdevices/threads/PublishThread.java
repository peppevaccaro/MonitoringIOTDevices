package dev.peppe.monitoringiotdevices.threads;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.UnsupportedEncodingException;

import dev.peppe.monitoringiotdevices.helpers.MQTTHelper;
import dev.peppe.monitoringiotdevices.utils.Topic;

public class PublishThread extends Thread {
    private MQTTHelper mqttHelper;
    private SensorManager sensorManager;
    private Topic topic;
    private Sensor sensorManaged;
    private String currentValue;

    private SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;
            if (sensor.getType() == sensorManaged.getType())
                currentValue = String.valueOf(event.values[0]);
        }
    };
    volatile boolean running = true;

    public PublishThread(MQTTHelper mqttHelper,Topic topic,SensorManager manager){
        this.mqttHelper = mqttHelper;
        this.topic = topic;
        this.sensorManager = manager;
    }

    public void run() {
        sensorManaged = getSensor();
        while (mqttHelper != null && mqttHelper.mqttAndroidClient.isConnected()&& running) {
            try {
                if(currentValue!=null)
                    mqttHelper.publishMessage(currentValue, topic.qos, topic.topicPath, topic.retain);
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel(){
        running = false;
    }

    public Sensor getSensor(){
        switch(topic.getTopic()){
            case "Light": {
                sensorManager.registerListener(listener,sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_UI);
                return sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            }
            default:
                return null;
        }
    }

    public boolean sensorExists(String topic){
        switch (topic){
            case "Light": {
                return true;
            }
            default:
                return false;
            }
        }
}
