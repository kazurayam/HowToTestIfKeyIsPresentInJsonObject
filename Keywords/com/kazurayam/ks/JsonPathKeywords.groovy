package com.kazurayam.ks

import com.jayway.jsonpath.DocumentContext
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil

class JsonPathKeywords {

	static def verifyValueEquals(DocumentContext dc, String jsonPath, Object expected,
			FailureHandling flowControl = RunConfiguration.getDefaultFailureHandling()) {
		def result = dc.read(jsonPath).toString();
		if (result != expected.toString()) {
			log("${jsonPath} is ${result}, not ${expected}", flowControl)
		}
	}

	static def verifyObjectSize(DocumentContext dc, String jsonPathForObject, int expected,
			FailureHandling flowControl = RunConfiguration.getDefaultFailureHandling()) {
		def result = (Integer)dc.read(jsonPathForObject + ".length()");
		if (result != expected) {
			log("${jsonPathForObject} is ${result}, not ${expected}", flowControl)
		}
	}

	static def verifyKeyPresent(DocumentContext dc, String jsonPathForObject, String key,
			FailureHandling flowControl = RunConfiguration.getDefaultFailureHandling()) {
		def keySet = (Set)dc.read(jsonPathForObject + ".keys()")
		if (!keySet.contains(key)) {
			log("${jsonPathForObject + '.keys()'} ${keySet} does not contain ${key}", flowControl)
		}
	}

	static private def log(String message, FailureHandling flowControl) {
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
}