import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.kazurayam.ks.JsonPathKeywords as JP
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path dataFile = projectDir.resolve("./data.json")
String dataText = Files.readString(dataFile)

DocumentContext dc = JsonPath.parse(dataText)

JP.verifyValueEquals(dc, "\$.body.user.firstName", "James");

JP.verifyObjectSize(dc, "\$.body.user", 6);

JP.verifyKeyPresent(dc, "\$.body.user", "firstName")

JP.verifyKeyPresent(dc, "\$.body.user", "nickName", FailureHandling.OPTIONAL)
