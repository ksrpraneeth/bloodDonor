package beans;

public class Contact {

	String _name;
	String _phone_number;
	String _id;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Contact() {

	}

	public Contact(String id, String _name, String _phone_number) {
		this._id = id;
		this._name = _name;
		this._phone_number = _phone_number;

	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_phone_number() {
		return _phone_number;
	}

	public void set_phone_number(String _phone_number) {
		this._phone_number = _phone_number;
	}

}
