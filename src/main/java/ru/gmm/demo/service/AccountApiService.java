package ru.gmm.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gmm.demo.exception.ErrorCode;
import ru.gmm.demo.exception.ServiceException;
import ru.gmm.demo.mapper.AccountMapper;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.UserEntity;
import ru.gmm.demo.model.api.AccountRegistrationRq;
import ru.gmm.demo.model.api.AccountRs;
import ru.gmm.demo.model.api.AccountUpdateRq;
import ru.gmm.demo.repository.AccountRepository;
import ru.gmm.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountApiService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    public AccountEntity createAccount(final AccountRegistrationRq request) {
        final UserEntity userEntity = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_002, request.getUserId()));

        final AccountEntity account = accountMapper.toAccountEntityAndUserEntity(request, userEntity);
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<AccountRs> getAll() {
        return accountRepository.findAll().stream()
            .map(accountMapper::toAccountRs)
            .toList();
    }

    @Transactional(readOnly = true)
    public AccountRs findById(final String id) {
        Optional<AccountEntity> byId = accountRepository.findById(Long.valueOf(id));
        if (byId.isEmpty()) {
            throw new ServiceException(ErrorCode.ERR_CODE_007, Long.valueOf(id));
        }
        return accountMapper.toAccountRs(byId.get());
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
