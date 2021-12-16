package com.barun.sellics.assignment.service;

import java.net.URISyntaxException;
import java.util.List;

/**
 * @author barun.jha on 09/12/21
 */
public interface AwsCompletionService {
    /**
     *
     * @param keyword
     * @return
     * @throws URISyntaxException
     */
    List<String> retrieveSuggestions(String keyword) throws URISyntaxException;
}
