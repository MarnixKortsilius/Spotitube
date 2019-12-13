package dto;

import java.util.List;

public class PlaylistResponse {
    private List<PlaylistForResponse> playlists;

    public PlaylistResponse(){

    }
    public  PlaylistResponse(List<PlaylistForResponse> playlists){
        this.playlists = playlists;
    }

    public List<PlaylistForResponse> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlaylistForResponse> playlists) {
        this.playlists = playlists;
    }
}
