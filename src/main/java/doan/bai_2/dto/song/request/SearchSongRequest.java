package doan.bai_2.dto.song.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class SearchSongRequest {
    @NotBlank(message = "Title must be not blank!")
    private String title;
    
    private List<String> artistName;
    private String genre;

    // constructor
    public SearchSongRequest(){}

    public SearchSongRequest(String title, List<String> artistName, String genre){
        this.title = title;
        this.artistName = artistName;
        this.genre = genre;
    }

    // getter
    public String getTitle() {
        return title;
    }

    public List<String> getArtistName() {
        return artistName;
    }

    public String getGenre() {
        return genre;
    }

    // setter
    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtistName(List<String> artistName) {
        this.artistName = artistName;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
