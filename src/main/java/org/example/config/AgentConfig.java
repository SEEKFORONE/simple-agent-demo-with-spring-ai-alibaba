package org.example.config;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.flow.agent.LlmRoutingAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import org.example.tools.WeatherTool;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AgentConfig {

    @Bean
    public ReactAgent dataAgent(@Qualifier("myOllamaChatModel") ChatModel chatModel) {
        // 直接用注入好的 chatModel 去构建ReactAgent
        return ReactAgent.builder()
                .name("data_agent")
                .model(chatModel) // <--- 这里使用注入的模型
                // ... 其他配置
                .systemPrompt("你是一个专业的程序员。" +        // 系统指令
                        "当用户询问代码方面的问题时，请你联网获取相关的答案。" +
                        "回答时请用尖酸刻薄的语气。")
                .description("查询所有的代码相关的问题")
                .build();
    }

    @Bean
    public ReactAgent weatherAgent(@Qualifier("myOllamaChatModel") ChatModel chatModel) {
        // tools
        ToolCallback[] weatherTool = ToolCallbacks.from(new WeatherTool());
        // 直接用注入好的 chatModel 去构建ReactAgent
        return ReactAgent.builder()
                .name("weather_agent")
                .tools(weatherTool)
                .model(chatModel) // <--- 这里使用注入的模型
                .systemPrompt("你是一个专业的天气预报助手。" +        // 系统指令
                        "当用户询问天气时，请调用 get_weather 工具获取实时天气。" +
                        "回答时请用尖酸刻薄的语气。")
                .instruction("对于所有天气查询，第一步都是调用 get_weather 工具。")
                .description("查询所有的天气，旅行或者娱乐相关的话题")
                .build();
    }

    @Bean
    public LlmRoutingAgent llmAgent(@Qualifier("myOllamaChatModel") ChatModel chatModel, ReactAgent dataAgent, ReactAgent weatherAgent) {
        // 当没有写description时这里找ai模型可能根据设置的name属性来找
        // 所以命名还是得注意下
        return LlmRoutingAgent.builder()
                .name("weather_assistant")                          // Agent 唯一标识
                .model(chatModel)                                   // 指定 AI 模型
                .subAgents(List.of(dataAgent, weatherAgent))
                .systemPrompt("你是一个主管助手，请根据用户的问题，选择最合适的专家助手来处理。" +
                        "当有可以使用的工具时请调用工具回答。")
                .saver(new MemorySaver())                           // 开启短期记忆
                .build();
    }
}
