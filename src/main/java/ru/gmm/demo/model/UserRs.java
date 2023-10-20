/**
 * Создал Андрей Антонов 10/17/2023 12:02 PM
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
public class UserRs {
    private long id;
    private String name;
    private String surName;
}
