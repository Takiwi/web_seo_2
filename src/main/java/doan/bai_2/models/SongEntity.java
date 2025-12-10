package doan.bai_2.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "Songs")
public class SongEntity extends BaseEntity{
    @Column(columnDefinition = "VARCHAR(100)")
    private String title;

    private LocalDate releaseDate;

    @Column(columnDefinition = "LONGTEXT")
    private String lyrics;

    @ManyToOne
    @JoinColumn(name = "Country_id")
    private CountryEntity country;
    
    private String image;
    private int views = 0;

    @ManyToOne
    @JoinColumn(name = "Genre_id")
    private GenreEntity genre;

    @ManyToOne
    @JoinColumn(name = "Album_id")
    private AlbumEntity album;

    @OneToMany(mappedBy = "song")
    private List<ArtistSong> artistSongs;

    // constructor
    public SongEntity(){}

    public SongEntity(String title, LocalDate releaseDate, String lyrics){
        this.title = title;
        this.releaseDate = releaseDate;
        this.lyrics = lyrics;
    }

    // getter
    public String getTitle() { return title;}
    public LocalDate getReleaseDate() { return releaseDate;}
    public String getLyrics() { return lyrics;}
    public CountryEntity getCountry() {return country;}
    public int getViews() { return views; }
    public String getImage() { return image;}
    public GenreEntity getGenre() { return genre;}
    public List<ArtistSong> getArtists() { return artistSongs;}
    public AlbumEntity getAlbum() { return album;}

    // setter
    public void setTitle(String title) {this.title = title;}
    public void setReleaseDate(LocalDate releaseDate) {this.releaseDate = releaseDate;}
    public void setLyrics(String lyrics) {this.lyrics = lyrics;}
    public void setCountry(CountryEntity country) {this.country = country;}
    public void setViews(int views) {this.views = views;}
    public void setImage(String image) { this.image = image;}
    public void setGenre(GenreEntity genre) { this.genre = genre;}
    public void setArtists(List<ArtistSong> artistSongs) {this.artistSongs = artistSongs;}
    public void setAlbum(AlbumEntity album) { this.album = album;}
}
