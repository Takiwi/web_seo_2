package doan.bai_2.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity(name = "ArtistSong")
public class ArtistSong {
    @EmbeddedId
    private ArtistSongID id;

    @ManyToOne
    @MapsId("artistId")
    @JoinColumn(name = "artist_id")
    private ArtistEntity artist;

    @ManyToOne
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    private SongEntity song;

    private String role;

    public ArtistSong(){}
    
    public ArtistSong(ArtistEntity artist, SongEntity song){
        this.artist = artist;
        this.song = song;
    }

    public ArtistSongID getId() {
        return id;
    }

    public ArtistEntity getArtist() {
        return artist;
    }

    public SongEntity getSong() {
        return song;
    }

    public String getRole() {
        return role;
    }

    public void setId(ArtistSongID id) {
        this.id = id;
    }

    public void setArtist(ArtistEntity artist) {
        this.artist = artist;
    }

    public void setSong(SongEntity song) {
        this.song = song;
    }

    public void setRole(String role) {
        this.role = role;
    }
}