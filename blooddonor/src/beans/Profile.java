package beans;

import java.util.ArrayList;

public class Profile {
	String mobileno;
	String isd;
	String name;
	String dob;
	String ldd;
	String bloodgroup;
	String email;
	String gender;
	String photo;

	public Profile() {

	}

	public Profile(String mobileno, String isd, String name, String dob,
			String ldd, String bloodgroup, String email, String gender,
			String photo) {
		super();
		this.mobileno = mobileno;
		this.isd = isd;
		this.name = name;
		this.dob = dob;
		this.ldd = ldd;
		this.bloodgroup = bloodgroup;
		this.email = email;
		this.gender = gender;
		this.photo = photo;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getLdd() {
		return ldd;
	}

	public void setLdd(String ldd) {
		this.ldd = ldd;
	}

	public String getBloodgroup() {
		return bloodgroup;
	}

	public void setBloodgroup(String bloodgroup) {
		this.bloodgroup = bloodgroup;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIsd() {
		return isd;
	}

	public void setIsd(String isd) {
		this.isd = isd;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
