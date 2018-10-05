package dev.peppe.monitoringiotdevices.threads;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
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
    private Context context;
    private MQTTHelper mqttHelper;
    private Intent batteryStatus;
    private SensorManager sensorManager;
    private TelephonyManager telManager;
    private Topic topic;
    private Sensor sensorManaged;
    private String currentValue;
    private boolean sensorCPUTemperature = false;

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

    public PublishThread(Context context,MQTTHelper mqttHelper,Topic topic,SensorManager manager,TelephonyManager telManager){
        this.mqttHelper = mqttHelper;
        this.topic = topic;
        this.sensorManager = manager;
        this.telManager = telManager;
        this.context = context;
    }

    public void run() {
        manageSensor();
        while (mqttHelper != null && mqttHelper.mqttAndroidClient.isConnected()&& running) {
            try {
                if(sensorCPUTemperature)
                    currentValue = getCpuTemp();
                if(batteryStatus!=null)
                    currentValue = getBatteryPercentage();
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

    public void manageSensor(){
        switch(topic.getTopic()){
            case "Light": {
                sensorManager.registerListener(listener,sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),SensorManager.SENSOR_DELAY_UI);
                sensorManaged = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                break;
            }

            case "Signal Strength":{
                // Register the listener with the telephony manager
                telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS |
                        PhoneStateListener.LISTEN_CELL_LOCATION);
            }

            case "CPU Temperature":{
                sensorCPUTemperature = true;
            }

            case "Battery":{
                IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                batteryStatus = context.registerReceiver(null, iFilter);
            }

            default:
                break;
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

    public String getBatteryPercentage() {

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return String.valueOf(batteryPct * 100);
    }
}
