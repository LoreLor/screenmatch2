package com.aluracursos.screenmatch2.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultarChatGPT {
    public static String obtenerTraduccion(String texto) {
        OpenAiService service = new OpenAiService("tokenAI");

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduce a español el siguente texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();
        var response = service.createCompletion(request);
        return response.getChoices().get(0).getText();
    }
}
