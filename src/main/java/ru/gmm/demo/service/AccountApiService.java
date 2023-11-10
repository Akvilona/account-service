package ru.gmm.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gmm.demo.mapper.AccountMapper;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.api.AccUpdateRq;
import ru.gmm.demo.repository.AccRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountApiService {
    private final AccRepository accRepository;
    private final AccountMapper accountMapper;

    public void createAcc(final AccountEntity accountEntity) {
        accRepository.save(accountEntity);
    }

    public List<AccountEntity> getAll() {
        return accRepository.findAll();
    }

    public AccountEntity findById(final String id) {
        return accRepository.findById(Long.valueOf(id))
            .orElseThrow();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AccountEntity updateAcc(final String id, final AccUpdateRq accUpdateRq) {
        final AccountEntity acc = accRepository.findById(Long.parseLong(id))
            .orElseThrow();

        return accountMapper.accUpdateRq(acc, accUpdateRq);
    }

    public void deleteAccById(final long id) {
        accRepository.deleteById(id);
    }
}
