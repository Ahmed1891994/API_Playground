package tests;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

public class StoresTests {
	private static ResponseSpecification responsespec;

	@BeforeClass
	public void setup() {
		baseURI = "http://localhost:3030";
		ResponseSpecBuilder builder = new ResponseSpecBuilder();
		builder.expectHeader("Content-Type", "application/json; charset=utf-8");
		builder.expectContentType("application/json");
		responsespec = builder.build();
	}

	@Test(description = "Check that the response status code is 200 when sending API GET/stores request ")
	public void STT_GET_status_code() {
		given().
			get("/stores").
		then().
			spec(responsespec).
			statusCode(200).
			log().all();
	}
	
	@Test(description = "Check that the total number of stores is an integer when sending API GET/stores request ")
	public void STT_GET_All_ID_Numbers() {
		given().
			get("/stores").
		then().
			spec(responsespec).
			statusCode(200).
			body("total",equalTo(1564)).
			log().all();
	}

	@Test(description = "Check that the length of data is equal to the limit MAX number 25 when sending API GET/stores request with limit parameter 25")
	public void STT_GET_Default_Limit() {
		given().
			get("/stores").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(10)).
			log().all();
	}
	
	@Test(description = "Check that the length of data is equal to the limit MAX number 25 when sending API GET/stores request with limit parameter 35")
	public void STT_GET_Limit_MAX() {
		given().
			get("/stores?$limit=25").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(25)).
			log().all();
	}
	
	@Test(description = "Check that the length of data is equal to the limit Default number 10 when sending API GET/stores request with limit parameter 0")
	public void STT_GET_Limit_Exceed_MAX() {
		given().
			get("/stores?$limit=35").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(25)).
			log().all();
	}
	@Test(description = "Check that the length of data is equal to the limit Default number 10 when sending API GET/stores request with limit parameter 0")
	public void STT_GET_Limit_MIN() {
		given().
			get("/stores?$limit=0").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(0)).
			log().all();
	}
	
	@Test(description = "Check that response error message when sending API GET/stores request with string limit parameter q")
	public void STT_GET_String_Limit() {
		given().
			get("/stores?$limit=q").
		then().
			spec(responsespec).
			statusCode(500).
			body("message", equalTo("SQLITE_ERROR: no such column: NaN")).
			log().all();
	}
	
	@Test(description = "Check that some of data are skipped in response when sending API GET/stores request with SKIP parameter less than the max IDs")
	public void STT_GET_Skip() {
		given().
			get("/stores?$skip=1562").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(5)).
			log().all();
	}
	@Test(description = "Check that All data are skipped in response when sending API GET/stores request with SKIP parameter equal max IDs")
	public void STT_GET_Skip_MAX() {
		given().
			get("/stores?$skip=1564").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(0)).
			log().all();
	}
	
	@Test(description = "Check that All data are skipped in response when sending API GET/stores request with SKIP parameter more than the max IDs")
	public void STT_GET_Skip_Exceed_MAX() {
		given().
			get("/stores?$skip=1565").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(0)).
			log().all();
	}
	
	@Test(description = "Check that no data are skipped in response and return 10 as the default limit when sending API GET/stores request with SKIP parameter 0")
	public void STT_GET_Skip_MIN() {
		given().
			get("/stores?$skip=0").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(10)).
			log().all();
	}
	
	@Test(description = "Check that response error message in response when sending API GET/stores request with String SKIP parameter q")
	public void STT_GET_String_Skip() {
		given().
			get("/stores?$skip=q").
		then().
			spec(responsespec).
			statusCode(500).
			body("message", equalTo("SQLITE_ERROR: no such column: NaN")).
			log().all();
	}
	
	@Test(description = "Check that the response status code is 200 when sending API GET/stores/validID")
	public void STT_GET_ID() {
		given().
			get("/stores/11").
		then().
			spec(responsespec).
			statusCode(200).
			body("id", equalTo(11)).
			log().all();
	}
	
	@Test(description = "Check that the response status code is 404 when sending API GET/stores/invalidID")
	public void STT_GET_status_code_Not_exist_id() {
		given().
			get("/stores/993223").
		then().
			spec(responsespec).
			statusCode(404).
			log().all();
	}
	
	@Test(description = "Check that the error message in response when sending API GET/stores/invalidID")
	public void STT_GET_Message_Not_exist_id() {
		given().
			get("/stores/993223").
		then().
			spec(responsespec).
			statusCode(404).
			body("message",equalTo("No record found for id '993223'")).
			log().all();
	}
	
	//***************************************POST*********************************************
		@Test(description = "Check that the response status code is 201 when sending API POST/stores request with valid data")
		public void STT_POST_Valid_Request() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim__store");
			request.put("type", "HardGood");
			request.put("address", "miami");
			request.put("address2", "miami2");
			request.put("city", "Alex");
			request.put("state", "montazah");
			request.put("zip", "Hakim");
			request.put("lat", 158);
			request.put("lng", 159);
			request.put("hours", "10 to 10");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/stores").
			then().
				statusCode(201);
			
			given().
				get("/stores/?name=Hakim__store").
			then().
				statusCode(200).
				body("data.size()", equalTo(1)).log().all();
			
			given().
				delete("/stores/?name=Hakim__store").
			then().
				statusCode(200);
			

			given().
				get("/stores/?name=Hakim__store").
			then().
				statusCode(200).
				body("data.size()", equalTo(0)).log().all();
		}
		
		@Test(description = "Check that the response status code is 400 when sending API POST/stores request with no name")
		public void STT_POST_Request_NO_Name() {
			JSONObject request = new JSONObject();

			//request.put("name", "Hakim__store");
			request.put("type", "HardGood");
			request.put("address", "miami");
			request.put("address2", "miami2");
			request.put("city", "Alex");
			request.put("state", "montazah");
			request.put("zip", "Hakim");
			request.put("lat", 158);
			request.put("lng", 159);
			request.put("hours", "10 to 10");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/stores").
			then().
				statusCode(400);
		}
		
		
		@Test(description = "Check that the response status code is 400 when sending API POST/stores request with no DATA")
		public void STT_POST_Request_NO_DATA() {

			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
			when().
				post("/stores").
			then().
			statusCode(400);
		}
		
		@Test(description = "Check that the response status code is 200 when sending API DELETE/stores request with valid id")
		void STT_DELETE_Request_VALID() {
			JSONObject request = new JSONObject();

			request.put("name", "Delete_Hakim__store");
			request.put("type", "HardGood");
			request.put("address", "miami");
			request.put("address2", "miami2");
			request.put("city", "Alex");
			request.put("state", "montazah");
			request.put("zip", "Hakim");
			request.put("lat", 158);
			request.put("lng", 159);
			request.put("hours", "10 to 10");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/stores").
			then().
				statusCode(201);
			
			given().
				get("/stores/?name=Delete_Hakim__store").
			then().
				statusCode(200).
				body("data.size()", equalTo(1)).log().all();
			
			given().
				delete("/stores/?name=Delete_Hakim__store").
			then().
				statusCode(200);
			

			given().
				get("/stores/?name=Delete_Hakim__store").
			then().
				statusCode(200).
				body("data.size()", equalTo(0)).log().all();	
			}
		
		@Test(description = "Check that the response status code is 404 when sending API DELETE/stores request with unknown id")
		void STT_DELETE_Request_INVALID_ID() {
			given().
				delete("/stores/1111111111").
			then().
				statusCode(404).log().all();

		}
}
