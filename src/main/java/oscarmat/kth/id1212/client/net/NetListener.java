package oscarmat.kth.id1212.client.net;

import oscarmat.kth.id1212.common.Message;

import javax.json.JsonObject;
import java.net.InetSocketAddress;

public interface NetListener {

    public void alertConnected(InetSocketAddress serverAddress);

    public void alertDisconnected();

    public void alertNewMessage(Message.Data message);

}
