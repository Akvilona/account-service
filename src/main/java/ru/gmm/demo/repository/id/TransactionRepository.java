/**
 * Создал Андрей Антонов 11/12/2023 12:13 PM.
 **/

package ru.gmm.demo.repository.id;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gmm.demo.model.TransactionEntity;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
