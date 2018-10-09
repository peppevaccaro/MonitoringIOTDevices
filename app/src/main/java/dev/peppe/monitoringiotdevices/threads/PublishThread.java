package dev.peppe.monitoringiotdevices.threads;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import dev.peppe.monitoringiotdevices.helpers.MQTTHelper;
import dev.peppe.monitoringiotdevices.utils.Topic;

public class PublishThread extends Thread {
    private Context context;
    private MQTTHelper mqttHelper;
    private Intent batteryStatus;
    private boolean internalMemoryStatus = false;
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
            if (sensor.getType() == sensorManaged.getType() && sensorManaged.getType()== Sensor.TYPE_LIGHT)
                currentValue = String.valueOf(event.values[0]) +" lux";
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
            currentValue = String.valueOf(sStrength.getLevel())+" dB";
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
                else if(batteryStatus!=null)
                    currentValue = getBatteryPercentage();
                else if(internalMemoryStatus)
                    currentValue = getAvailableInternalMemorySize();
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
                sensorManaged = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                sensorManager.registerListener(listener,sensorManaged,SensorManager.SENSOR_DELAY_UI);
                break;
            }

            case "Signal Strength":{
                // Register the listener with the telephony manager
                telManager.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS |
                        PhoneStateListener.LISTEN_CELL_LOCATION);
                break;
            }

            case "CPU Temperature":{
                sensorCPUTemperature = true;
                break;
            }

            case "Battery":{
                IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                batteryStatus = context.registerReceiver(null, iFilter);
                break;
            }

            case "Memory":{
                internalMemoryStatus = true;
                break;
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

                return (String.valueOf(temp)+ " °C");

            } catch (Exception e) {
                e.printStackTrace();
                return (String.valueOf(0.0f)+ " °C");
            }
        }

    public String getBatteryPercentage() {

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (String.valueOf(batteryPct * 100)+ "%");
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return formatSize(availableBlocks * blockSize);
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

}
