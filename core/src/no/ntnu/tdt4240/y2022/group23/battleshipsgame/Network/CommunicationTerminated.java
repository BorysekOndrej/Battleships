package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import java.io.IOException;

public class CommunicationTerminated extends IOException {
    public CommunicationTerminated() {}
    public CommunicationTerminated(String message) { super(message); }
    public CommunicationTerminated(Throwable e) { super(e); }
    public CommunicationTerminated(String message, Throwable e) { super(message, e); }
}
