package ru.gmm.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import ru.gmm.demo.model.enums.TransactionStatus;
import ru.gmm.demo.model.support.BaseEntity;
import ru.gmm.demo.model.support.CDTEntity;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Getter
@Setter
@ToString

@Entity
@Table(name = "transactions")
public class TransactionEntity extends BaseEntity {

    @Embedded
    private CDTEntity cdtEntity;

    @Column(name = "accountFrom", nullable = false, updatable = false)
    private String accountFrom;

    @Column(name = "accountTo", nullable = false, updatable = false)
    private String accountTo;

    @Column(name = "sum", nullable = false, updatable = true)
    private BigDecimal sum;

    //типы транзакции: депозит, снятие/зачисление, перевод
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "description", nullable = true)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_entity_id_from", foreignKey = @ForeignKey(name = "fk_account_entity_id_from"))
    @ToString.Exclude
    private AccountEntity accountEntityIdFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_entity_id_to", foreignKey = @ForeignKey(name = "fk_account_entity_id_to"))
    @ToString.Exclude
    private AccountEntity accountEntityIdTo;
}
