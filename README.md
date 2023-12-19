![Release](https://jitpack.io/v/kotvertolet/podegen.svg)   ![Static Badge](https://img.shields.io/badge/license-Apache%202.0-green)


# Podegen - Page Object Code Generation for Selenium and Selenide in Java

Podegen is a Java library intended for generating Page Object classes out of yaml or json templates files,
it supports both Selenium and Selenide, it also comes with two variants of the Page object. Let's how it works:

Template file like this:
```yaml
- searchInputField:
    name: "q"
- submitButton:
    id: "search_button"
- $$searchResultsList:
    xpath: "//section/ol[@class='react-results--main']/li"
```
Will produce the following page object class:

```java
public class SearchResultsPage {
  @FindBy(
      name = "q"
  )
  protected WebElement searchInputField;

  @FindBy(
      id = "search_button"
  )
  protected WebElement submitButton;

  @FindBy(
      xpath = "//section/ol[@class='react-results--main']/li"
  )
  protected List<WebElement> searchResultsList;

  public SearchResultsPage(WebDriver driver) {
    PageFactory.initElements(driver, this);
  }

  public WebElement getSearchInputField() {
    return searchInputField;
  }

  public WebElement getSubmitButton() {
    return submitButton;
  }

  public List<WebElement> getSearchResultsList() {
    return searchResultsList;
  }
}

```
<details>
  <summary>Or if you don't like Page Factory approach...</summary>
  ...you can also enjoy 'classic' Page Object: 

```java
public class SearchResultsPage {
  protected By searchInputField = By.name("q");

  protected By submitButton = By.id("search_button");

  protected By searchResultsList = By.xpath("//section/ol[@class='react-results--main']/li");

  protected WebDriver driver;

  public SearchResultsPage(WebDriver driver) {
    this.driver = driver;
  }

  public WebElement getSearchInputField() {
    return driver.findElement(searchInputField);
  }

  public WebElement getSubmitButton() {
    return driver.findElement(submitButton);
  }

  public List<WebElement> getSearchResultsList() {
    return driver.findElements(searchResultsList);
  }
}
```
</details>

## How to install?

Add this to your pom file:
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

 Now you can add `podegen` dependency to your `<dependencies>` block:
```xml 
 	<dependency>
	    <groupId>com.github.kotvertolet.podegen</groupId>
	    <artifactId>podegen-core</artifactId>
	    <version>v1.0.0</version>
	</dependency>
```

## How to use?

1. Add `@PageObject` annotation to your configuration class (usually it's something called BaseTest or something)
`@PageObject` annotation has 3 optional parameters:
  - flavour - `Selenium` (by default) or `Selenide`
  - strategy - `Page Factory` (by default) or `Page Object`
  - prefix - prefix for template files, if not specified `podegen` will process all json and yaml files (not recommended)
2. Put page object template template files into your `resources` folder in the following format:

<details>
  <summary>Yaml</summary>
  
```yaml
      - elementName:
        locatorType: "locator"
```
  
</details>

<details>
  <summary>Json</summary>
  
```json
[
  {
    "elementName": {
      "locatorType": "locator"
    }
  }
]
```

</details>

Where `elementName` would be used as a field name,
`locatorType` may be any of the locator types supported by Selenium (id, name, css, xpath, etc),
`locator` is the locator itself.

