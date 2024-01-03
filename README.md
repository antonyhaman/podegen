 ![Static Badge](https://img.shields.io/badge/license-Apache%202.0-green)   ![Static Badge](https://img.shields.io/badge/Java-17-green) 


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
  protected SelenideElement searchInputField;

  @FindBy(
      id = "search_button"
  )
  protected SelenideElement submitButton;

  @FindBy(
      xpath = "//section/ol[@class='react-results--main']/li[@data-layout='organic']"
  )
  protected ElementsCollection searchResultsList;

  public static SearchResultsPage getPage() {
    return com.codeborne.selenide.Selenide.page(SearchResultsPage.class);
  }

  public SelenideElement getSearchInputField() {
    return searchInputField;
  }

  public SelenideElement getSubmitButton() {
    return submitButton;
  }

  public ElementsCollection getSearchResultsList() {
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

Add the following dependency into your POM.xml:
```xml
<dependency>
   <groupId>io.github.antonyhaman</groupId>
   <artifactId>podegen-core</artifactId>
   <version>1.0.0</version>
   <scope>test</scope>
</dependency>
```

Also, it's recommended to add the following maven plugin into your `<build>` section:
```xml
<plugins>
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>build-helper-maven-plugin</artifactId>
        <version>3.0.0</version>
        <executions>
            <execution>
                <id>add-source</id>
                <phase>generate-sources</phase>
                <goals>
                    <goal>add-source</goal>
                </goals>
                <configuration>
                    <sources>
                        <source>${basedir}/target/generated-test-sources/test-annotations/</source>
                    </sources>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
```

## How to use?

1. Add `@PageObject` annotation to your configuration class (usually it's something called BaseTest or something)
`@PageObject` annotation has 4 optional parameters:
  	- **flavour** - `Selenium` (by default) or `Selenide`
  	- **strategy** - `Page Factory` (by default) or `Page Object`
  	- **prefix** - prefix for template files, if not specified `Podegen` will process all json and yaml files (not recommended)
  	- **packages** - packages where you want your page object classes generated for ex. `packages="com.example.somePackage"`, if empty `Podegen` will use packages of a class annotated with `@PageObject` annotation
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

   Where `elementName` would be used as a field name for the WebElement,
   `locatorType` may be any of the locator types supported by Selenium (id, name, css, xpath, etc),
   `locator` is the locator itself.

3. Enable annotation processing in your IDE:

    #### Intellij Idea
      Press Ctrl + Alt + S to open the IDE settings and then select Build, Execution, Deployment | Compiler | Annotation Processors and then tick 'Enable annotation processing' checkbox

    #### Eclipse
     [Please refer to this guide](https://shorturl.at/goEJ9)
   
4. Now you can build your project and find your generated page object classes in `target -> generated-test-sources -> test-annotations`
