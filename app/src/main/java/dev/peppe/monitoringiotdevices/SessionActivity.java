package dev.peppe.monitoringiotdevices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
    ArrayList<Session> list;
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
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        serverUriText = findViewById(R.id.serverUriText);
        clientIdText = findViewById(R.id.clientIdText);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        connectButton = findViewById(R.id.connectButton);
        serverURIInput = findViewById(R.id.serverUriInput);
        clientIdInput = findViewById(R.id.clientIdInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        listview = findViewById(R.id.sessionsList);
        list = new ArrayList<Session>();
        final SessionArrayAdapter adapter = new SessionArrayAdapter(this,R.layout.session_listitem,list,mqttHelper);
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(TextUtils.isEmpty(serverURIInput.getText().toString())))
                    serverURI = serverURIInput.getText().toString();
                else
                    serverURI = getString(R.string.serverUri);
                if(!(TextUtils.isEmpty(clientIdInput.getText().toString())))
                    clientId = clientIdInput.getText().toString();
                else
                    clientId = getString(R.string.clientId);
                if(!(TextUtils.isEmpty(usernameInput.getText().toString())))
                    user = usernameInput.getText().toString();
                else
                    user = getString(R.string.username);
                if(!(TextUtils.isEmpty(passwordInput.getText().toString())))
                    password = passwordInput.getText().toString();
                else
                    password = getString(R.string.password);
                startMqtt(serverURI,clientId,user,password);
                adapter.setMqttHelper(mqttHelper);
                Session session = new Session(serverURI,clientId,user,password,mqttHelper);
                list.add(session);
                listview.invalidateViews();
                listview.refreshDrawableState();
            }
        });
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

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(list!=null) {
            Intent resultIntent = new Intent(this,MainActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("mqttHelper", list.get(0).mqttHelper);
            resultIntent.putExtras(mBundle);
            startActivity(resultIntent);
        }
        else{
            Intent resultIntent = new Intent();
            setResult(RESULT_CANCELED, resultIntent);
        }
        finish();
        return true;
    }
}
