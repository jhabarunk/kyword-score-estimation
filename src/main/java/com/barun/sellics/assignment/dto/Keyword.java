package com.barun.sellics.assignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author barun.jha on 09/12/21
 */
public class Keyword {
    @JsonProperty("keyword")
    private final String  keyword;
    @JsonProperty("score")
    private final int score;

    public Keyword(String keyword, int score) {
        this.keyword = keyword;
        this.score = score;
    }
}
