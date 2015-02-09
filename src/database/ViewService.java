package database;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.j256.simplewebframework.freemarker.ModelView;

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

	private final DaoMain dao;

	public ViewService(DaoMain dao) {
		this.dao = dao;
	}

	
//	/**
//	 * Default page. View/Export student database by ID
//	 */
//	@Path("/view_id")
//	@GET
//	@WebMethod
//	public ModelView viewById() {
//		Map<String, Object> model = new HashMap<String, Object>();
//
//		model.put("viewType", "VIEW BY ID");
//		model.put("exportCsv", db.saveDataAsCsv());
//		model.put("listBy", db.getStudentListById());
//		return new ModelView(model, "/view.html");
//	}
//
//	/**
//	 * View/Export student database by last name
//	 */
//	@Path("/view_lastname")
//	@GET
//	@WebMethod
//	public ModelView viewByLastName() {
//		Map<String, Object> model = new HashMap<String, Object>();
//
//		model.put("viewType", "VIEW BY LAST NAME");
//		model.put("exportCsv", db.saveDataAsCsv());
//		model.put("listBy", db.getStudentListByLastName());
//		return new ModelView(model, "/view.html");
//	}

	/**
	 * View/Export student database by age
	 */
	@Path("/view_age")
	@GET
	@WebMethod
	public ModelView viewByAge() {
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("viewType", "VIEW BY AGE");
		//model.put("exportCsv", db.saveDataAsCsv());
		model.put("listBy", dao.viewStudentsSorted("age"));
		return new ModelView(model, "/view.html");
	}

/*DEPRECATED FOR FREEMARKER
	// Header contains "Modification" title. Bar. Provides Buttons for options
	// to view by id, age, or lastname or return home.
	public String viewPageHeader(String type) {

		StringBuilder sb = new StringBuilder();

		sb.append("<html>\n");
		sb.append("<font face = 'verdana'><h1>").append(type)
				.append("</h1></font>\n");
		sb.append("<hr width = '95%' size = '5' color = '#270A33' align = 'left'/>\n");
		sb.append("<blockquote><style type='text/css'> form {display: inline;}</style>\n");
		sb.append("<form action='http://localhost:8080/home'><input type='submit' value='Return Home' style = 'float: right'></form>\n");
		sb.append("<form action='http://localhost:8080/home/view_lastname'><input type='submit' value='View By Last Name'style = 'float: right'></form>\n");
		sb.append("<form action='http://localhost:8080/home/view_id'><input type='submit' value='View By ID' style = 'float: right'></form>\n");
		sb.append("<form action='http://localhost:8080/home/view_age'><input type='submit' value='View By Age'style = 'float: right'></form>\n");
		sb.append("<br/>").append("</blockquote>");

		sb.append("</html>");

		return sb.toString();

	}

	// Footer of page only has export to csv button.
	public String viewPageFooter() {
		StringBuilder sb = new StringBuilder();

		sb.append("<html>\n");
		sb.append("<br/>");
		sb.append("<input type='button' onclick='").append(db.saveDataAsCsv())
				.append("' value ='Export To CSV'/>");
		sb.append("</html>");

		return sb.toString();
	}


	@Path("/view_id")
	@GET
	@WebMethod
	public String viewByID() {

		StringBuilder sb = new StringBuilder();

		sb.append(viewPageHeader("VIEW BY ID"));

		sb.append("<html>\n");
		sb.append("<body><font face = 'verdana'>\n");
		sb.append(db.getStudentListById()).append("\n");
		sb.append("</font></body></html>\n");

		sb.append(viewPageFooter());

		return sb.toString();
	}

	@Path("/view_lastname")
	@GET
	@WebMethod
	public String viewByLastName() {

		StringBuilder sb = new StringBuilder();

		sb.append(viewPageHeader("VIEW BY LAST NAME"));

		sb.append("<html>\n");
		sb.append("<body><font face = 'verdana'>\n");
		sb.append(db.getStudentListByLastName()).append("\n");
		sb.append("</font></body></html>\n");

		sb.append(viewPageFooter());

		return sb.toString();
	}

	@Path("/view_age")
	@GET
	@WebMethod
	public String viewByAge() {

		StringBuilder sb = new StringBuilder();

		sb.append(viewPageHeader("VIEW BY AGE"));

		sb.append("<html>\n");
		sb.append("<body><font face = 'verdana'>\n");
		sb.append(db.getStudentListByAge()).append("\n");
		sb.append("</font></body></html>\n");

		sb.append(viewPageFooter());

		return sb.toString();
	}*/
}