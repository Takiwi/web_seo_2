package doan.bai_2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.SongEntity;
import java.util.List;
import java.util.Optional;


@Repository
public interface SongRepository extends JpaRepository<SongEntity, Integer>, JpaSpecificationExecutor<SongEntity>{
    List<SongEntity> findByAlbum(AlbumEntity album);
    Optional<SongEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
