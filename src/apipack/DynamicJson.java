package apipack;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static  org.hamcrest.Matchers.*;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.ReUsuableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {
	
	@Test(dataProvider="BooksData")
	public void addBook(String isbn,String aisle)
	{
		RestAssured.baseURI="http://216.10.245.166";
		String response=given().header("Content-type","application/json").
		body(payload.Addbook(isbn,aisle)).
		when()
		.post("/Library/Addbook.php")
		.then().log().all().assertThat().statusCode(200)
		.extract().response().asString();
		JsonPath js=ReUsuableMethods.rawToJson(response);
		String id = js.get("ID");
		System.out.println(id);
		
		//getBook
		String getBookResponse=given().log().all().queryParam("ID",id)
		.when().get("/Library/GetBook.php")
		.then().assertThat().log().all().statusCode(200).extract().asString();
		System.out.println(getBookResponse);
		
		//deleteBook
		String deleteResponse = 
		given().log().all().header("Content-type","application/json")
		.body("{\r\n"
				+ "    \"ID\": \""+id+"\r\n"
				+ "}")
		.when().post("/Library/DeleteBook.php")
		.then().assertThat().log().all().statusCode(200)
		//.body("msg", equalTo("book is successfully deleted"));
		.extract().asString();		
		System.out.println(deleteResponse);
	}
	
	@DataProvider(name="BooksData")
	public Object[][] getData()
	{
		return new Object[][] {{"kmk","24278"},{"opnkn","79898"},{"juk","8976"}};
	}

}
