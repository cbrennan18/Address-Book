package addressbook;

import addressbook.exception.ServiceException;

import java.util.List;

public abstract class Service<T> {

	protected Database database;
	
	public Service(Database database) {
		this.database = database;
	}
	
	public abstract T findByObject(T model) throws ServiceException;
	public abstract T findById(long id) throws ServiceException;
	public abstract List<T> findAll() throws ServiceException;
	public abstract void create(T model) throws ServiceException;
	public abstract void update(T model) throws ServiceException;
	public abstract void delete(T model) throws ServiceException;
		
}
