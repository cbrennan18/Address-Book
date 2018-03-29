package addressbook;

import addressbook.model.Person;

import java.util.ArrayList;
import java.util.List;

public class Database {

	private List<Person> people = new ArrayList<Person>();
	
	public static Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}		
		return instance;
	}
	
	public synchronized List<Person> getPeople() {
		return people;
	}
	
	public synchronized void setPeople(List<Person> people) {
		this.people = people;
	}
	
	private static Database instance;
	private Database() {}

}
