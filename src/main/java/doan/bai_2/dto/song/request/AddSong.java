package doan.bai_2.dto.song.request;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class AddSong {
    @NotBlank(message = "Title is not be blank")
    private String title;

    // @NotBlank(message = "Artist name cannot be blank") 
    @NotEmpty(message = "Artist list cannot be empty")
    private List<String> artistName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    
    private String lyrics;
    private String country;
    private String genre;
    private MultipartFile image;

    // getter
    public String getTitle() { return title;}
    public LocalDate getReleaseDate() { return releaseDate;}
    public String getLyrics() { return lyrics;}
    public String getCountry() {return country;}
    public String getGenre() { return genre;}
    public List<String> getArtistName() { return artistName;}
    public MultipartFile getImage() { return image;}
    
    // setter
    public void setTitle(String title) {this.title = title;}
    public void setReleaseDate(LocalDate releaseDate) {this.releaseDate = releaseDate;}
    public void setLyrics(String lyrics) {this.lyrics = lyrics;}
    public void setCountry(String country) {this.country = country;}
    public void setGenre(String genre) { this.genre = genre;}
    public void setArtistName(List<String> artistName) { this.artistName = artistName;}
    public void setImage(MultipartFile image) { this.image = image;}
}
