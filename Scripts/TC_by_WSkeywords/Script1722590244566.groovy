import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path dataFile = projectDir.resolve("./data.json")
String dataText = Files.readString(dataFile)

ResponseObject response = new ResponseObject(dataText)

WS.verifyElementPropertyValue(response, 'body.user.firstName', "James")
WS.verifyElementsCount(response, 'body.user', 6)

// The following keyword is not supported
//WS.verifyNodePresent(response, 'body.user.firstName', true)
