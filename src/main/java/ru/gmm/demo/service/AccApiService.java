/**
 * Создал Андрей Антонов 11/1/2023 9:37 AM.
 **/

package ru.gmm.demo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gmm.demo.mapper.AccMapper;
import ru.gmm.demo.model.AccEntity;
import ru.gmm.demo.model.api.AccUpdateRq;
import ru.gmm.demo.repository.AccRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccApiService {
    private final AccRepository accRepository;
    private final AccMapper accMapper;

    public void createAcc(final AccEntity accEntity) {
        accRepository.save(accEntity);
    }

    public List<AccEntity> getAll() {
        return accRepository.findAll();
    }

    public AccEntity findById(final String id) {
        return accRepository.findById(Long.valueOf(id))
            .orElseThrow();
    }

    @Transactional
    public AccEntity updateAcc(final String id, final AccUpdateRq accUpdateRq) {
        final AccEntity acc = accRepository.findById(Long.parseLong(id))
            .orElseThrow();

        return accMapper.accUpdateRq(acc, accUpdateRq);

    }

    public void deleteAccById(final long id) {
        accRepository.deleteById(id);
    }
}
