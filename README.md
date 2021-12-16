# Estimation API

Application that estimates score of a keyword(how often Amazon customers search for that exact keyword). 

## 1. Algorithm

### Scoring
* Amazon's AutoComplete API returns a list of top 10 keywords that have an exact prefix-match with the input.
* To give an example, if you search "c" on Amazon.com, it autocompletes it to "coffee".
* That means including of all keywords that starts with "c**", "coffee" has a greater search-volume
  than other keywords that starts with "c**".

### Searching
* Since SLA is 10 seconds for a request round-trip to my API call and response of Amazon's API call depends on your network & latency, I need to decide how many max calls I can make
* To decide max calls that can be made, I used the average of request round-trip Amazon's AutoComplete API
* Since we have SLA of 10 seconds and we can't do searching every letter by letter for long keywords, I decided to use binary search and can be completed on O(log n * k) (where n is the number of letters in a keyword, k is the time taken for Amazon API call)
* An example of algorithm:
* Searching for "iphone charger" would start as "iphone" -> "iph" -> "ip" -> "i"
* Ideally score of `100` should be for those words where empty search to Amazon API return the keyword to be score. But for empty search Amazon's API returns empty set
* If match found for the search keyword at starting letter of keyword only, score in `100`.
* If match found for the search keyword after starting letter of keyword,formula used to decide the score is ``


## 3. The Hint

* I think hint given is correct but we have to co-relate the AWS Completion API response with some other API to find more effective score as AWS Completion API response doesn't tell the search volume of the keyword.

## 4. How precise is the outcome

* It's not really precise.
* Since we don't know the internal working of Amazon API and don't know search volume, this algo will give same score for all the keywords where the least index of letter is same when prefix letters are same for the keywords. <br />
  Example: iphone and iphone charger both has score `100` because Amazon API result-set for `i` has both iphone and iphone charger.
* There is no documentation by Amazon for the API, so we don't know how internally it works and we can't get the advantage of it's internal working to score more precisely 


## 5. Docs

Please use swagger ui (http://localhost:8080/swagger-ui) for API documentation  
