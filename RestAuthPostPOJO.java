package assignmentRestAssuredPOJO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RestAuthPostPOJO {
	@JsonProperty
	String success;
	@JsonProperty
	String[] error;
	@JsonProperty
	
	RestAssuredAuthenticationDataPOJO data;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String[] getError() {
		return error;
	}
	public void setError(String[] error) {
		this.error = error;
	}
	public RestAssuredAuthenticationDataPOJO getData() {
		return data;
	}
	public void setData(RestAssuredAuthenticationDataPOJO data) {
		this.data = data;
	}
	

}