package com.barun.sellics.assignment.service.impl;

import com.barun.sellics.assignment.service.AwsCompletionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.context.annotation.Primary;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author barun.jha on 09/12/21
 */
@Service
public class DefaultAwsCompletionService implements AwsCompletionService {
    private static final String AWS_COMPLETION_URL = "https://completion.amazon.com/search/complete?search-alias=aps&client=amazon-search-ui&mkt=1&q=%s";

    private final RestTemplate restTemplate;

    public DefaultAwsCompletionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public List<String> retrieveSuggestions(String keyword) throws URISyntaxException {
        final String url = String.format(AWS_COMPLETION_URL, keyword.replace(" ", "+"));
        RequestEntity request = RequestEntity.get(new URI(url)).build();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        if (response == null) {
            throw new RuntimeException("Cannot process response from Amazon as response is null from Amazon side");
        }

        List<String> suggestions = parseSuggestions(response.getBody());
        return suggestions;
    }

    private List<String> parseSuggestions(String response) {
        ArrayNode arrayNode;
        try {
            arrayNode = (ArrayNode) new ObjectMapper().readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot process Amazon response", e);
        }
        final ArrayNode suggestionsAsArrayNode = (ArrayNode) arrayNode.get(1);
        List<String> suggestions = new LinkedList<>();
        suggestionsAsArrayNode.elements().forEachRemaining(keyword -> suggestions.add(keyword.asText()));
        return suggestions;
    }

}
