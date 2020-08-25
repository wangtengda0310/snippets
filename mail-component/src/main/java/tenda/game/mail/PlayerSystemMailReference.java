package tenda.game.mail;

public class PlayerSystemMailReference {
    long playerId;
    long systemMailId;
    boolean received;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getSystemMailId() {
        return systemMailId;
    }

    public void setSystemMailId(long systemMailId) {
        this.systemMailId = systemMailId;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }
}
