package apipack;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

public class BugTest {

	public static void main(String[] args) {

		RestAssured.baseURI="https://keerthanap773.atlassian.net/";
		
		String createIssueResponse =given()
		.header("Content-type","application/json")
		.header("Authorization","Basic a2VlcnRoYW5hcDc3M0BnbWFpbC5jb206QVRBVFQzeEZmR0YwZlJTWVhCR1plaWowRHZiMjVZNEVQTkVyanpDLVJ4eVptdGdYRzdma05vWjZyNl9mbERGUWl1U1VVY19RU0hSNU8tR19tS2RJQVFPNXRYUHFXNTNKY2FFUkY3UVZLRjRmVGVFLXJSRVpNUlBubnM5WjlHOERaOEpJYndmNlQ1WURKOWpsSFhHVzdRbFJabnZDRU5NZnFKOVM5ZEVsYmQ3S2RUWlhBc1U0QUJZPTkzMUZFNzA0")
		.body("{\r\n"
				+ "    \"fields\": {\r\n"
				+ "       \"project\":\r\n"
				+ "       {\r\n"
				+ "          \"key\": \"PKJIR\"\r\n"
				+ "       },\r\n"
				+ "       \"summary\": \"Menu items are not working-automation Rest Assured\",\r\n"
				+ "       \"issuetype\": {\r\n"
				+ "          \"name\": \"Bug\"\r\n"
				+ "       }\r\n"
				+ "   }\r\n"
				+ "}")
		.log().all()
		.post("rest/api/3/issue").then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		
		JsonPath js = new JsonPath(createIssueResponse);
		String issueId=js.getString("id");
		System.out.println(issueId);
		
		//Add attachment
		
		given()
		.pathParam("key", issueId)
		.header("X-Atlassian-Token","no-check")
		.header("Authorization","Basic a2VlcnRoYW5hcDc3M0BnbWFpbC5jb206QVRBVFQzeEZmR0YwZlJTWVhCR1plaWowRHZiMjVZNEVQTkVyanpDLVJ4eVptdGdYRzdma05vWjZyNl9mbERGUWl1U1VVY19RU0hSNU8tR19tS2RJQVFPNXRYUHFXNTNKY2FFUkY3UVZLRjRmVGVFLXJSRVpNUlBubnM5WjlHOERaOEpJYndmNlQ1WURKOWpsSFhHVzdRbFJabnZDRU5NZnFKOVM5ZEVsYmQ3S2RUWlhBc1U0QUJZPTkzMUZFNzA0")
		.multiPart("file",new File("C:/Users/keert/Downloads/APIDocs/JiraAPIAutomationAddAttachment.png")).log().all()
		.post("rest/api/2/issue/{key}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
		
	}

}
