![Release](https://jitpack.io/v/kotvertolet/podegen.svg)   ![Static Badge](https://img.shields.io/badge/license-Apache%202.0-green)


# Podegen - Page Object Code Generation for Selenium and Selenide in Java

Podegen is a Java library intended for generating Page Object classes out of yaml or json templates files,
it supports both Selenium and Selenide, it also comes with two variants of the Page object.

## How to install?

## How to use?

### Yaml

```yaml
- elementName:
  locatorType: "locator"
```

### Json
```json
[
  {
    "elementName": {
      "locatorType": "locator"
    }
  }
]
```

Where `elementName` would be used as a field name,
`locatorType` may be any of the locator types supported by Selenium (id, name, xpath, etc),
`locator` is the locator itself.

