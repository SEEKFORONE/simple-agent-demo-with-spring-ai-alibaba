package org.example.controller;

import org.example.entity.ChatRequest;
import org.example.service.DataService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/weather")
    public String weather(@RequestParam String city) {
        return dataService.askWeather(city);
    }

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {
        return dataService.askWithSupervisor(request.getMessage(), request.getSessionId());
    }
}
