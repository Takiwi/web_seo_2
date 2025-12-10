package doan.bai_2.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.ArtistSong;
import doan.bai_2.models.ArtistSongID;
import doan.bai_2.models.SongEntity;

@Repository
public interface ArtistSongRepository extends JpaRepository<ArtistSong, ArtistSongID>{
    Optional<ArtistSong> findByArtistAndSong(ArtistEntity artist, SongEntity song);
    List<ArtistSong> findBySong(SongEntity song);
    List<ArtistSong> findArtistBySong(SongEntity song);
}
