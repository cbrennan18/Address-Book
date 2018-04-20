package addressbook;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.util.HashMap;
import java.util.Map;

public abstract class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private Action defaultAction;
	private Map<String, Action> actions = new HashMap<String, Action>();
		
	public void init(ServletConfig config) throws ServletException {
		initActions();
		defaultAction = defaultAction();
		if (defaultAction == null) throw new ServletException("A default action was not specified");
		
		System.out.println("Loaded Controller with Base Path of: "+basePath());
		
	}
	
	protected abstract void initActions();
	protected abstract Action defaultAction();
	
	protected String basePath() { 
		WebServlet webServlet = this.getClass().getAnnotation(WebServlet.class);
		String urlPattern = webServlet.value()[0];
		return urlPattern.substring(0, urlPattern.lastIndexOf("/*"));
	}
		
	protected void doGet(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
		String action = httpRequest.getPathInfo();
		String response = null;
		
		try {
			if (action == null || action.equals("") || action.equals("/")) {
				response = defaultAction.execute(httpRequest, httpResponse);
			}
			else if (actions.get(action) == null) {
				httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
			else {
				response = actions.get(action).execute(httpRequest, httpResponse);
			}
		}
		catch (Exception e) {
			throw new ServletException(e);
		}
		
		if (response != null && !response.equals("")) {
			if (response.startsWith("view:")) {
				String view = response.substring(5);
				RequestDispatcher dispatcher = httpRequest.getRequestDispatcher("/WEB-INF/views/"+view);
				if (dispatcher == null) throw new ServletException("The view file (WEB-INF/views/"+view+") was not found!");
				dispatcher.forward(httpRequest, httpResponse);
			} else if (response.startsWith("json:")) {
				String json = response.substring(5);
				httpResponse.setContentType("application/json");
				httpResponse.getWriter().print(json);
			}
			else if (response.startsWith("data:")) {
				String data = response.substring(5);
				String contentType = data.split(";")[0];
				byte[] bytes = Base64.decode(data.split(";")[1]);

				httpResponse.setContentType(contentType);
				httpResponse.getOutputStream().write(bytes);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void addAction(String path, Action action) {
		actions.put(path, action);
	}
	
	public interface Action {
		public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
	}
	
}
