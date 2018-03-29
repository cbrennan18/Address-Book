package addressbook;

import addressbook.exception.ServiceException;

import java.sql.SQLException;
import java.util.List;

public abstract class Service<T> {

	protected Database database;
	
	public Service(Database database) {
		this.database = database;
	}
	
	public abstract T findByObject(T model) throws SQLException;
	public abstract T findById(long id) throws SQLException;
	public abstract List<T> findAll() throws SQLException;
	public abstract void create(T model) throws SQLException;
	public abstract void update(T model) throws SQLException;
	public abstract void delete(T model) throws SQLException;
		
}
