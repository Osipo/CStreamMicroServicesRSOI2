package ru.osipov.deploy.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.osipov.deploy.entities.UserEntity;
import ru.osipov.deploy.models.user.UserModel;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    List<UserEntity> findByIdIn(List<Long> userIds);

    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

//    @Query("select u from UserEntity u")
//    Page<UserModel> findAllUserEntities(Pageable pageable);
//    @Query("select u from UserEntity u where id = :id")
//    Optional<UserModel> findByIdUserEntity(@Param("id") Long id);
}