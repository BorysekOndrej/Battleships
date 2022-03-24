package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

public enum ClientServerMessage  {
    JOIN_MATCHMAKING,
    CREATE_LOBBY,
    JOIN_LOBBY,

    SEND_PLACEMENT,
    SEND_ACTION,
    TIMEOUT,

    END_COMMUNICATION
}
