package oscarmat.kth.id1212.server.startup;

import oscarmat.kth.id1212.server.model.Leaderboard;
import oscarmat.kth.id1212.server.net.GameServer;

public class Server {

    public static void main(String[] args) {
        Leaderboard leaderboard = new Leaderboard();
        String[] wordList = {
                "Hoozuki",
                "Pantsu",
                "Kitty"
        };
        GameServer server = new GameServer(System.out, wordList, leaderboard);
        new Thread(server).run();
    }

}
