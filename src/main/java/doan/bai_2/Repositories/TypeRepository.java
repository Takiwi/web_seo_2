package doan.bai_2.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doan.bai_2.models.TypeEntity;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity, Integer>{
    boolean existsBySlug(String slug);
}
