package ru.gmm.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gmm.demo.model.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("""
         SELECT u FROM UserEntity u
         LEFT JOIN FETCH u.accounts
         WHERE u.id = :userId
        """)
    Optional<UserEntity> findUserAccountInfo(@Param("userId") Long userId);
}
