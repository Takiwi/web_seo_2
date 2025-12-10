package doan.bai_2.dto.album.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

public class AddAlbum {
    @NotBlank(message = "Title must be not blank!")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    private String label;
    private String description;
    private MultipartFile image;

    @NotBlank(message = "Artist must be not blank!")
    private String artist;

    public String getTitle() { return title;}
    public LocalDate getReleaseDate() { return releaseDate;}
    public String getLabel() { return label;}
    public MultipartFile getImage() { return image;}
    public String getDescription() { return description;}
    public String getArtist() { return artist;}

    public void setTitle(String title) { this.title = title;}
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate;}
    public void setLabel(String label) { this.label = label;}
    public void setImage(MultipartFile image) { this.image = image;}
    public void setDescription(String description) { this.description = description;}
    public void setArtist(String artist) { this.artist = artist;}
}
