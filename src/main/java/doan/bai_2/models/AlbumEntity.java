package doan.bai_2.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "Albums")
public class AlbumEntity extends BaseEntity{
    @Column(columnDefinition = "VARCHAR(100)")
    private String title;

    private LocalDate releaseDate;

    @Column(columnDefinition = "VARCHAR(100)")
    private String label;

    @Column(columnDefinition = "LONGTEXT")
    private String description;
    
    private String image;

    @OneToMany(mappedBy = "album")
    private List<SongEntity> songs;

    @ManyToOne
    @JoinColumn(name = "Artist_id")
    private ArtistEntity artist;

    public AlbumEntity(){}

    public AlbumEntity(String title, LocalDate releaseDate, String label, String description, ArtistEntity artist){
        this.title = title;
        this.releaseDate = releaseDate;
        this.label = label;
        this.description = description;
        this.artist = artist;
    }

    public String getTitle() { return title;}
    public LocalDate getReleaseDate() { return releaseDate;}
    public String getLabel() { return label;}
    public String getImage() { return image;}
    public String getDescription() { return description;}
    public ArtistEntity getArtist() { return artist;}
    public List<SongEntity> getSongs() { return songs;}

    public void setTitle(String title) { this.title = title;}
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate;}
    public void setLabel(String label) { this.label = label;}
    public void setImage(String image) { this.image = image;}
    public void setDescription(String description) { this.description = description;}
    public void setArtist(ArtistEntity artist) { this.artist = artist;}
    public void setSongs(List<SongEntity> songs) { this.songs = songs;}
}
