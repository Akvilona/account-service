package ru.gmm.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gmm.demo.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /*
    @Query("""
          SELECT new UserAccountRs (
                 u.id as userId,
                 u.email as userEmail,
                 u.name as userName,
                 u.surname as userSurname,
                 a.id as accountId,
                 a.number as accountAccount,
                 a.status as accountStatus,
                 a.sum as accountSum,
                 a.description as accountDescription )
         FROM UserEntity u JOIN u.accounts a
         WHERE u.id = :userId
         """)*/
         /*    List<UserAccountRs> getUserAccountInfo(@Param("userId") Long userId);*/
}
