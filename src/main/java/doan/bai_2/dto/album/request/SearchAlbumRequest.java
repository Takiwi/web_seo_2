package doan.bai_2.dto.album.request;

public class SearchAlbumRequest {
    private String title;
    private String label;
    private String artist;

    public SearchAlbumRequest(){}

    public SearchAlbumRequest(String title, String label, String artist){
        this.title = title;
        this.label = label;
        this.artist = artist;
    }
    
    public String getTitle() { return title;}
    public String getLabel() { return label;}
    public String getArtist() { return artist;}

    public void setTitle(String title) { this.title = title;}
    public void setLabel(String label) { this.label = label;}
    public void setArtist(String artist) { this.artist = artist;}
}
