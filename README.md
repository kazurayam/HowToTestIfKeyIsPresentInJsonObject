# [Katalon Studio] How to test if a key is present in a JSON object in a Web Service Response

## Problem to solve

In the Katalon user forum, there was an old post in May 2019

- https://forum.katalon.com/t/how-to-verify-a-json-node-is-present/26244

which has not been answered yet until Aug 2024.

The original poster had a Web Service Response as follows:

```
{
  "body": {
    "user": {
      "surname": "Morris",
      "firstName": "James",
      "dateOfBirth": "1983-04-19",
      "titleCode": "Mr",
      "middleName": "P",
      "genderCode": "male"
    }
  }
}
```

The originator asked

>Is there any method to check if a specific node is present using katalon inbuild method, something like `WS.verifyNodePresent(response, 'body.user.firstName', true)`

Katalon Studio does not support `WS.verifyNodePresent` keyword.

The originator has got no answer from the Katalon forum. So, let me ask the same question again. Is there any way to test if a key is present in a JSON object?

## Solution proposed

Recenty I learned [Jayway JsonPath](https://github.com/json-path/JsonPath). I found that Jayway JsonPath can solve the problem nicely. So I created a sample code and will show it here.

## Description

### The problem reproduced

See [Test Cases/TC_by_WSkeywords](https://github.com/kazurayam/HowToVerifyJsonIfObjectKeyIsPresent/blob/master/Scripts/TC_by_WSkeywords/Script1722590244566.groovy). This code reproduces the problem that the original poster presented.

### The solution using Jayway JsonPath

See [Test Cases/TC_by_JaywayJsonPath](https://github.com/kazurayam/HowToVerifyJsonIfObjectKeyIsPresent/blob/master/Scripts/TC_by_JaywayJsonPath/Script1722590259600.groovy). Let me quote the source here for your reference:

```
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path dataFile = projectDir.resolve("./data.json")
String dataText = Files.readString(dataFile)

DocumentContext dc = JsonPath.parse(dataText)

def result = (String)dc.read("\$.body.user.firstName");
if (result != "James") {
	KeywordUtil.	markFailed("\$.body.user.firstName is ${result}, not James")
}

result = (Integer)dc.read("\$.body.user.length()");
if (result != 6) {
	KeywordUtil.markFailed("\$.body.user.length() is ${result}, not 6")
}

def keySet = (Set)dc.read("\$.body.user.keys()")
println "\$.body.user.keys(): ${keySet}"
if (!keySet.contains("firstName")) {
	KeywordUtil.markFailed("\$.body.user.keys() ${keySet} does not contain firstName")
}

keySet = (Set)dc.read("\$.body.user.keys()")
if (!keySet.contains("nickName")) {
	KeywordUtil.markWarning("\$.body.user.keys() ${keySet} does not contain nickName but it doesnt matter much")
}
```

When I ran this, I got the following output in the console.

```
2024-08-02 18:51:03.216 INFO  c.k.katalon.core.main.TestCaseExecutor   - --------------------
2024-08-02 18:51:03.231 INFO  c.k.katalon.core.main.TestCaseExecutor   - START Test Cases/TC_by_JaywayJsonPath
$.body.user.keys(): [surname, firstName, dateOfBirth, titleCode, middleName, genderCode]
2024-08-02 18:51:05.547 WARN  com.kms.katalon.core.util.KeywordUtil    - $.body.user.keys() [surname, firstName, dateOfBirth, titleCode, middleName, genderCode] does not contain nickName but it doesnt matter much
2024-08-02 18:51:05.596 INFO  c.k.katalon.core.main.TestCaseExecutor   - END Test Cases/TC_by_JaywayJsonPath
```

Please note that here I utilized the `keys()` function of JsonPath, which returns a Set of keys of a specified JSON object, as follows:

```
$.body.user.keys() [surname, firstName, dateOfBirth, titleCode, middleName, genderCode]
```

The `keys()` function helps solving a lot of issues regading verifying JSON. The `keys()` function is q unique feature of Jayway JsonPath in Java. Other implementations of [JsonPath](https://goessner.net/articles/JsonPath/) in JavaScript and Pathon do not have it.

### How to import the required dependencies

Katalon Studio does not bundle the jar of `Jayway JsonPath`. Therefore you need to import the necessary external dependencies yourself. Katalon's documentation [How to resolve external dependencyes using Gradle](https://docs.katalon.com/katalon-platform/plugins-and-add-ons/katalon-store/katalon-studio-plugins/how-to-resolve-external-dependencies-for-a-plugin-in-katalon-studio)

See the [build.gradle](https://github.com/kazurayam/HowToVerifyJsonIfObjectKeyIsPresent/blob/master/build.gradle) that I wrote.


## Conclusion

Katalon Studio provides a set of WS keywords that deal with JSON response as Web Service, for example [WS.verifyElementPropertyValue](https://docs.katalon.com/katalon-studio/keywords/keyword-description-in-katalon-studio/web-service-keywords/ws-verify-element-property-value). In my humble opinion, the coverage is very limitted. I do not expect the product's feature for JSON verification will be expanded in future. However, Jayway JsonPath is available to us. I believe it is well worth specing your time to learn.
