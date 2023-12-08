package com.github.kotvertolet;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TestPageFactory {

    @FindBy(id = "login_field")
    private WebElement login;

    public TestPageFactory(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public WebElement getLogin() {
        return login;
    }


}
