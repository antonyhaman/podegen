package io.github.antonyhaman.podegen.tests;


import io.github.antonyhaman.podegen.core.annotations.PageObject;
import io.github.antonyhaman.podegen.core.data.enums.Flavours;
import io.github.antonyhaman.podegen.core.data.enums.Strategies;
import io.github.antonyhaman.podegen.tests.pageObjects.duckduckgo.SearchResultsPage;
import io.github.antonyhaman.podegen.tests.pageObjects.duckduckgo.StartPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@PageObject(flavour = Flavours.Selenide, strategy = Strategies.PageFactory)
public class PodegenTests {

    @Test
    public void test() {
        String podegen = "podegen";
        open("https://duckduckgo.com/");
        StartPage startPage = StartPage.getPage();
        startPage.getSearchInput().sendKeys(podegen);
        startPage.getSearchInput().submit();
        SearchResultsPage searchResultsPage = SearchResultsPage.getPage();
        searchResultsPage.getSearchResultsList().forEach(elem -> Assertions.assertFalse(elem.$("h2").getText().contains(podegen)));
    }
}