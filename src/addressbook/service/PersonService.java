package addressbook.service;

import addressbook.Database;
import addressbook.Service;
import addressbook.exception.ServiceException;
import addressbook.model.Person;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PersonService extends Service<Person> {

	public PersonService(Database database) {
		super(database);
	}

	@Override
	public Person findByObject(Person model) throws SQLException {
		List<Person> people = database.getPeople();
				
		// search by id
		try {
			return people.get((int) model.getId()-1);			
		}
		catch (IndexOutOfBoundsException e) {
			// do nothing
		}
		
		// search by name
		for(Person each : findAll()) {
			if (each.getFirstName().equalsIgnoreCase(model.getFirstName()) && each.getLastName().equalsIgnoreCase(model.getLastName())) return each;
		}

		return null;
	}

	@Override
	public Person findById(long id) throws SQLException {
		Person person = null;
		PreparedStatement stmt = database.getPreparedStatement("select * from PERSON where ID =? and ACTIVE = 1");
		stmt.setLong(1, id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			person = parse(rs);
		}
		rs.close();
		stmt.close();
		return person;
	}
	
	@Override
	public List<Person> findAll() throws SQLException {
		List<Person> people = new ArrayList<Person>();
		PreparedStatement stmt = database.getPreparedStatement("select * from PERSON where ACTIVE = 1");
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			people.add(parse(rs));
		}
		rs.close();
		stmt.close();
		return people;
	}

	@Override
	public void create(Person model) throws SQLException {

		if (model == null) throw new IllegalArgumentException("This method requires an argument of type Person!");
		if (findByObject(model) != null) throw new SQLException("This person already exists!");
		List<Person> people = database.getPeople();
		model.setActive(true);
		model.setId(people.size()+1);
		people.add(model);
		database.setPeople(people);
		/*
		String stmt = "INSERT INTO PRODUCTDATA (MAKE, MODEL, DESCRIPTION, PRICE, IMAGEURL) \n VALUES('" + make + "', '" + model + "', '" + description + "', " + price + ", '" + imageURL + "')";
		DBManager dbMgr = new DBManager();
		Connection con = dbMgr.getConnection();
		executeSqlScript(con, stmtProduct);
		*/
	}

	@Override
	public void update(Person model) throws SQLException {
		if (model == null) throw new IllegalArgumentException("This method requires an argument of type Person!");
		if (findByObject(model) == null) throw new SQLException("This person does not exist!");
		
		List<Person> people = database.getPeople();		
		people.set((int) model.getId()-1, model);
		database.setPeople(people);
		
	}

	@Override
	public void delete(Person model) throws SQLException {
		model.setActive(false);
		update(model);
	}

	public Person parse(ResultSet rs) throws SQLException {
		Person person = new Person();

		person.setId(rs.getLong("ID"));
		person.setActive(rs.getBoolean("ACTIVE"));
		person.setFirstName(rs.getString("FIRSTNAME"));
		person.setLastName(rs.getString("LASTNAME"));
		person.setEmail(rs.getString("EMAIL"));
		person.setStreetAddress(rs.getString("STREETADDRESS"));
		person.setApartment(rs.getString("APARTMENT"));
		person.setCityAddress(rs.getString("CITYADDRESS"));
		person.setStateAddress(rs.getString("STATEADDRESS"));
		person.setZipAddress(rs.getLong("ZIPADDRESS"));
		person.setDateOfBirth((rs.getDate("DATEOFBIRTH")).toLocalDate());
		person.setImageBytes(rs.getBytes("IMAGEBYTES"));
		person.setImageType(rs.getString("IMAGETYPE"));

		return person;
	}
}
