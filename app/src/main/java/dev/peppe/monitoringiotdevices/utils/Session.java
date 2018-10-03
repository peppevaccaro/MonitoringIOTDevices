package dev.peppe.monitoringiotdevices.utils;

import java.io.Serializable;

import dev.peppe.monitoringiotdevices.helpers.MQTTHelper;

public class Session implements Serializable {
    public String serverUri;
    public String clientId;
    public String username;
    public String password;
    public MQTTHelper mqttHelper;

    public Session (String uri,String client,String user, String passw,MQTTHelper helper){
        this.serverUri = uri;
        this.clientId = client;
        this.username = user;
        this.password = passw;
        this.mqttHelper = helper;
    }

    public String getClientId() {
        return clientId;
    }

    public String getServerUri() {
        return serverUri;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public MQTTHelper getMqttHelper() {
        return mqttHelper;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setServerUri(String serverUri) {
        this.serverUri = serverUri;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setMqttHelper(MQTTHelper helper){ this.mqttHelper = helper; }
}
