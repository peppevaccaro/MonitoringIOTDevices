package dev.peppe.monitoringiotdevices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

import dev.peppe.monitoringiotdevices.helpers.MQTTHelper;
import dev.peppe.monitoringiotdevices.helpers.SessionArrayAdapter;
import dev.peppe.monitoringiotdevices.utils.Session;

public class SessionActivity extends AppCompatActivity {

    MQTTHelper mqttHelper;
    Button connectButton;
    TextView serverUriText;
    TextView clientIdText;
    TextView usernameText;
    TextView passwordText;

    EditText serverURIInput;
    EditText clientIdInput;
    EditText usernameInput;
    EditText passwordInput;

    String serverURI;
    String clientId;
    String user;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_setting);
        serverUriText = findViewById(R.id.serverUriText);
        clientIdText = findViewById(R.id.clientIdText);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        connectButton = findViewById(R.id.connectButton);
        serverURIInput = findViewById(R.id.serverUriInput);
        clientIdInput = findViewById(R.id.clientIdInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverURI = serverURIInput.getText().toString();
                clientId = clientIdInput.getText().toString();
                user = usernameInput.getText().toString();
                password = passwordInput.getText().toString();
                startMqtt();
            }
        });

        ListView listview = findViewById(R.id.sessionsList);

        final ArrayList<Session> list = new ArrayList<Session>();
        Session session1 = new Session("http://prova","client1","peppevaccaro","provaPass");
        list.add(session1);


        SessionArrayAdapter adapter = new SessionArrayAdapter(this,R.layout.session_listitem,list);
        listview.setAdapter(adapter);
    }

    private void startMqtt() {
        mqttHelper = new MQTTHelper(getApplicationContext(),serverURI,clientId,user,password);
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

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }
}
