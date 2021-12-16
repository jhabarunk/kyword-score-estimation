package com.barun.sellics.assignment.service;

import com.barun.sellics.assignment.dto.Keyword;

import java.net.URISyntaxException;

/**
 * @author barun.jha on 09/12/21
 */
public interface SearchScoreService {
    Keyword getScore(String keyword) throws URISyntaxException;
}
