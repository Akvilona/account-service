package ru.gmm.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.enums.AccountStatus;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByNumberAndStatus(String number, AccountStatus status);
}
