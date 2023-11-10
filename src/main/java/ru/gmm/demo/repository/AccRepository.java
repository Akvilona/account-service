package ru.gmm.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gmm.demo.model.AccountEntity;

@Repository
public interface AccRepository extends JpaRepository<AccountEntity, Long> {
}
