package com.account.accountservice.model;

import com.account.accountservice.model.support.Audit;
import com.account.accountservice.model.support.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Embedded
    private Audit audit;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;
    private String name;
    private String surname;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    @ToString.Exclude
    @Builder.Default
    private List<AccountEntity> accounts = new ArrayList<>();

    //bi-directional many-to-one association to AccountEntity
    public UserEntity withAccount(final AccountEntity accountEntity) {
        this.accounts.add(accountEntity);
        accountEntity.setUser(this);
        return this;
    }

}
