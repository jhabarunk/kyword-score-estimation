package com.barun.sellics.assignment.controller;

import com.barun.sellics.assignment.dto.Keyword;
import com.barun.sellics.assignment.service.SearchScoreService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.net.URISyntaxException;

/**
 * @author barun.jha on 09/12/21
 */
@RestController
@RequestMapping("/")
@Validated
public class SearchScoreController {

    private final SearchScoreService searchScoreService;

    public SearchScoreController(SearchScoreService searchScoreService) {
        this.searchScoreService = searchScoreService;
    }

    @GetMapping("/score")
    public ResponseEntity<Keyword> getScore(@NotBlank(message = "Keyword can't be empty") @RequestParam("keyword") String keyword) throws URISyntaxException {
        return new ResponseEntity<>(searchScoreService.getScore(keyword), HttpStatus.OK);
    }
}
