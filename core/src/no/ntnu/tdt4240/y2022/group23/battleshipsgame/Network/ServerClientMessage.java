package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

public enum ServerClientMessage {
    JOIN_LOBBY_WITH_ID,
    PLACEMENT_START,

    GAME_START,
    ACTION_PERFORMED,
    GAME_OVER,

    OTHER_ENDED_COMMUNICATION
}
