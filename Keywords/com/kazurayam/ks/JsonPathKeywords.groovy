package com.kazurayam.ks

import com.jayway.jsonpath.DocumentContext
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil

class JsonPathKeywords {
	
	static def verifyValueEquals(DocumentContext dc, String jsonPath, String expected,
			FailureHandling flowControl = FailureHandling.CONTINUE_ON_FAILURE) {
		def result = (String)dc.read(jsonPath);
		if (result != expected) {
			log("${jsonPath} is ${result}, not ${expected}", flowControl)
		}
	}

	static def verifyObjectLength(DocumentContext dc, String jsonPathForObject, int expected,
			FailureHandling flowControl = FailureHandling.CONTINUE_ON_FAILURE) {
		def result = (Integer)dc.read(jsonPathForObject + ".length()");
		if (result != expected) {
			log("${jsonPathForObject} is ${result}, not ${expected}", flowControl)
		}
	}

	static def verifyKeyPresent(DocumentContext dc, String jsonPathForObject, String key,
			FailureHandling flowControl = FailureHandling.CONTINUE_ON_FAILURE) {
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