/**
 * Создал Андрей Антонов 10/19/2023 11:44 AM
 **/
package ru.gmm.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserUpdateRq {
    private Long id;
    private String name;
    private String password;
}
