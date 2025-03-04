package com.java.isi.gatewayservice.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cours {

    private Long id = null;

    private String libelle;

    private String description;

    private Long classeId;
}
