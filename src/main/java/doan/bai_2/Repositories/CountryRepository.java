package doan.bai_2.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import doan.bai_2.models.CountryEntity;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Integer>{
    Optional<CountryEntity> findByCode(String code);
    Optional<CountryEntity> findByName(String name);
}
