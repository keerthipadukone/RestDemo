package apipack;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static  org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.ReUsuableMethods;
import files.payload;

public class Basics {

	public static void main(String[] args) {
		
//validate if Add Place API is working as expected
		//given - all input details
		//when - submit the API - resource, http method
		//Then - validate the response
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response =given().log().all().queryParam("key", "qaclick123").header("Content-type","application/json")
		.body(payload.AddPlace()).when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server","Apache/2.4.52 (Ubuntu)").extract().response().asString();
		
		System.out.println(response);
		JsonPath js=new JsonPath(response); //for parsing json
		String placeId = js.getString("place_id");
		System.out.println(placeId);
		
		//Add place ->Update Place with New Address -> Get Place to validate
		//if new address is present in response
		
		//Update Place
		String newAddress = "Summer walk, Africa";
		given().log().all().queryParam("key", "qaclick123").header("Content-type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}").when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Place
		String getPlaceResponse=given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id",placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().asString();
		
		JsonPath js1 = ReUsuableMethods.rawToJson(getPlaceResponse);
		String actualAddress=js1.getString("address");
		System.out.println(actualAddress);
		//Cucumber Junit, TestNG
		Assert.assertEquals(actualAddress,newAddress);
		
	}

}
