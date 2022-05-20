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

public class ProductsTests {
	private static ResponseSpecification responsespec;

	@BeforeClass
	public void setup() {
		baseURI = "http://localhost:3030";
		ResponseSpecBuilder builder = new ResponseSpecBuilder();
		builder.expectHeader("Content-Type", "application/json; charset=utf-8");
		builder.expectContentType("application/json");
		responsespec = builder.build();
	}

	@Test(description = "Check that the response status code is 200 when sending API GET/products request ")
	public void PDT_GET_status_code() {
		given().
			get("/products").
		then().
			spec(responsespec).
			statusCode(200).
			log().all();
	}
	
	@Test(description = "Check that the total number of products is an integer when sending API GET/products request ")
	public void PDT_GET_All_ID_Numbers() {
		given().
			get("/products").
		then().
			spec(responsespec).
			statusCode(200).
			body("total",equalTo(51957)).
			log().all();
	}

	@Test(description = "Check that the length of data is equal to the limit MAX number 25 when sending API GET/products request with limit parameter 25")
	public void PDT_GET_Default_Limit() {
		given().
			get("/products").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(10)).
			log().all();
	}
	
	@Test(description = "Check that the length of data is equal to the limit MAX number 25 when sending API GET/products request with limit parameter 35")
	public void PDT_GET_Limit_MAX() {
		given().
			get("/products?$limit=25").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(25)).
			log().all();
	}
	
	@Test(description = "Check that the length of data is equal to the limit Default number 10 when sending API GET/products request with limit parameter 0")
	public void PDT_GET_Limit_Exceed_MAX() {
		given().
			get("/products?$limit=35").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(25)).
			log().all();
	}
	@Test(description = "Check that the length of data is equal to the limit Default number 10 when sending API GET/products request with limit parameter 0")
	public void PDT_GET_Limit_MIN() {
		given().
			get("/products?$limit=0").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(0)).
			log().all();
	}
	
	@Test(description = "Check that response error message when sending API GET/products request with string limit parameter q")
	public void PDT_GET_String_Limit() {
		given().
			get("/products?$limit=q").
		then().
			spec(responsespec).
			statusCode(500).
			body("message", equalTo("SQLITE_ERROR: no such column: NaN")).
			log().all();
	}
	
	@Test(description = "Check that some of data are skipped in response when sending API GET/products request with SKIP parameter less than the max IDs")
	public void PDT_GET_Skip() {
		given().
			get("/products?$skip=51955").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(2)).
			log().all();
	}
	@Test(description = "Check that All data are skipped in response when sending API GET/products request with SKIP parameter equal max IDs")
	public void PDT_GET_Skip_MAX() {
		given().
			get("/products?$skip=51957").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(0)).
			log().all();
	}
	
	@Test(description = "Check that All data are skipped in response when sending API GET/products request with SKIP parameter more than the max IDs")
	public void PDT_GET_Skip_Exceed_MAX() {
		given().
			get("/products?$skip=51958").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(0)).
			log().all();
	}
	
	@Test(description = "Check that no data are skipped in response and return 10 as the default limit when sending API GET/products request with SKIP parameter 0")
	public void PDT_GET_Skip_MIN() {
		given().
			get("/products?$skip=0").
		then().
			spec(responsespec).
			statusCode(200).
			body("data.size()", equalTo(10)).
			log().all();
	}
	
	@Test(description = "Check that response error message in response when sending API GET/products request with String SKIP parameter q")
	public void PDT_GET_String_Skip() {
		given().
			get("/products?$skip=q").
		then().
			spec(responsespec).
			statusCode(500).
			body("message", equalTo("SQLITE_ERROR: no such column: NaN")).
			log().all();
	}
	
	@Test(description = "Check that the response status code is 200 when sending API GET/products/validID")
	public void PDT_GET_ID() {
		given().
			get("/products/309062").
		then().
			spec(responsespec).
			statusCode(200).
			body("id", equalTo(309062)).
			log().all();
	}
	
	@Test(description = "Check that the response status code is 404 when sending API GET/products/invalidID")
	public void PDT_GET_status_code_Not_exist_id() {
		given().
			get("/products/993223").
		then().
			spec(responsespec).
			statusCode(404).
			log().all();
	}
	
	@Test(description = "Check that the error message in response when sending API GET/products/invalidID")
	public void PDT_GET_Message_Not_exist_id() {
		given().
			get("/products/993223").
		then().
			spec(responsespec).
			statusCode(404).
			body("message",equalTo("No record found for id '993223'")).
			log().all();
	}
	
	//***************************************POST*********************************************
		@Test(description = "Check that the response status code is 201 when sending API POST/products request with valid data")
		public void PDT_POST_Valid_Request() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			request.put("price", 1.22);
			request.put("shipping", 0);
			request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(201);
			
			given().
				get("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200).
				body("data.size()", equalTo(1)).log().all();
			
			given().
				delete("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200);
			

			given().
				get("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200).
				body("data.size()", equalTo(0)).log().all();
		}
		
		@Test(description = "Check that the response status code is 400 when sending API POST/products request with no name")
		public void PDT_POST_Request_NO_Name() {
			JSONObject request = new JSONObject();

			//request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			request.put("price", 1.22);
			request.put("shipping", 0);
			request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(400);
		}
		
		@Test(description = "Check that the response status code is 400 when sending API POST/products request with no type")
		public void PDT_POST_Request_NO_Type() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			//request.put("type", "HardGood");
			request.put("price", 1.22);
			request.put("shipping", 0);
			request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(400);
		}
		
		@Test(description = "Check that the response status code is 201 when sending API POST/products request with no price")
		public void PDT_POST_Request_NO_Price() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			//request.put("price", 1.22);
			request.put("shipping", 0);
			request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(201);
			
			given().
				delete("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200);
			
			given().
			get("/products/?name=Hakim-AAA_Batteries(4-Pack)").
		then().
			statusCode(200).
			body("data.size()", equalTo(0)).log().all();
		}
		
		@Test(description = "Check that the response status code is 201 when sending API POST/products request with no Shipping")
		public void PDT_POST_Request_NO_Shipping() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			request.put("price", 1.22);
			//request.put("shipping", 0);
			request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(201);
			
			given().
				delete("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200);
			
			given().
				get("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200).
				body("data.size()", equalTo(0)).log().all();
		}
		
		@Test(description = "Check that the response status code is 400 when sending API POST/products request with no UPC")
		public void PDT_POST_Request_NO_UPC() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			request.put("price", 1.22);
			request.put("shipping", 0);
			//request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(400);
		}
		
		@Test(description = "Check that the response status code is 400 when sending API POST/products request with no Description")
		public void PDT_POST_Request_NO_Description() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			request.put("price", 1.22);
			request.put("shipping", 0);
			request.put("upc", "041333429999");
			//request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(400);
		}
		
		@Test(description = "Check that the response status code is 201 when sending API POST/products request with no Manufacturer")
		public void PDT_POST_Request_NO_Manufacturer() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			request.put("price", 1.22);
			request.put("shipping", 0);
			request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			//request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(201);
			
			given().
				delete("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200);
			
			given().
				get("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200).
				body("data.size()", equalTo(0)).log().all();
		}
		
		@Test(description = "Check that the response status code is 400 when sending API POST/products request with no Model")
		public void PDT_POST_Request_NO_Model() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			request.put("price", 1.22);
			request.put("shipping", 0);
			request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			//request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(400);
		}
		
		@Test(description = "Check that the response status code is 201 when sending API POST/products request with no URL")
		public void PDT_POST_Request_NO_URL() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			request.put("price", 1.22);
			request.put("shipping", 0);
			request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			//request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(201);
			
			given().
				delete("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200);
			
			given().
				get("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200).
				body("data.size()", equalTo(0)).log().all();
		}
		
		@Test(description = "Check that the response status code is 201 when sending API POST/products request with no Image")
		public void PDT_POST_Request_NO_Image() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			request.put("price", 1.22);
			request.put("shipping", 0);
			request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			//request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
			statusCode(201);
			
			given().
				delete("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200);
			
			given().
				get("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200).
				body("data.size()", equalTo(0)).log().all();
		}
		
		@Test(description = "Check that the response status code is 400 when sending API POST/products request with no DATA")
		public void PDT_POST_Request_NO_DATA() {

			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
			when().
				post("/products").
			then().
			statusCode(400);
		}
		
		@Test(description = "Check that the response status code is 200 when sending API DELETE/products request with valid id")
		void PDT_DELETE_Request_VALID() {
			JSONObject request = new JSONObject();

			request.put("name", "Hakim-AAA_Batteries(4-Pack)");
			request.put("type", "HardGood");
			request.put("price", 1.22);
			request.put("shipping", 0);
			request.put("upc", "041333429999");
			request.put("description", "Compatible with select electronic devices; AAA size; Hakim Power Preserve technology; 4-pack");
			request.put("manufacturer", "Hakim");
			request.put("model", "MN2400B4Z");
			request.put("url", "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC");
			request.put("image", "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg");
			
			
			given().
				header("Content-Type", "application/json").
				contentType(ContentType.JSON).
				accept(ContentType.JSON).
				body(request.toJSONString()).
			when().
				post("/products").
			then().
				statusCode(201);
			
			given().
				get("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200).
				body("data.size()", equalTo(1)).log().all();
			
			given().
				delete("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200);
			

			given().
				get("/products/?name=Hakim-AAA_Batteries(4-Pack)").
			then().
				statusCode(200).
				body("data.size()", equalTo(0)).log().all();
			}
		
		@Test(description = "Check that the response status code is 404 when sending API DELETE/products request with unknown id")
		void PDT_DELETE_Request_INVALID_ID() {
			given().
				delete("/products/1111111111").
			then().
				statusCode(404).log().all();

		}
}
