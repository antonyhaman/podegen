package com.github.kotvertolet.podegen.tests;


import com.github.kotvertolet.podegen.core.annotations.PageObject;
import com.github.kotvertolet.podegen.core.data.enums.Flavours;
import com.github.kotvertolet.podegen.core.data.enums.Strategies;
import com.github.kotvertolet.podegen.tests.pageObjects.duckduckgo.SearchResultsPage;
import com.github.kotvertolet.podegen.tests.pageObjects.duckduckgo.StartPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@PageObject(flavour = Flavours.Selenide, strategy = Strategies.PageFactory)
public class Tests {

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