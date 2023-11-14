package ru.gmm.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gmm.demo.mapper.AccountMapper;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.api.AccountUpdateRq;
import ru.gmm.demo.repository.AccountRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountApiService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public void createAccount(final AccountEntity accountEntity) {
        accountRepository.save(accountEntity);
    }

    public List<AccountEntity> getAll() {
        return accountRepository.findAll();
    }

    public AccountEntity findById(final String id) {
        return accountRepository.findById(Long.valueOf(id))
            .orElseThrow();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AccountEntity updateAccount(final String id, final AccountUpdateRq accountUpdateRq) {
        final AccountEntity acc = accountRepository.findById(Long.parseLong(id))
            .orElseThrow();

        return accountMapper.toAccountEntity(acc, accountUpdateRq);
    }

    public void deleteAccById(final long id) {
        accountRepository.deleteById(id);
    }
}
