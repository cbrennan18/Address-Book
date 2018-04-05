package addressbook.model;

import addressbook.Model;
import addressbook.Params;
import addressbook.Require;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Person extends Model<Person> {

	private long id = -1;
	private boolean active;
		
	@Require
	private String firstName;
	
	@Require
	private String lastName;

	@Require
	private String email;

	@Require
	private String streetAddress;

	private String apartment = "";

	@Require
	private String cityAddress;

	@Require
	private String stateAddress;

	@Require
	private String zipAddress;

	@Require
	private LocalDate dateOfBirth;

	private byte[] imageBytes;

	private String imageType;


	public Person() {}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {return email; }

	public void setEmail(String email) {this.email = email; }

	public String getStreetAddress() {return streetAddress; }

	public void setStreetAddress(String streetAddress) {this.streetAddress = streetAddress; }

	public String getApartment() { return apartment; }

	public void setApartment(String apartment) { this.apartment = apartment; }

	public String getCityAddress() {return cityAddress; }

	public void setCityAddress(String cityAddress) {this.cityAddress = cityAddress; }

	public String getStateAddress() {return stateAddress; }

	public void setStateAddress(String stateAddress) {this.stateAddress = stateAddress; }

	public String getZipAddress() {return zipAddress; }

	public void setZipAddress(String zipAddress) {this.zipAddress = zipAddress; }

	public LocalDate getDateOfBirth() {return dateOfBirth; }

	public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth = dateOfBirth; }

	public int getAge() {

		LocalDate todaysDate = LocalDate.now();
		return Period.between(dateOfBirth, todaysDate).getYears();
	}

	public byte[] getImageBytes() { return imageBytes; }

	public void setImageBytes(byte[] imageBytes) { this.imageBytes = imageBytes; }

	public String getImageUrl() {
		// String url = "image" + Long.toString(getId()) + ".jpg";
		return "data:" + imageType + ";base64," + Base64.encode(imageBytes);
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Person=[");
		sb.append("id=").append(id).append(",");
		sb.append("active=").append(active).append(",");
		sb.append("firstName=").append(firstName).append(",");
		sb.append("lastName=").append(lastName).append(",");
		sb.append("email=").append(email).append(",");
		sb.append("streetAddress=").append(streetAddress).append(",");
		sb.append("apartment=").append(apartment).append(",");
		sb.append("cityAddress=").append(cityAddress).append(",");
		sb.append("stateAddress=").append(stateAddress).append(",");
		sb.append("zipAddress=").append(zipAddress).append(",");
		sb.append("imageBytes=").append(imageBytes).append(",");
		sb.append("imageType=").append(imageType).append(",");
		sb.append("dateOfBirth=").append(dateOfBirth);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public Person parse(Params params) {
		Person person = new Person();
		person.id = params.getLong("id");
		person.active = params.getBoolean("active");
		person.firstName = params.getString("firstName");
		person.lastName = params.getString("lastName");
		person.email = params.getString("email");
		person.streetAddress = params.getString("streetAddress");
		person.apartment = params.getString("apartment");
		person.cityAddress = params.getString("cityAddress");
		person.stateAddress = params.getString("stateAddress");
		person.zipAddress = params.getString("zipAddress");
		person.imageBytes = params.getFileBytes("image");
		if(person.imageBytes != null) {
			person.imageType = params.getFileType("image");
		}
		person.dateOfBirth = LocalDate.parse(params.getString("dateOfBirth"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return person;
	}
}
