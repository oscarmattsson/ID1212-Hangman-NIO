package oscarmat.kth.id1212.client.controller;

import javax.json.JsonObject;

public interface ResponseListener {

    public void alertResponseReceived(JsonObject response);

}
