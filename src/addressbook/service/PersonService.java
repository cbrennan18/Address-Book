package addressbook.service;

import addressbook.Database;
import addressbook.Service;
import addressbook.exception.ServiceException;
import addressbook.model.Person;

import java.sql.*;
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
		List<Person> people = findAll();
				
		people.get((int) model.getId()-1);

		// search by name
		for(Person each : findAll()) {
			if (each.getId() == model.getId()) return each;
			else if (each.getFirstName().equalsIgnoreCase(model.getFirstName()) && each.getLastName().equalsIgnoreCase(model.getLastName())) return each;
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
	public void create(Person person) throws SQLException {

		person.setActive(true);
		person.setId(database.getNextSequence("personsequence"));

		PreparedStatement stmt = database.getPreparedStatement("INSERT INTO person (id, active, fname, lname, email, streetaddress, " +
				"apartment, cityaddress, stateaddress, zipaddress, dateofbirth, imagebytes, imagetype)" +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		stmt.setLong(1, person.getId());
		stmt.setBoolean(2, person.isActive());
		stmt.setString(3, person.getFirstName());
		stmt.setString(4, person.getLastName());
		stmt.setString(5, person.getEmail());
		stmt.setString(6, person.getStreetAddress());
		stmt.setString(7, person.getApartment());
		stmt.setString(8, person.getCityAddress());
		stmt.setString(9, person.getStateAddress());
		stmt.setString(10, person.getZipAddress());
		stmt.setDate(11, Date.valueOf(person.getDateOfBirth()));
		stmt.setBytes(12, person.getImageBytes());
		stmt.setString(13, person.getImageType());
		stmt.executeUpdate();

		stmt.close();

	}

	@Override
	public void update(Person person) throws SQLException {

		person.setActive(true);

		PreparedStatement stmt = database.getPreparedStatement("UPDATE person SET active=?,fname=?,lname=?,email=?," +
				"streetaddress=?,apartment=?,cityaddress=?,stateaddress=?,zipaddress=?,dateofbirth=?,imagebytes=?,imagetype=? where id=?");
		stmt.setBoolean(1, person.isActive());
		stmt.setString(2, person.getFirstName());
		stmt.setString(3, person.getLastName());
		stmt.setString(4, person.getEmail());
		stmt.setString(5, person.getStreetAddress());
		stmt.setString(6, person.getApartment());
		stmt.setString(7, person.getCityAddress());
		stmt.setString(8, person.getStateAddress());
		stmt.setString(9, person.getZipAddress());
		stmt.setDate(10, Date.valueOf(person.getDateOfBirth()));
		stmt.setBytes(11, person.getImageBytes());
		stmt.setString(12, person.getImageType());
		stmt.setLong(13, person.getId());

		stmt.executeUpdate();

		stmt.close();

	}

	public void delete(Person person) throws SQLException {

		PreparedStatement stmt = database.getPreparedStatement("UPDATE person SET active = 0 WHERE id=?");
		stmt.setLong(1, person.getId());

		stmt.executeUpdate();

		stmt.close();
	}


	public Person parse(ResultSet rs) throws SQLException {
		Person person = new Person();

		person.setId(rs.getLong("id"));
		person.setActive(rs.getBoolean("active"));
		person.setFirstName(rs.getString("fname"));
		person.setLastName(rs.getString("lname"));
		person.setEmail(rs.getString("email"));
		person.setStreetAddress(rs.getString("streetaddress"));
		person.setApartment(rs.getString("apartment"));
		person.setCityAddress(rs.getString("cityaddress"));
		person.setStateAddress(rs.getString("stateaddress"));
		person.setZipAddress(rs.getString("zipaddress"));
		person.setDateOfBirth((rs.getDate("dateofbirth")).toLocalDate());
		person.setImageBytes(rs.getBytes("imagebytes"));
		person.setImageType(rs.getString("imagetype"));

		return person;
	}
}
