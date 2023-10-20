package ru.gmm.demo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
//@SuppressWarnings("PMD.ShortClassName")
public class User {
    private Long id;
    private String name;
    private String surname;
    private String password;

}
