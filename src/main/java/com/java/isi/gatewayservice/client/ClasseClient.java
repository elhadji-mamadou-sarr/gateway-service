package com.java.isi.gatewayservice.client;

import com.java.isi.gatewayservice.dto.ClasseResponse;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ClasseClient {

    @GetMapping
    List<ClasseResponse> getAllClassesApi();

}
