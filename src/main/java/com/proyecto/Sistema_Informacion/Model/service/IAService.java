package com.proyecto.Sistema_Informacion.Model.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class IAService {

    private final WebClient webClient;

    public IAService(@Value("${openai.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public String preguntarIA(String mensaje) {

        try {

            Map<String, Object> request = Map.of(
                    "model", "gpt-4.1-mini",
                    "input", mensaje
            );

            Map response = webClient.post()
                    .uri("/responses")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            // 🔥 EXTRAER TEXTO (IMPORTANTE)
            var output = (java.util.List<Map>) response.get("output");

            if (output != null && !output.isEmpty()) {
                var content = (java.util.List<Map>) output.get(0).get("content");
                if (content != null && !content.isEmpty()) {
                    return content.get(0).get("text").toString();
                }
            }

            return "⚠️ No se pudo generar respuesta.";

        } catch (Exception e) {

            e.printStackTrace(); // 👈 VER ERROR REAL EN CONSOLA

            // 🔥 MANEJO ESPECÍFICO
            if (e.getMessage() != null && e.getMessage().contains("429")) {
                return "⚠️ Demasiadas solicitudes. Intenta en unos segundos.";
            }

            return "⚠️ Error al conectar con la IA.";
        }
    }
}