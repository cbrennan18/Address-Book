package addressbook.controller;

import addressbook.Controller;
import addressbook.Database;
import addressbook.Params;
import addressbook.exception.ServiceException;
import addressbook.model.Person;
import addressbook.service.PersonService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/person/*")
@MultipartConfig
public class PersonController extends Controller {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void initActions() {
		addAction("/list", new ListAction());
		addAction("/new", new NewAction());
		addAction("/edit", new EditAction());
		addAction("/view", new ViewAction());		
		addAction("/create", new CreateAction());
		addAction("/update", new UpdateAction());
		addAction("/delete", new DeleteAction());

	}

	@Override
	protected Action defaultAction() {
		return new ListAction();
	}
	
	public class ListAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Database database = Database.getInstance();
			PersonService svc = new PersonService(database);			
			request.setAttribute("people", svc.findAll());			
			return basePath() + "/list.jsp";
		}		
	}

	public class NewAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			request.setAttribute("action", basePath() + "/create");			
			request.setAttribute("person", new Person());
			return basePath() + "/form.jsp";
		}		
	}
	
	public class EditAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Params params = new Params(request);
			Database database = Database.getInstance();
			PersonService svc = new PersonService(database);

			Person person = svc.findById(params.getLong("id"));
			if (person == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			request.setAttribute("action", basePath() + "/update");
			request.setAttribute("person", person);
			return basePath() + "/form.jsp";
		}		
	}	
	
	public class ViewAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Params params = new Params(request);
			Database database = Database.getInstance();
			PersonService svc = new PersonService(database);

			Person person = svc.findById(params.getLong("id"));
			if (person == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			
			request.setAttribute("person", person);
			return basePath() + "/view.jsp";
		}
	}	
	
	public class CreateAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Params params = new Params(request);
			if (params.isGet()) throw new ServletException("This action only responds to POST requests");

			Person person = new Person().parse(params);
			Database database = Database.getInstance();
			PersonService svc = new PersonService(database);
			if (person.isValid()) {				
				try {
					svc.create(person);
					request.getSession().setAttribute("flash", person.getFirstName() + " " + person.getLastName() + " was created!");
					response.sendRedirect(basePath());
					return null;					
				}
				catch (ServiceException e) {
					person.addValidationError(e.getMessage());
				}				
			}
			
			request.setAttribute("action", basePath() + "/create");
			request.setAttribute("person", person);
			return basePath() + "/form.jsp";
		}		
	}
	
	public class UpdateAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Params params = new Params(request);
			if (params.isGet()) throw new ServletException("This action only responds to POST requests");

			Person person = new Person().parse(params);			
			Database database = Database.getInstance();
			PersonService svc = new PersonService(database);
			if (person.isValid()) {			
				try {
					svc.update(person);
					request.getSession().setAttribute("flash", person.getFirstName() + " " + person.getLastName() + " was updated!");
					response.sendRedirect(basePath()+"/view?id="+person.getId());
					return null;
				}
				catch (ServiceException e) {
					person.addValidationError(e.getMessage());								
				}
			}
			
			request.setAttribute("action", basePath() + "/update");			
			request.setAttribute("person", person);
			return basePath() + "/form.jsp";
		}		
	}
	
	public class DeleteAction implements Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Params params = new Params(request);
			if (params.isGet()) throw new ServletException("This action only responds to POST requests");


			Database database = Database.getInstance();
			PersonService svc = new PersonService(database);

			Person person = svc.findById(params.getLong("id"));
			if (person == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return null;
			}
			
			try {
				svc.delete(person);
				request.getSession().setAttribute("flash", person.getFirstName() + " " + person.getLastName() + " was deleted!");
				response.sendRedirect(basePath());
				return null;
			}
			catch (ServiceException e) {
				person.addValidationError(e.getMessage());								
			}
			
			request.setAttribute("person", person);
			return basePath()+"/view.jsp";
		}
	}

}
