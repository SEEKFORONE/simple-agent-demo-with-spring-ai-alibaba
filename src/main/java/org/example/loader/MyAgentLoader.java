package org.example.loader;

import com.alibaba.cloud.ai.agent.studio.loader.AgentLoader;
import com.alibaba.cloud.ai.graph.agent.BaseAgent;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyAgentLoader implements AgentLoader {

    private final ReactAgent dataAgent;

    // 给个默认的构造函数
    public MyAgentLoader(ReactAgent dataAgent) {
        this.dataAgent = dataAgent;
    }

    //必须实现下面两个方法
    @NotNull
    @Override
    public List<String> listAgents() {
        return List.of();
    }

    @Override
    public BaseAgent loadAgent(String name) {
        return null;
    }

    public ReactAgent getDataAgent() {
        return dataAgent;
    }
}