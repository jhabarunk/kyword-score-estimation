package com.barun.sellics.assignment.service.impl;

import com.barun.sellics.assignment.dto.Keyword;
import com.barun.sellics.assignment.service.AwsCompletionService;
import com.barun.sellics.assignment.service.SearchScoreService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @author barun.jha on 10/12/21
 */
@Service
public class DefaultSearchScoreService implements SearchScoreService {

    private static final long MAX_MILLIS = 10000;
    private static final int MAX_SCORE = 100;

    private final AwsCompletionService awsCompletionService;

    public DefaultSearchScoreService(AwsCompletionService awsCompletionService) {
        this.awsCompletionService = awsCompletionService;
    }

    @Override
    public Keyword getScore(String keyword) throws URISyntaxException {
        KeywordStats stats = binarySearch(keyword);
        return new Keyword(keyword, calculateScore(stats, keyword.length()));
    }

    private KeywordStats binarySearch(@org.jetbrains.annotations.NotNull String keyword) throws URISyntaxException {
        long averageTime = 0L;
        int length = keyword.length();
        int mid = length / 2;
        Integer right = null;
        int left = 0;
        int requestCount = 0;
        boolean exist = false;
        int bestIndex = Integer.MAX_VALUE;

        while ((right == null || mid != right) && (requestCount++ * averageTime < MAX_MILLIS)) {
            requestCount++;
            final String keywordChunk = keyword.substring(0, mid);
            int previousIndex = mid;

            Instant start = Instant.now();
            boolean suggestionsContainsKeyword = suggestionsContainsKeyword(keyword, keywordChunk);
            Instant finish = Instant.now();

            averageTime = ((averageTime * (requestCount-1)) +  Duration.between(start,finish).toMillis())/requestCount;

            if (suggestionsContainsKeyword) {
                bestIndex = Math.min(bestIndex, mid);
                exist = true;
                if (mid == 1 || (right != null && right - left == 2) || mid == length) {
                    break;
                }
                mid -= (mid - left) / 2;
                right = previousIndex;
            } else {
                if (mid == length || (right != null && right - left == 2)) {
                    break;
                }
                mid += Math.max(1, (Optional.ofNullable(right).orElse(length) - mid) / 2);
                left = previousIndex;
            }
        }
        return new KeywordStats(exist, bestIndex);
    }

    private int calculateScore(KeywordStats stats, int length) {
        if (!stats.isExist()) {
            return 0;
        } else if (stats.getLowestIndexMatch() == 1) {
            return 100;
        }
        return (length-stats.getLowestIndexMatch()) * (MAX_SCORE/length);
    }


    private boolean suggestionsContainsKeyword(String keyword, String keywordChunk) throws URISyntaxException {
        final List<String> suggestions = awsCompletionService.retrieveSuggestions(keywordChunk);
        return suggestions.stream().anyMatch(suggestion -> suggestion.equalsIgnoreCase(keyword.toLowerCase()));
    }

}

class KeywordStats {
    private final boolean exist;
    private final int lowestIndexMatch;

    KeywordStats(boolean isExist, int lowestIndexMatch) {
        this.exist = isExist;
        this.lowestIndexMatch = lowestIndexMatch;
    }

    boolean isExist() {
        return exist;
    }

    int getLowestIndexMatch() {
        return lowestIndexMatch;
    }
}
