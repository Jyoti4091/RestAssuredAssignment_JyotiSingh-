package restAssuredAssignment;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import assignmentRestAssuredPOJO.RestAuthPostPOJO;
import assignmentRestAssuredPOJO.responseDetailsAdmin;
public class RestAssuredAssignmentTestCases {

	Header acceptHeader = new Header("accept", "application/json");
	String accessToken;
	Logger log;
	String sRemove;

	@Test
	public void TC01_getAccessToken() throws JsonParseException, JsonMappingException, IOException {

		sRemove = "<b>Warning</b>: mysqli::mysqli(): Headers and client library minor version mismatch. Headers:100508 Library:100236 in <b>/home/u942925711/domains/upskills.in/public_html/rest-api/system/library/db/mysqli.php</b> on line <b>7</b>";

		System.out.println("TC01");

		// log4j configuration
		PropertyConfigurator.configure("C:\\Users\\07579O744\\Desktop\\IBM Training-Full Stack Tester-SDETLPB20\\Module-1\\API Testing\\REST ASSURED\\SDET-Demo-RestAssured\\DemoProjectRestAssured\\src\\log4j.properties");
		log = Logger.getLogger("devpinoyLogger");
		log.info("Rest Assured Project Assignment TC01 validations start!");

			Response res = RestAssured.given()
				.baseUri("http://rest-api.upskills.in/api/rest_admin/oauth2/token/client_credentials")
				.accept(ContentType.JSON)
				.header("Authorization",
						"Basic dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9jbGllbnQ6dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9zZWNyZXQ=")
				.when().post();

		//------------- Converting response into string to get access token------------
		String respBody = res.body().asString();
		respBody = respBody.replace(sRemove, "");
		log.info(respBody);
		ObjectMapper obj = new ObjectMapper();
		RestAuthPostPOJO token = obj.readValue(respBody, RestAuthPostPOJO.class);
		accessToken = token.getData().getAccess_token();
		log.info(accessToken);

		log.info("Rest Assured Project Assignment TC01 validations end!");

	}

	@Test
	public void TC02_adminLogin() throws JsonParseException, JsonMappingException, IOException {
		log.info("Rest Assured Project Assignment TC02 validations start!"); 
		System.out.println("TC02- Admin Login");

		File jsonFile = new File(
				"C:\\Users\\07579O744\\Desktop\\IBM Training-Full Stack Tester-SDETLPB20\\Module-1\\API Testing\\REST ASSURED\\SDET-Demo-RestAssured\\DemoProjectRestAssured\\src\\main\\java\\assignmentRestAssuredPOJO\\TestAdminLoginDetails.json");

		RestAssured.given().baseUri("http://rest-api.upskills.in/api/rest_admin/login")
				.header("Authorization", "Bearer " + accessToken).accept(ContentType.JSON).contentType(ContentType.JSON)
				.body(jsonFile).when().post().then().log().ifStatusCodeIsEqualTo(200);
				log.info("Assertion is done to validate the status code and admin username");
					

		System.out.println("TC02- In User service, call get admin user account details api endpoint");

			Response resDetails = RestAssured.given().header(acceptHeader)
				.baseUri("http://rest-api.upskills.in/api/rest_admin/user")
				.header("Authorization", "Bearer " + accessToken).accept(ContentType.JSON).contentType(ContentType.JSON)
				.body(jsonFile).when().get();

		String sBody = resDetails.body().asString();
		sBody = sBody.replace(sRemove, "");
		ObjectMapper obj = new ObjectMapper();
		responseDetailsAdmin details = obj.readValue(sBody, responseDetailsAdmin.class);
		String userName = details.getData().getUsername();
		Assert.assertEquals(userName, "upskills_admin");

		System.out.println("TC02 - Admin Logout");

		RestAssured.given().baseUri("http://rest-api.upskills.in/api/rest_admin/logout")
				.header("Authorization", "Bearer " + accessToken).accept(ContentType.JSON).contentType(ContentType.JSON)
				.when().post()
				.then().log().ifStatusCodeIsEqualTo(200);
		log.info("Assertion is done to validate the status code and admin username");
		log.info("Rest Assured Project Assignment TC02 validations end!");

	}

	@Test
	public void TC03_AddNewCustomer() {
		log.info("Rest Assured Project Assignment TC03 validations start!");
		System.out.println("TC03- Add new customer");

		File jsonFile = new File(
				"C:\\Users\\07579O744\\Desktop\\IBM Training-Full Stack Tester-SDETLPB20\\Module-1\\API Testing\\REST ASSURED\\SDET-Demo-RestAssured\\DemoProjectRestAssured\\src\\main\\java\\assignmentRestAssuredPOJO\\AddNewCustomer.json");

		RestAssured.given().header(acceptHeader).baseUri("http://rest-api.upskills.in/api/rest_admin/customers")
				.header("Authorization", "Bearer " + accessToken).accept(ContentType.JSON).contentType(ContentType.JSON)
				.body(jsonFile).when().post()
				.then().log().ifStatusCodeIsEqualTo(200);
		log.info("Assertion is done to validate the status code");

		// In Customer service, call get list of customers 

		RestAssured.given().header(acceptHeader)
				.baseUri("http://rest-api.upskills.in/api/rest_admin/customers/added_on/2017-04-30")
				.header("Authorization", "Bearer " + accessToken).accept(ContentType.JSON).contentType(ContentType.JSON)
				.body(jsonFile).when().post()
				.then().log().ifStatusCodeIsEqualTo(200);
		log.info("Assertion is done to validate the status code and response header");
		log.info("Rest Assured Project Assignment TC03 validations end!");
	}
}