package database;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.j256.simplewebframework.freemarker.ModelView;
import com.j256.simplewebframework.util.ResponseUtils;

/**
 * Provides a web view of the students enrolled based on id, age, or lastname.
 * Includes an export that exports sorted view to CSV located on desktop.
 * Default view is view by ID.
 * 
 * @author lornasong
 */
@WebService
@Produces({ "text/html" })
@Path("/home")
public class ViewService {

	private final StudentDao dao;

	public ViewService(StudentDao dao) {
		this.dao = dao;
	}
	
	/**
	 * Default page. View/Export student database by ID
	 */
	@Path("/view")
	@GET
	@WebMethod
	public ModelView viewHome(@QueryParam("viewType") String viewType) {
		Map<String, Object> model = new HashMap<String, Object>();

		return new ModelView(model, "/viewHome.html");
	}

	/**
	 * View student database by different parameters: age, id, last name
	 */
	@Path("/view/submit")
	@GET
	@WebMethod
	public ModelView submit(@FormParam("viewType") String viewType){
		Map<String, Object> model = new HashMap<String, Object>();
		
		model.put("viewType", viewType);
		model.put("viewTypeList", dao.getStudentListSorted(viewType));
	
		return new ModelView(model, "/viewSubmit.html");
	}

	/**
	 * Export student database by type
	 */
	@Path("/view/export")
	@POST
	@WebMethod
	public void export(@FormParam("viewType") String viewType,
			@Context HttpServletResponse response,
			@Context HttpServletRequest request) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("viewType", viewType);
		dao.exportToCsv(dao.getStudentListSorted(viewType));
		ResponseUtils.sendRelativeRedirect(request, response, "/home/view");
	}
}