package dev.peppe.monitoringiotdevices;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dev.peppe.monitoringiotdevices.fragments.PublishFragment;
import dev.peppe.monitoringiotdevices.fragments.SubscribeFragment;
import dev.peppe.monitoringiotdevices.helpers.MQTTHelper;
import dev.peppe.monitoringiotdevices.helpers.PagerAdapter;
import dev.peppe.monitoringiotdevices.helpers.SubscriptionArrayAdapter;
import dev.peppe.monitoringiotdevices.helpers.TopicArrayAdapter;
import dev.peppe.monitoringiotdevices.threads.PublishThread;
import dev.peppe.monitoringiotdevices.utils.ReceivedMessage;
import dev.peppe.monitoringiotdevices.utils.Subscription;
import dev.peppe.monitoringiotdevices.utils.Topic;

public class MainActivity extends AppCompatActivity implements SubscribeFragment.OnSubscribeInteractionListener,
        PublishFragment.OnPublishInteractionListener,TopicArrayAdapter.DeleteRowButtonListener,SubscriptionArrayAdapter.UnsubscribeRowButtonListener {

    private MainActivity.OnMessageArrivedListener mListener;
    private SensorManager manager;
    private SensorEventListener listener;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    MQTTHelper mqttHelper;
    String serverURI;
    String clientId;
    String user;
    String password;
    Map<String, PublishThread> mapThreads ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            final Button connect = findViewById(R.id.connectButton);
            connect.setVisibility(View.VISIBLE);
            final Button disconnect = findViewById(R.id.disconnectButton);
            disconnect.setVisibility(View.INVISIBLE);

            mapThreads = new HashMap<String,PublishThread>();

            manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            listener = new SensorEventListener() {
                @Override
                public void onAccuracyChanged(Sensor arg0, int arg1) {
                }

                @Override
                public void onSensorChanged(SensorEvent event) {
                    Sensor sensor = event.sensor;
                    float currentValue = event.values[0];
                    if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                        //accelerometerValueText.setText(String.valueOf(currentValue));

                    }
                    else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {

                    }
                    else if (sensor.getType() == Sensor.TYPE_LIGHT) {
                        //lightValueText.setText(String.valueOf(currentValue));

                    }
                    else if (sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
                        //humidityValueText.setText(String.valueOf(currentValue));
                    }

                }
            };

            connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serverURI = getString(R.string.serverUri);
                    clientId = randomAlphaNumeric(5);
                    user = getString(R.string.username);
                    password = getString(R.string.password);
                    startMqtt(serverURI,clientId,user,password);
                    connect.setVisibility(View.INVISIBLE);
                    disconnect.setVisibility(View.VISIBLE);
                }
            });

            disconnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mqttHelper.disconnect();
                    connect.setVisibility(View.VISIBLE);
                    disconnect.setVisibility(View.INVISIBLE);

                }
            });

            TabLayout tabLayout = findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText("Chronology"));
            tabLayout.addTab(tabLayout.newTab().setText("Publish"));
            tabLayout.addTab(tabLayout.newTab().setText("Subscribe"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            final ViewPager viewPager = findViewById(R.id.pager);
            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.navigation_sensors_list:
                Intent sensorsIntent = new Intent(getApplicationContext(), SensorActivity.class);
                startActivity(sensorsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startMqtt(String server,String client,String user,String passw) {
        mqttHelper = new MQTTHelper(getApplicationContext(),server,client,user,passw);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                Log.w("Debug", mqttMessage.toString());
                ReceivedMessage message = new ReceivedMessage(mqttMessage,topic,new Date());

                if (mListener != null) {
                    mListener.onMessageArrived(message);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });
    }
    @Override
    public boolean onPublishButtonClicked(Topic topic) {
        if(mqttHelper!= null && mqttHelper.mqttAndroidClient.isConnected()){
            runPublishThread(topic);
            return true;
        }
        else {
            Toast.makeText(this, "Before Connect to Broker ", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onDeleteRowButtonClick(String topic){
        PublishThread t = mapThreads.get(topic);
        t.cancel();
        mapThreads.remove(topic);
    }

    @Override
    public boolean onSubscribeButtonClicked(Subscription sub) {
        if(mqttHelper!= null && mqttHelper.mqttAndroidClient.isConnected()){
            mqttHelper.subscribeToTopic(sub.topicSubscription,sub.qosSubscription);
            Toast.makeText(this,"Subscribed to " +sub.getTopic(), Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Toast.makeText(this, "Before Connect to Broker ", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public boolean onUnsubscribeRowButtonClick(String topic){
        if(mqttHelper!= null && mqttHelper.mqttAndroidClient.isConnected()){
            try {
                mqttHelper.unSubscribe(topic);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            Toast.makeText(this,"Unsubscribed to " +topic, Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Toast.makeText(this, "Before Connect to Broker ", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void runPublishThread(final Topic topic){
        PublishThread t = new PublishThread(mqttHelper,topic);
        t.start();
        mapThreads.put(topic.topicPath,t);
    }

    public interface OnMessageArrivedListener {
        void onMessageArrived(ReceivedMessage message);
    }

    public void setOnMessageArrivedListener(OnMessageArrivedListener activityListener) {
        this.mListener = activityListener;
    }


    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

}
