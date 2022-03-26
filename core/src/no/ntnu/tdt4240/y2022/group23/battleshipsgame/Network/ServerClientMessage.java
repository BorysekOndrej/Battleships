package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

public enum ServerClientMessage {
    JOINED_LOBBY_WITH_ID,
    NO_SUCH_LOBBY,
    PLACEMENT_START,

    GAME_START,
    ACTION_PERFORMED,
    GAME_OVER,

    OTHER_ENDED_COMMUNICATION
}
