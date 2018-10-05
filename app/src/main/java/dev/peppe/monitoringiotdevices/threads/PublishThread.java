package dev.peppe.monitoringiotdevices.threads;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import dev.peppe.monitoringiotdevices.helpers.MQTTHelper;
import dev.peppe.monitoringiotdevices.utils.Topic;

public class PublishThread extends Thread {
    private MQTTHelper mqttHelper;
    private SensorManager sensorManager;
    private TelephonyManager telManager;
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

    // Listener for signal strength.
    final PhoneStateListener phoneStateListener = new PhoneStateListener()
    {
        @Override
        public void onCellLocationChanged(CellLocation mLocation)
        {
        }

        @Override
        public void onSignalStrengthsChanged(SignalStrength sStrength)
        {
            currentValue = String.valueOf(sStrength.getLevel());
        }
    };

    volatile boolean running = true;

    public PublishThread(MQTTHelper mqttHelper,Topic topic,SensorManager manager,TelephonyManager telManager){
        this.mqttHelper = mqttHelper;
        this.topic = topic;
        this.sensorManager = manager;
        this.telManager = telManager;
    }

    public void run() {
        if(sensorExists(topic.getTopic()))
            sensorManaged = getSensor();
        while (mqttHelper != null && mqttHelper.mqttAndroidClient.isConnected()&& running) {
            try {
                if(!sensorExists(topic.getTopic()))
                    manageTopic(topic.getTopic());
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

    public void manageTopic(String topic){
        switch (topic) {
            case "CPU Temperature": {
                currentValue = getCpuTemp();
                break;
            }

            case "Battery": {
                break;
            }

            case "Signal Strength":{
                // Register the listener with the telephony manager
                telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS |
                        PhoneStateListener.LISTEN_CELL_LOCATION);
            }
            default:
                break;
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

        public String getCpuTemp() {
            Process p;
            try {
                p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line = reader.readLine();
                float temp = Float.parseFloat(line) / 1000.0f;

                return String.valueOf(temp);

            } catch (Exception e) {
                e.printStackTrace();
                return String.valueOf(0.0f);
            }
        }
}
