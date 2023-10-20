/**
 * Создал Андрей Антонов 10/17/2023 5:12 PM
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
public class User {
    private Long id;
    private String name;
    private String serName;
    private String password;


}
