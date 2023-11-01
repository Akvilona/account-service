/**
 * Создал Андрей Антонов 10/31/2023 5:42 PM.
 **/

package ru.gmm.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Accessors(chain = true)
public class AccEntity {
    private Long id;
    private String account;
    private BigDecimal sum;
    private String name;
    private String surname;
}
