package dto;

import domain.Track;

import java.util.List;

public class PlaylistForResponse {
    private int id;
    private String name;
    private Boolean owner;
    private List<Track> tracks;

    public PlaylistForResponse(int id, String name, Boolean owner, List<Track> tracks){
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.tracks = tracks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }
}
