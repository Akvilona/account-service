/**
 * Создал Андрей Антонов 10/31/2023 6:36 PM.
 **/

package ru.gmm.demo.repository.list;

import org.springframework.stereotype.Component;
import ru.gmm.demo.exception.ErrorCode;
import ru.gmm.demo.exception.ServiceException;
import ru.gmm.demo.model.AccountEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListAccRepository {
    private final List<AccountEntity> accEntities = new ArrayList<>();

    public void save(final AccountEntity accountEntity) {
        accEntities.add(accountEntity);
    }

    public AccountEntity get(final Long id) {
        return findById(id);
    }

    public List<AccountEntity> getAll() {
        return accEntities;
    }

    public boolean deleteAccById(final Long id) {
        return accEntities.removeIf(accEntity -> accEntity.getId().equals(id));
    }

    private AccountEntity findById(final Long id) {
        return accEntities.stream()
            .filter(accEntity -> accEntity.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, id));
    }
}
