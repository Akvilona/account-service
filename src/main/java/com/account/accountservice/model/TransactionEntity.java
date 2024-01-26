package com.account.accountservice.model;

import com.account.accountservice.model.enums.TransactionType;
import com.account.accountservice.model.support.Audit;
import com.account.accountservice.model.support.BaseEntity;
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

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "transactions")
public class TransactionEntity extends BaseEntity {

    @Embedded
    private Audit audit;

    @Column(name = "sum", nullable = false)
    private BigDecimal sum;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_from_id", foreignKey = @ForeignKey(name = "fk_account_from_id_accounts"))
    @ToString.Exclude
    private AccountEntity accountFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_to_id", foreignKey = @ForeignKey(name = "fk_account_to_id_accounts"))
    @ToString.Exclude
    private AccountEntity accountTo;
}
