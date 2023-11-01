/**
 * Создал Андрей Антонов 11/1/2023 9:37 AM.
 **/

package ru.gmm.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gmm.demo.model.AccEntity;
import ru.gmm.demo.repository.AccRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccApiService {
    private final AccRepository accRepository;

    public void createAcc(final AccEntity accEntity) {
        accRepository.save(accEntity);
    }

    public List<AccEntity> getAll() {
        return accRepository.getAll();
    }

    public AccEntity findById(final String id) {
        return accRepository.get(Long.valueOf(id));
    }

    public AccEntity updateAcc(final String id, final AccEntity accEntity) {
        return accRepository.updateAcc(id, accEntity);
    }

    public void deleteAccById(final long id) {
        accRepository.deleteAccById(id);
    }
}
