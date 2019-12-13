package dto;

public class TrackRequest {
    private String id;
    private int playCount;
    private boolean offlineAvailable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public boolean isOfflineAvailable() {
        return offlineAvailable;
    }

    public void setOfflineAvailable(boolean offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
    }
}
