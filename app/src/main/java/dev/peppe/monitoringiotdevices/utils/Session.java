package dev.peppe.monitoringiotdevices.utils;

public class Session {
    public String serverUri;
    public String clientId;
    public String username;
    public String password;

    public Session (String uri,String client,String user, String passw){
        this.serverUri = uri;
        this.clientId = client;
        this.username = user;
        this.password = passw;
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
}
