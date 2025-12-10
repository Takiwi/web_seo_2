package doan.bai_2.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.ArtistEntity;
import doan.bai_2.models.SongEntity;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Integer>, JpaSpecificationExecutor<AlbumEntity>{
    List<AlbumEntity> findByArtist(ArtistEntity artist);
    Optional<AlbumEntity> findBySongs(SongEntity song);
    Optional<AlbumEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
