package domain;

public class PlaylistTrack {
    private String track_id;
    private int playlist_id;
    private boolean offlineAvailable;
    private int playCount;

    public PlaylistTrack(String track_id, int playlist_id, boolean offlineAvailable, int playCount){
        this.track_id = track_id;
        this.playlist_id = playlist_id;
        this.offlineAvailable = offlineAvailable;
        this.playCount = playCount;
    }

    public String getTrackId() {
        return track_id;
    }

    public void setTrackId(String track_id) {
        this.track_id = track_id;
    }

    public int getPlaylistId() {
        return playlist_id;
    }

    public void setPlaylistId(int playlist_id) {
        this.playlist_id = playlist_id;
    }

    public boolean isOfflineAvailable() {
        return offlineAvailable;
    }

    public void setOfflineAvailable(boolean offlineAvailable) {
        this.offlineAvailable = offlineAvailable;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
}
