package org.example.service;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.flow.agent.LlmRoutingAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataService {
    private final ReactAgent weatherAgent;
    private final ReactAgent dataAgent;
    private final LlmRoutingAgent llmAgent;

    public DataService(ReactAgent weatherAgent, ReactAgent dataAgent, LlmRoutingAgent llmAgent) {
        this.weatherAgent = weatherAgent;
        this.dataAgent = dataAgent;
        this.llmAgent = llmAgent;
    }

    public String askWeather(String city) {
        try {
            AssistantMessage result = weatherAgent.call(city);
            return result.getText();
        } catch (GraphRunnerException e) {
            throw new RuntimeException(e);
        }
    }

    public String askData(String question) {
        try {
            AssistantMessage result = dataAgent.call(question);
            return result.getText();
        } catch (GraphRunnerException e) {
            throw new RuntimeException(e);
        }
    }

    // 使用主管 Agent 自动路由
    public String askWithSupervisor(String userInput, String sessionId) {
        var config = RunnableConfig.builder().threadId(sessionId).build();
        try {
            Optional<OverAllState> result = llmAgent.invoke(userInput, config);

            if (result.isPresent()) {
                OverAllState state = result.get();
                return state.data().values().stream().filter(value -> value instanceof List<?>)
                        //这里的结构还得再深一层
                        .map(value -> (List<?>) value)
                        .flatMap(List::stream)
                        //拿到对应的结果，找到最后一条message返回
                        .filter(value -> value instanceof AssistantMessage)
                        .map(value -> (AssistantMessage) value)
                        .reduce((first, second) -> second)
                        .map(AssistantMessage::getText)
                        .filter(text -> !text.isEmpty())
                        .orElse("未找到处理结果");
            } else {
                return "未找到处理结果";
            }
        } catch (GraphRunnerException e) {
            return "未找到处理结果";
        }
    }
}
