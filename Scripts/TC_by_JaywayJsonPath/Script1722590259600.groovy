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
if (!keySet.contains("firstName")) {
	KeywordUtil.markFailed("\$.body.user.keys() ${keySet} does not contain firstName")
}

keySet = (Set)dc.read("\$.body.user.keys()")
if (!keySet.contains("nickName")) {
	KeywordUtil.markWarning("\$.body.user.keys() ${keySet} does not contain nickName but it doesnt matter much")
}
