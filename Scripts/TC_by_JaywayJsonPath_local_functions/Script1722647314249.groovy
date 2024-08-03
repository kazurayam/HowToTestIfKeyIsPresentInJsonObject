import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path dataFile = projectDir.resolve("./data.json")
String dataText = Files.readString(dataFile)

DocumentContext dc = JsonPath.parse(dataText)


verifyValueEquals(dc, "\$.body.user.firstName", "James");

verifyObjectLength(dc, "\$.body.user", 6);

verifyKeyPresent(dc, "\$.body.user", "firstName")

verifyKeyPresent(dc, "\$.body.user", "nickName", FailureHandling.OPTIONAL)


def verifyValueEquals(DocumentContext dc, String jsonPath, String expected,
		FailureHandling flowControl = FailureHandling.CONTINUE_ON_FAILURE) {
	def result = (String)dc.read(jsonPath);
	if (result != expected) {
		log("${jsonPath} is ${result}, not ${expected}", flowControl)
	}
}

def verifyObjectLength(DocumentContext dc, String jsonPathForObject, int expected,
		FailureHandling flowControl = FailureHandling.CONTINUE_ON_FAILURE) {
	def result = (Integer)dc.read(jsonPathForObject + ".length()");
	if (result != expected) {
		log("${jsonPathForObject} is ${result}, not ${expected}", flowControl)
	}
}

def verifyKeyPresent(DocumentContext dc, String jsonPathForObject, String key,
		FailureHandling flowControl = FailureHandling.CONTINUE_ON_FAILURE) {
	def keySet = (Set)dc.read(jsonPathForObject + ".keys()")
	if (!keySet.contains(key)) {
		log("${jsonPathForObject + '.keys()'} ${keySet} does not contain ${key}", flowControl)
	}
}

def log(String message, FailureHandling flowControl) {
	switch (flowControl) {
		case FailureHandling.STOP_ON_FAILURE:
			KeywordUtil.markFailedAndStop(message)
			break
		case FailureHandling.CONTINUE_ON_FAILURE:
			KeywordUtil.markFailed(message)
			break
		case FailureHandling.OPTIONAL:
			KeywordUtil.markWarning(message)
			break
	}
}