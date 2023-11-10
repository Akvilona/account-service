/**
 * Создал Андрей Антонов 10/31/2023 6:36 PM.
 **/

package ru.gmm.demo.repository;

import org.springframework.stereotype.Component;
import ru.gmm.demo.exception.ErrorCode;
import ru.gmm.demo.exception.ServiceException;
import ru.gmm.demo.model.AccEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListAccRepository {
    private final List<AccEntity> accEntities = new ArrayList<>();

    public void save(final AccEntity accEntity) {
        accEntities.add(accEntity);
    }

    public AccEntity get(final Long id) {
        return findById(id);
    }

    public List<AccEntity> getAll() {
        return accEntities;
    }

    public boolean deleteAccById(final Long id) {
        return accEntities.removeIf(accEntity -> accEntity.getId().equals(id));
    }

    private AccEntity findById(final Long id) {
        return accEntities.stream()
            .filter(accEntity -> accEntity.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, id));
    }
}
