package ru.gmm.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gmm.demo.model.AccountEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Query("""
        select a from AccountEntity a
        where a.number = :number
        and a.status = ru.gmm.demo.model.enums.AccountStatus.OPENED
        """)
    Optional<AccountEntity> findOpenedAccountByNumber(@Param("number") String number);

    @Query("""
        select a from AccountEntity a
        left join fetch a.transactionsFrom
        left join fetch a.transactionsTo
        """)
    List<AccountEntity> fetchAll();
}
