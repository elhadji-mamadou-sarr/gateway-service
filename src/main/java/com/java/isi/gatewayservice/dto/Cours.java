package com.java.isi.gatewayservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cours {

    private Long id;

    private String libelle;

    private String description;

    private Long classeId;
}
