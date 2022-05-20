package tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;

import org.json.simple.JSONObject;

public class CategoriesTests {
	private static ResponseSpecification responsespec;

	@BeforeClass
	public void setup() {
		baseURI = "http://localhost:3030";
		ResponseSpecBuilder builder = new ResponseSpecBuilder();
		builder.expectHeader("Content-Type", "application/json; charset=utf-8");
		builder.expectContentType("application/json");
		responsespec = builder.build();
	}

	@Test(description = "Check that the response status code is 200 when sending API GET/categories request ")
	public void CTT_GET_status_code() {
		given().
			get("/categories").
		then().
			spec(responsespec).
			statusCode(200).
			log().all();
	}
	
	@Test(description = "Check that the total number of products is an integer when sending API GET/categories request ")
	public void CTT_GET_All_ID_Numbers() {
		given().
			get("/categories").
		then().
			spec(responsespec).
			statusCode(200).
			body("total",equalTo(4307)).
			log().all();
	}

	@Test(description = "Check that the length of data is equal to the limit MAX number 25 when sending API GET/categories request with limit parameter 25")
	public void CTT_GET_Default_Limit() {
		given().
			get("/categories").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(10)).
			log().all();
	}
	
	@Test(description = "Check that the length of data is equal to the limit MAX number 25 when sending API GET/categories request with limit parameter 35")
	public void CTT_GET_Limit_MAX() {
		given().
			get("/categories?$limit=25").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(25)).
			log().all();
	}
	
	@Test(description = "Check that the length of data is equal to the limit Default number 10 when sending API GET/categories request with limit parameter 0")
	public void CTT_GET_Limit_Exceed_MAX() {
		given().
			get("/categories?$limit=35").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(25)).
			log().all();
	}
	@Test(description = "Check that the length of data is equal to the limit Default number 10 when sending API GET/categories request with limit parameter 0")
	public void CTT_GET_Limit_MIN() {
		given().
			get("/categories?$limit=0").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(0)).
			log().all();
	}
	
	@Test(description = "Check that response error message when sending API GET/categories request with string limit parameter q")
	public void CTT_GET_String_Limit() {
		given().
			get("/categories?$limit=q").
		then().
			spec(responsespec).
			statusCode(500).
			body("message", equalTo("SQLITE_ERROR: no such column: NaN")).
			log().all();
	}
	
	@Test(description = "Check that some of data are skipped in response when sending API GET/categories request with SKIP parameter less than the max IDs")
	public void CTT_GET_Skip() {
		given().
			get("/categories?$skip=4305").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(2)).
			log().all();
	}
	@Test(description = "Check that All data are skipped in response when sending API GET/categories request with SKIP parameter equal max IDs")
	public void CTT_GET_Skip_MAX() {
		given().
			get("/categories?$skip=4307").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(0)).
			log().all();
	}
	
	@Test(description = "Check that All data are skipped in response when sending API GET/categories request with SKIP parameter more than the max IDs")
	public void CTT_GET_Skip_Exceed_MAX() {
		given().
			get("/categories?$skip=4308").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(0)).
			log().all();
	}
	
	@Test(description = "Check that no data are skipped in response and return 10 as the default limit when sending API GET/categories request with SKIP parameter 0")
	public void CTT_GET_Skip_MIN() {
		given().
			get("/categories?$skip=0").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(10)).
			log().all();
	}
	
	@Test(description = "Check that response error message in response when sending API GET/categories request with String SKIP parameter q")
	public void CTT_GET_String_Skip() {
		given().
			get("/categories?$skip=q").
		then().
			spec(responsespec).
			statusCode(500).
			body("message", equalTo("SQLITE_ERROR: no such column: NaN")).
			log().all();
	}
	
	@Test(description = "Check that the response status code is 200 when sending API GET/Categories/validID")
	public void CTT_GET_ID() {
		given().
			get("/categories/pcmcat84000050001").
		then().
			spec(responsespec).
			statusCode(200).
			body("id", equalTo("pcmcat84000050001")).
			log().all();
	}
	
	@Test(description = "Check that the response status code is 404 when sending API GET/Categories/invalidID")
	public void CTT_GET_status_code_Not_exist_id() {
		given().
			get("/categories/993223").
		then().
			spec(responsespec).
			statusCode(404).
			log().all();
	}
	
	@Test(description = "Check that the error message in response when sending API GET/Categories/invalidID")
	public void CTT_GET_Message_Not_exist_id() {
		given().
			get("/categories/993223").
		then().
			spec(responsespec).
			statusCode(404).
			body("message",equalTo("No record found for id '993223'")).
			log().all();
	}
	
	//***************************************POST*********************************************
	@Test(description = "Check that the response status code is 200 when sending API POST/categories request with valid data")
	public void CTT_POST_Valid_Request() {
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("id", "555");
		map.put("name", "Categorytest");
		JSONObject request = new JSONObject(map);
		
		given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			body(request.toJSONString()).
		when().
			post("/categories").
		then().
			statusCode(201);
		
		given().
			get("/categories/?name=Categorytest").
		then().
			statusCode(200).
			body("data.size()", equalTo(1)).log().all();
		
		given().
			delete("/categories/?name=Categorytest").
		then().
			statusCode(200);
		

		given().
			get("/categories/?name=Categorytest").
		then().
			statusCode(200).
			body("data.size()", equalTo(0)).log().all();
	}
	
	@Test(description = "Check that the response status code is 400 when sending API POST/categories request with no ID")
	public void CTT_POST_Request_NO_ID() {
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("name", "Categorytest");
		JSONObject request = new JSONObject(map);
		
		given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			body(request.toJSONString()).
		when().
			post("/categories").
		then().
			statusCode(400);
	}
	
	@Test(description = "Check that the response status code is 400 when sending API POST/categories request with no name")
	public void CTT_POST_Request_NO_Name() {
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("id", "556");
		JSONObject request = new JSONObject(map);
		
		given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			body(request.toJSONString()).
		when().
			post("/categories").
		then().
			statusCode(400);
	}
	
	@Test(description = "Check that the response status code is 400 when sending API POST/categories request with no data")
	public void CTT_POST_Request_NO_DATA() {
		given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
		when().
			post("/categories").
		then().
			statusCode(400);
	}
	
	@Test(description = "Check that the response status code is 400 when sending API POST/categories request with existing ID")
	public void CTT_POST_Request_Exist_ID() {
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("id", "pcmcat84000050001");
		map.put("name", "Categorytest");
		JSONObject request = new JSONObject(map);
		
		given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			body(request.toJSONString()).
		when().
			post("/categories").
		then().
			statusCode(400);
	
	}
	
	@Test(description = "Check that the response status code is 200 when sending API DELETE/categories request with valid id")
	void CTT_DELETE_Request_VALID() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("id", "555");
		map.put("name", "Categorytest");
		JSONObject request = new JSONObject(map);
		
		given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			body(request.toJSONString()).
		when().
			post("/categories").
		then().
			statusCode(201);
		
		given().
			get("/categories/?name=Categorytest").
		then().
			statusCode(200).
			body("data.size()", equalTo(1)).log().all();
		
		given().
			delete("/categories/?name=Categorytest").
		then().
			statusCode(200);
		

		given().
			get("/categories/?name=Categorytest").
		then().
			statusCode(200).
			body("data.size()", equalTo(0)).log().all();
		}
	
	@Test(description = "Check that the response status code is 404 when sending API DELETE/categories request with unknown id")
	void CTT_DELETE_Request_INVALID_ID() {
		given().
			delete("/categories/1111111111").
		then().
			statusCode(404).log().all();

	}
	
	//*********************************PatchMethod************************************
	@Test(description = "Check that the response status code is 200 when sending API PATCH/Categories request with Valid ID")
	public void CTT_PATCH_Request_VALID() {
		String beforemodificationid;
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("id", "555");
		map.put("name", "BeforemodificationCategorytest");
		JSONObject request = new JSONObject(map);
		
		//post to create ID with value 555
		Response response = given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			body(request.toJSONString()).
		when().
			post("/categories");
		
		//save ID 555 in variable
		beforemodificationid = response.jsonPath().getString("id");
		
		map.put("id", "555");
		map.put("name", "AftermodificationCategorytest");	
		request = new JSONObject(map);
		
		//Send patch request to modify the name of ID 555
		given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			body(request.toJSONString()).
		when().
			patch("/stores/"+beforemodificationid).
		then().
			statusCode(200).
			body("name", equalTo("AftermodificationCategorytest")).
			log().all();
		
		//Delete the modified ID
		given().
			delete("/categories/"+beforemodificationid).
		then().
			statusCode(200);
	}
	
	@Test(description = "Check that the response status code is 404 when sending API PATCH/Categories request with InValid ID")
	public void CTT_PATCH_Request_InvalidID() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("id", "555");
		map.put("name", "AftermodificationCategorytest");	
		JSONObject request = new JSONObject(map);
		
		//Send patch request to modify the name of ID 555
		given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
			body(request.toJSONString()).
		when().
			patch("/categories/555").
		then().
			statusCode(404).
			log().all();
	}
	
	@Test(description = "Check that the response status code is 404 when sending API PATCH/Categories request with InValid ID")
	public void CTT_PATCH_Request_stringID() {	
		given().
			header("Content-Type", "application/json").
			contentType(ContentType.JSON).
			accept(ContentType.JSON).
		when().
			patch("/categories/555").
		then().
			statusCode(404).
			log().all();
	}
}
