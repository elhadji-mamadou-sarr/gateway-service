package com.java.isi.gatewayservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etudiant {

    private Long id;

    private String nom;
    private String prenom;
    private String email;
    private int age;
}
