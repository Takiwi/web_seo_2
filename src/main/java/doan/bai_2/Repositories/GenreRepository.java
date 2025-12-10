package doan.bai_2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doan.bai_2.models.GenreEntity;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Integer>{
    boolean existsBySlug(String slug);
}
