/**
 * Создал Андрей Антонов 10/31/2023 5:42 PM.
 **/

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import ru.gmm.demo.model.enums.AccountStatus;
import ru.gmm.demo.model.support.Audit;
import ru.gmm.demo.model.support.BaseEntity;

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
@Table(name = "accounts")
public class AccountEntity extends BaseEntity {

    @Embedded
    private Audit audit;
    @Column(name = "account", nullable = false, updatable = false)
    private String account;
    @Column(name = "sum", nullable = false)
    private BigDecimal sum;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_accounts_users"))
    @ToString.Exclude
    private UserEntity user;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = AccountStatus.OPENED;
        }
    }

}
