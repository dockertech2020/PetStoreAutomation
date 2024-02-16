package api.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPointsUsingPropertyFile;
import api.payload.User;
import io.restassured.response.Response;

public class UserTestsUsingPropertyFile {

	Faker faker; // Defining variable
	User userPayload; // Defining variable

	public Logger logger; // creating logger variable

	@BeforeClass
	public void setup() {

		faker = new Faker(); // creating object
		userPayload = new User(); // creating object

		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());

		// for logs (initiator logger varaiale)
		logger = LogManager.getLogger(this.getClass());
	}

	@Test(priority = 1)
	public void testPostUser() {
		logger.info("********** Creating User ***************");
		Response response = UserEndPointsUsingPropertyFile.createUser(userPayload);
		response.then().log().all();

		Assert.assertEquals(response.statusCode(), 200);

	}

	@Test(priority = 2)
	public void testGetUserByName() {
		logger.info("********** Reading User Info ***************");
		Response response = UserEndPointsUsingPropertyFile.readUser(this.userPayload.getUsername());
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(), 200);

	}

	@Test(priority = 3)
	public void testUpdateUserByName() {

		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());

		logger.info("********** Updating User Info ***************");

		Response response = UserEndPointsUsingPropertyFile.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().body();

		Assert.assertEquals(response.getStatusCode(), 200);

		// To Validate make sure update is happened or not
		Response responseAfterUpdate = UserEndPointsUsingPropertyFile.readUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(), 200);

	}

	@Test(priority = 4)
	public void testDeleteUserbyUserName() {

		logger.info("********** Deleting User Info ***************");
		Response response = UserEndPointsUsingPropertyFile.deleteUser(this.userPayload.getUsername());
		response.then().statusCode(200);

		Assert.assertEquals(response.getStatusCode(), 200);

	}
}
