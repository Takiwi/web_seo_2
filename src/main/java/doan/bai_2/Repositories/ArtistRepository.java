package doan.bai_2.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import doan.bai_2.models.AlbumEntity;
import doan.bai_2.models.ArtistEntity;


@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Integer>, JpaSpecificationExecutor<ArtistEntity>{
    boolean existsBySlug(String slug);
    Optional<ArtistEntity> findBySlug(String slug);
    Optional<ArtistEntity> findByName(String name);
    Optional<ArtistEntity> findByAlbum(AlbumEntity album);
    Optional<ArtistEntity> findByStageName(String stageName);
    Optional<ArtistEntity> findByNameOrStageName(String keyword1, String keyword2);

    @Query("SELECT a.id FROM Artists a")
    List<Integer> getAllIdList();

    List<ArtistEntity> findByNameInOrStageNameIn(List<String> name, List<String> stageName);
}
