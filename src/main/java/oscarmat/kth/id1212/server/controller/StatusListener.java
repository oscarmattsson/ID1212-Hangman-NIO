package oscarmat.kth.id1212.server.controller;

import oscarmat.kth.id1212.client.net.NetListener;

public interface StatusListener extends NetListener {

    public void updateStatus(String status);

}
