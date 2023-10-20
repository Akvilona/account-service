/**
 * Создал Андрей Антонов 10/17/2023 1:07 PM
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
public class  UserRegistrationRq {
    public String name;
}
