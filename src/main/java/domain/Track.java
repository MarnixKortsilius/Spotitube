package domain;

public class Track {
    private String id;
    private String title;
    private String url;
    private String performer;
    private int duration;
    private String album;
    private String publicationDate;
    private String description;
    private boolean isSong;

    public Track(String id, String title, String url,String performer, int duration, String album, String publicationDate, String description, boolean isSong){
        this.id = id;
        this.title = title;
        this.url = url;
        this.performer = performer;
        this.duration = duration;
        this.album = album;
        this.publicationDate = publicationDate;
        this.description = description;
        this.isSong = isSong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSong() {
        return isSong;
    }

    public void setSong(boolean song) {
        isSong = song;
    }
}
