package doan.bai_2.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
@Entity(name = "Countries")
public class CountryEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    @Column(columnDefinition = "VARCHAR(100)")
    private String name;

    @OneToMany(mappedBy = "country")
    private List<ArtistEntity> artists;

    @OneToMany(mappedBy = "country")
    private List<SongEntity> songs;

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public List<SongEntity> getSongs() {
        return songs;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArtistEntity> getArtists() {
        return artists;
    }

    public void setArtists(List<ArtistEntity> artists) {
        this.artists = artists;
    }

    public void setSongs(List<SongEntity> songs) {
        this.songs = songs;
    }
}
