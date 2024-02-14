[![Maven Central](https://github.com/antonyhaman/podegen/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/antonyhaman/podegen/actions/workflows/maven-publish.yml) ![Static Badge](https://img.shields.io/badge/license-Apache%202.0-green)   ![Static Badge](https://img.shields.io/badge/Java-17-green) 


# Podegen - Page Object Code Generation for Selenium and Selenide in Java

Podegen is a Java library for generating page object classes from yaml or json template files. 
It supports both Selenium and Selenide and has two variants of the page object. This is how it works:

Template file like this:
```yaml
- searchInputField:
    name: "q"
- submitButton:
    id: "search_button"
- $$searchResultsList:
    xpath: "//section/ol[@class='react-results--main']/li"
```
Creates the following page object class:

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
   <version>1.1.1</version>
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

1. Add the `@PageObject` annotation to your configuration class (usually a class called BaseTest or something similar). 
The `@PageObject` annotation has 4 optional parameters:
  	- **flavour** - `Selenium` (by default) or `Selenide`
  	- **strategy** - `Page Factory` (by default) or `Page Object`
  	- **prefix** - prefix for template files, if not specified, Podegen will process all json and yaml files (not recommended)
  	- **packages** - packages in which the classes for the page objects should be generated, e.g. `packages="com.example.somePackage"`,
      if empty, Podegen will use the packages of a class provided with the `@PageObject` annotation. 
      Note that the actual classes are generated in `target/test-annotations` in any case, but the classes have the package you specify,
      so you can import them into your classes as if it were a class in your project.

```java
@PageObject(flavour = Flavours.Selenide, strategy = Strategies.PageFactory, prefix = "PO_", packages = "com.example.test")
```

2. Put page object template files into `resources` folder of the same source root as the class annotated with `@PageObject`, for example, if the class annotated with `@PageObject` is located in the `src/main` directory, then the page object template files should be placed in the `src/main/resources` directory. Page object template files should be in the following format:

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

Where:
   - `elementName` would be used as the field name for the `WebElement`/`SelenideElement` (for Selenium and Selenide flavour accordingly) 
or if you add `$$` as a prefix to the element name, it will be `List<WebElement>`/`ElementsCollection`
   - `locatorType` can be one of the locator types supported by Selenium (id, name, css, xpath, etc.)
   - `locator` is the locator itself.

3. Enable annotation processing in your IDE:

    #### Intellij Idea
      Press Ctrl + Alt + S to open the IDE settings and then select Build, Execution, Deployment | Compiler | Annotation Processors and then tick 'Enable annotation processing' checkbox

    #### Eclipse
     [Please refer to this guide](https://shorturl.at/goEJ9)
   
4. Now you can build your project and find your generated page object classes in `target -> generated-test-sources -> test-annotations`
