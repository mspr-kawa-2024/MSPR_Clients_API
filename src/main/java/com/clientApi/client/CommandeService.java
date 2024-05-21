package com.clientApi.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CommandeService {

    private final RestTemplate restTemplate;

    @Autowired
    public CommandeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getCommandeById(Long id) {
        String url = "http://localhost:8081/api/v1/command/" + id;
        return restTemplate.getForObject(url, Map.class);
    }

    public List<Map<String, Object>> getAllCommandes() {
        String url = "http://localhost:8081/api/v1/command";
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}).getBody();
    }
}
