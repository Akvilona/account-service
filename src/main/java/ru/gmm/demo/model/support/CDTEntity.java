/**
 * Создал Андрей Антонов 11/10/2023 1:39 PM.
 **/

package ru.gmm.demo.model.support;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Embeddable
@Data
public class CDTEntity {
    @CreationTimestamp
    @Column(name = "create_date_time", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "update_date_time", nullable = false, updatable = false)
    private LocalDateTime updateDateTime;
}
