package doan.bai_2.Repositories;

import doan.bai_2.models.RoleEntity;
import doan.bai_2.models.UserEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    boolean existsBySlug(String slug);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<UserEntity> findByRole(RoleEntity roleId);

    Optional<UserEntity> findByUsername(String username);
}
