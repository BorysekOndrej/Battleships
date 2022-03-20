package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import java.util.Map;

public interface INetworkClient {

    Map<String, String> receive();
    boolean send(String url, Map<String, String> data);
}
