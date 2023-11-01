/**
 * Создал Андрей Антонов 11/1/2023 9:03 AM.
 **/

package ru.gmm.demo.mapper;

import org.springframework.stereotype.Component;
import ru.gmm.demo.model.AccEntity;
import ru.gmm.demo.model.api.AccRegistrationRq;
import ru.gmm.demo.model.api.AccRegistrationRs;
import ru.gmm.demo.model.api.AccRs;
import ru.gmm.demo.model.api.AccUpdateRq;

import java.util.Random;

@Component
public class AccMapper {
    public static final Random RANDOM = new Random();

    public AccEntity mapToAccEntity(final AccRegistrationRq accRegistrationRq) {
        return AccEntity.builder()
            .id(RANDOM.nextLong())
            .account(accRegistrationRq.getAccount())
            .sum(accRegistrationRq.getSum())
            .surname(accRegistrationRq.getDescription())
            .build();
    }

    public AccEntity mapToAccEntity(final AccUpdateRq accUpdateRq) {
        return AccEntity.builder()
            .account(accUpdateRq.getAccount())
            .sum(accUpdateRq.getSum())
            .surname(accUpdateRq.getDescription())
            .build();
    }

    public AccRegistrationRs mapToAccRegistrationRs(final AccEntity accEntity) {
        return AccRegistrationRs.builder()
            .id(String.valueOf(accEntity.getId()))
            .sum(accEntity.getSum())
            .build();
    }

    public AccRs mapToAccRs(final AccEntity accEntity) {
        return AccRs.builder()
            .id(String.valueOf(accEntity.getId()))
            .account(accEntity.getAccount())
            .sum(accEntity.getSum())
            .description(accEntity.getSurname())
            .build();
    }

    public AccUpdateRq mapToAccUpdateRq(final AccEntity accEntity) {
        return AccUpdateRq.builder()
            .account(accEntity.getAccount())
            .sum(accEntity.getSum())
            .description(accEntity.getSurname())
            .build();
    }
}
