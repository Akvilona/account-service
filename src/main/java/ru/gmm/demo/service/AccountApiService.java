package ru.gmm.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gmm.demo.mapper.AccountMapper;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.AccountRegistrationRq;
import ru.gmm.demo.model.api.AccountUpdateRq;
import ru.gmm.demo.repository.AccountRepository;
import ru.gmm.demo.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountApiService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    public AccountEntity createAccount(final AccountRegistrationRq request) {
        final UserEntity userEntity = userRepository.findById(request.getUserId())
            .orElseThrow();

        final AccountEntity account = accountMapper.toAccountEntityAndUserEntity(request, userEntity);
        return accountRepository.save(account);
    }

    public List<AccountEntity> getAll() {
        return accountRepository.fetchAll();
    }

    public AccountEntity findById(final String id) {
        return accountRepository.findById(Long.valueOf(id))
            .orElseThrow();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AccountEntity updateAccount(final String id, final AccountUpdateRq accountUpdateRq) {
        final AccountEntity acc = accountRepository.findById(Long.parseLong(id))
            .orElseThrow();

        accountMapper.updateAccountEntity(acc, accountUpdateRq);
        return acc;
    }

    public void deleteAccById(final long id) {
        accountRepository.deleteById(id);
    }
}
