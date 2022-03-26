package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import java.util.List;
import java.util.Map;

class ResponseCheckers {
    static ServerClientMessage
    checkCommunicationTerminated(Map<String, String> response) throws CommunicationTerminated {
        ServerClientMessage responseType = ServerClientMessage.valueOf(response.get("type"));
        if (responseType == ServerClientMessage.OTHER_ENDED_COMMUNICATION)
            throw new CommunicationTerminated("other user terminated communication");
        return responseType;
    }

    static void checkUnexpectedType(List<ServerClientMessage> expected, ServerClientMessage received) {
        if (!expected.contains(received))
            throw new IllegalStateException(
                    "unexpected server message type " + received + "," +
                            "expected one of " + expected
            );
    }
}
