package exercises;

import com.github.tomakehurst.wiremock.junit5.*;
import io.restassured.builder.*;
import io.restassured.http.*;
import io.restassured.specification.*;
import org.junit.jupiter.api.*;

import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.*;

@WireMockTest(httpPort = 9876)
public class MicroServiceExchangeTest {

	private RequestSpecification requestSpec;

	@BeforeEach
	public void createRequestSpec() {

		requestSpec = new RequestSpecBuilder().
				setBaseUri("http://localhost").
				setPort(9876).
				build();
	}

	public void setupStub1() {

		stubFor(
				post(
						urlEqualTo("/requestLoan")
				)
						.willReturn(
								aResponse()
										.withHeader("Content-Type", "text/plain")
										.withStatus(200)
										.withBody("foo!")));
	}

	public void setupStub2() {

		stubFor(
				post(
						urlEqualTo("/requestLoan")
				)
						.willReturn(
								aResponse()
										.withHeader("Content-Type", "application/json")
										.withStatus(200)
										.withBody("foo!")));

	}

	public void setupStub3() {

		stubFor(
				post(
						urlEqualTo("/requestLoan")
				)
						.willReturn(
								aResponse()
										.withHeader("Content-Type", "application/json")
										.withStatus(200)
										.withBody("Loan application received!")));
	}

	@Test
	public void test_1() {

		setupStub1();

		given().
				spec(requestSpec).
		when().
				post("/requestLoan").
		then().
				assertThat().
				statusCode(200);

	}

	@Test
	public void test_2() {

		setupStub2();

		given().
				spec(requestSpec).
		when().
				post("/requestLoan").
		then().
				assertThat().
				contentType(ContentType.JSON);
	}

	@Test
	public void test_3() {

		setupStub3();

		given().
				spec(requestSpec).
		when().
				post("/requestLoan").
		then().
				assertThat().
				body(org.hamcrest.Matchers.equalTo("Loan application received!"));
	}
}
