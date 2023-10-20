/**
 * Создал Андрей Антонов 10/17/2023 5:10 PM
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
public class UserPutRq {
    private String surName;
}
