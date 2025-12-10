package doan.bai_2.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ArtistSongID implements Serializable{
    @Column(name = "artist_id")
    private Integer artistId;

    @Column(name = "song_id")
    private Integer songId;

    public ArtistSongID(){}

    public ArtistSongID(Integer artistId, Integer songId){
        this.artistId = artistId;
        this.songId = songId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtistSongID that = (ArtistSongID) o;
        return Objects.equals(artistId, that.artistId) &&
            Objects.equals(songId, that.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistId, songId);
    }
    
    public Integer getArtistId() { return artistId;}
    public Integer getSongId() { return songId;}

    public void setArtistId(Integer artistId) { this.artistId = artistId;}
    public void setSongId(Integer songId) { this.songId = songId;}
}
