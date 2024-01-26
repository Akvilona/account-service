/**
 * Создал Андрей Антонов 10/31/2023 5:42 PM.
 **/

package com.account.accountservice.model;

import com.account.accountservice.model.enums.AccountStatus;
import com.account.accountservice.model.support.Audit;
import com.account.accountservice.model.support.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)

@Entity
@Table(name = "accounts")
public class AccountEntity extends BaseEntity {

    @Embedded
    private Audit audit;

    @Column(name = "number", nullable = false, updatable = false, unique = true)
    private String number;

    @Column(name = "sum", nullable = false)
    private BigDecimal sum;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_accounts_users"))
    @ToString.Exclude
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accountFrom")
    @ToString.Exclude
    @Builder.Default
    private List<TransactionEntity> transactionsFrom = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "accountTo")
    @ToString.Exclude
    @Builder.Default
    private List<TransactionEntity> transactionsTo = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = AccountStatus.OPENED;
        }
    }

    public AccountEntity withTransactionsFrom(final TransactionEntity transactionEntity) {
        this.transactionsFrom.add(transactionEntity);
        transactionEntity.setAccountFrom(this);
        return this;
    }

    public AccountEntity withTransactionTo(final TransactionEntity transactionEntity) {
        this.transactionsTo.add(transactionEntity);
        transactionEntity.setAccountTo(this);
        return this;
    }
}
