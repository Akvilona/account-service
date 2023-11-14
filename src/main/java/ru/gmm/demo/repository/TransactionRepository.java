/**
 * Создал Андрей Антонов 11/12/2023 12:13 PM.
 **/

package ru.gmm.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.gmm.demo.model.TransactionEntity;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("""
        select tr from TransactionEntity tr
        left join fetch tr.accountFrom
        left join fetch tr.accountTo
        """)
    List<TransactionEntity> fetchTransactionEntityList();
}
