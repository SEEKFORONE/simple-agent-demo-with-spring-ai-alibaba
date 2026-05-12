package org.example.tools;

import org.springframework.ai.tool.annotation.Tool;

public class WeatherTool {
    @Tool(name = "get_weather", description = "获取指定城市天气")
    public String getCityWeather(String city) {
        // 模拟天气查询
        return city + " 晴，25℃";
    }
}
