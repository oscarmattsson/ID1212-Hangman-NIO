/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oscarmat.kth.id1212.server.model;

import javax.json.Json;
import javax.json.JsonArray;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Leaderboard implements Serializable {

    private final Map<String, Integer> leaderboard;

    public Leaderboard() {
        leaderboard = new HashMap<>();
    }

    public int getEntry(String alias) {
        return leaderboard.get(alias);
    }

    public JsonArray getLeaderboard() {
        return Json.createArrayBuilder().build();
    }
}
