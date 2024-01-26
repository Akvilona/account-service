package com.account.accountservice.repository;

import com.account.accountservice.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query("""
        select a from AccountEntity a
        where a.number = :number
        and a.status = com.account.accountservice.model.enums.AccountStatus.OPENED
        """)
    Optional<AccountEntity> findOpenedAccountByNumber(@Param("number") String number);

}
