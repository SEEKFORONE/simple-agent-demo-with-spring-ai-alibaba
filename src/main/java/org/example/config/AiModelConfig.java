package org.example.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AiModelConfig {
    @Value("${spring.ai.ollama.base-url}")
    private String ollamaBaseUrl;

    @Value("${spring.ai.ollama.chat.options.model}")
    private String ollamaModel;

    // 设置一个Bean名称，方便后续识别和使用
    // 这里不能命名为ollamaChatModel，因为spring ai alibaba会注册这个bean
    @Bean("myOllamaChatModel")
    public ChatModel ollamaChatModel() {


        return OllamaChatModel.builder()
                .ollamaApi(OllamaApi.builder().baseUrl(ollamaBaseUrl).build())
                .defaultOptions(
                        OllamaChatOptions.builder()
                                .model(ollamaModel)
                                .temperature(0.9)
                                .build())
                .build();
    }
}
