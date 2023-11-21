/**
 * Создал Андрей Антонов 11/12/2023 1:37 PM.
 **/

package ru.gmm.demo.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.gmm.demo.model.AccountEntity;
import ru.gmm.demo.model.TransactionEntity;
import ru.gmm.demo.model.api.CreateTransactionRq;
import ru.gmm.demo.model.api.CreateTransactionRs;
import ru.gmm.demo.model.api.TransactionRs;
import ru.gmm.demo.model.api.TransactionUpdateRq;

import java.util.Random;

@Mapper(config = MapperConfiguration.class)
public interface TransactionMapper {
    Random RANDOM = new Random();

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", expression = "java( RANDOM.nextLong() )")
    @Mapping(target = "sum", source = "sum")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "accountFrom", source = "accountFrom")
    @Mapping(target = "accountTo", source = "accountTo")
    TransactionEntity toTransactionEntity(CreateTransactionRq createTransactionRq,
                                          AccountEntity accountFrom,
                                          AccountEntity accountTo);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "sum", source = "sum")
    @Mapping(target = "type", source = "status")
    @Mapping(target = "description", source = "description")
    void toTransactionEntity(@MappingTarget TransactionEntity transactionEntity, TransactionUpdateRq request);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sum", source = "sum")
    @Mapping(target = "status", source = "type")
    @Mapping(target = "description", source = "description")
    CreateTransactionRs toCreateTransactionRs(final TransactionEntity transactionEntity);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "status", source = "type")
    @Mapping(target = "sum", source = "sum")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "accountFrom", source = "accountFrom.number")
    @Mapping(target = "accountTo", source = "accountTo.number")
    @Mapping(target = "createDateTime", source = "audit.createDateTime")
    @Mapping(target = "updateDateTime", source = "audit.updateDateTime")
    TransactionRs toTransactionRs(TransactionEntity transaction);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "sum", source = "sum")
    @Mapping(target = "status", source = "type")
    @Mapping(target = "description", source = "description")
    TransactionUpdateRq toTransactionUpdateRq(final TransactionEntity transactionEntity);
}
