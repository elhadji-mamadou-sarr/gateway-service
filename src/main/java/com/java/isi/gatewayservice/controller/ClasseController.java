package com.java.isi.gatewayservice.controller;

import com.java.isi.gatewayservice.dto.Classe;
import com.java.isi.gatewayservice.dto.Cours;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/classes")
public class ClasseController {

    private final WebClient webClient;

    public ClasseController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    // Afficher la liste des classes
    @GetMapping
    public Mono<String> listeDesClasses(Model model) {
        return webClient.get()
                .uri("/api/classes")
                .retrieve()
                .bodyToFlux(Classe.class)
                .collectList()
                .defaultIfEmpty(List.of())
                .doOnNext(classes -> model.addAttribute("classes", classes))
                .thenReturn("classes/list"); // Renvoie le nom de la vue
    }

    // Afficher le formulaire d'ajout d'une classe
    @GetMapping("/ajout")
    public String formulaireAjout(Model model) {
        model.addAttribute("classe", new Classe());
        return "classes/add";
    }

    // Ajouter une classe
    @PostMapping("/ajout")
    public Mono<String> ajouterClasse(@ModelAttribute Classe classe) {
        return webClient.post()
                .uri("/api/classes")
                .bodyValue(classe)
                .retrieve()
                .bodyToMono(Classe.class)
                .thenReturn("redirect:/classes"); // Redirection vers la liste
    }

    // Afficher le formulaire d'édition
    @GetMapping("/edit/{id}")
    public Mono<String> formulaireModification(@PathVariable Long id, Model model) {
        return webClient.get()
                .uri("/api/classes/{id}", id)
                .retrieve()
                .bodyToMono(Classe.class)
                .doOnNext(classe -> model.addAttribute("classe", classe))
                .thenReturn("classes/edit");
    }

    // Modifier une classe
    @PostMapping("/edit/{id}")
    public Mono<String> modifierClasse(@PathVariable Long id, @ModelAttribute Classe classe) {
        return webClient.put()
                .uri("/api/classes/{id}", id)
                .bodyValue(classe)
                .retrieve()
                .bodyToMono(Classe.class)
                .thenReturn("redirect:/classes");
    }


    @GetMapping("/{id}")
    public Mono<String> detailsClasse(@PathVariable Long id, Model model) {
        return webClient.get()
                .uri("/api/classes/{id}", id)
                .retrieve()
                .bodyToMono(Classe.class)
                .zipWith(
                        webClient.get()
                                .uri("/api/cours/classe/{id}", id)
                                .retrieve()
                                .bodyToFlux(Cours.class)
                                .collectList()
                )
                .doOnNext(tuple -> {
                    model.addAttribute("classe", tuple.getT1());
                    model.addAttribute("cours", tuple.getT2());
                })
                .thenReturn("classes/details"); // Supposons que tu as une vue "details"
    }


    // Supprimer une classe
    @PostMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable Long id) {
        return webClient.delete()
                .uri("/api/classes/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .thenReturn("redirect:/classes");
    }
}