package database;

import java.io.IOException;
import java.sql.SQLException;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.j256.simplewebframework.freemarker.ModelView;
import com.j256.simplewebframework.util.ResponseUtils;

/**
 * Includes all web services with url "/home/modify" base. Which includes the
 * modify default page, add, edit, and remove pages.
 * 
 * Allows users to perform these three actions on their database
 * 
 * @author lornasong
 */
@WebService
@Produces({ "text/html" })
@Path("/home/modify")
public class ModifyService {

	private final StudentDao dao;

	public ModifyService(StudentDao dao) {
		this.dao = dao;
	}

	/**
	 * HOME page which only has header of options. Body is blank
	 */
	@Path("")
	@GET
	@WebMethod
	public ModelView modifyHome() {
		Map<String, Object> model = new HashMap<String, Object>();

		return new ModelView(model, "/modifyHome.html");
	}

	/**
	 * ADD student page. Has the modify header. Allows user to add students to
	 * database. User must include First name, Last name, and Age information.
	 */
	@Path("/add")
	@GET
	@WebMethod
	public ModelView add(@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@QueryParam("ageString") String ageString) {

		Map<String, Object> model = new HashMap<String, Object>();

		Integer ageInt = null;
		try {
			if (ageString != null && !ageString.isEmpty()) {
				ageInt = Integer.parseInt(ageString);
				dao.addStudentToDatabase(firstName, lastName, ageInt);
			}
		} catch (NumberFormatException nfe) {
			model.put("ageError", "Error: you did not input a number for age");
		}

		return new ModelView(model, "/modifyAdd.html");

	}

	/**
	 * SEARCH student page. Allows user to enter information (first name, last
	 * name, age) to search for student that they would like to edit.
	 */
	@Path("/search")
	@GET
	@WebMethod
	public ModelView search(@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@QueryParam("age") String ageString) {

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("firstName", firstName);
		model.put("lastName", lastName);
		model.put("age", ageString);

		Integer age = null;
		try {
			if (ageString != null && !ageString.isEmpty()) {
				age = Integer.parseInt(ageString);
			}
		} catch (NumberFormatException nfe) {
			model.put("ageError", "Error: you did not input a number for age");
		}

		model.put("searchList",
				dao.queryByMultipleFields(firstName, lastName, age));

		return new ModelView(model, "/modifySearch.html");
	}

	/**
	 * EDIT page once a student is selected. Display's student's current
	 * information. Allows user to edit first name, last name, and age. Or
	 * allows user to remove student.
	 */
	@Path("/editStudent/{pKey}")
	@GET
	@WebMethod
	public ModelView edit(@PathParam("pKey") Integer pKey) {

		Student selectedStudent = dao.getStudentByPKey(pKey);

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("pKey", pKey);
		model.put("firstName", selectedStudent.getFirstName());
		model.put("lastName", selectedStudent.getLastName());
		model.put("age", selectedStudent.getAge());

		return new ModelView(model, "/editStudent.html");
	}

	@Path("/submitStudent")
	@POST
	@WebMethod
	public void submit(@FormParam("pKey") Integer pKey,
			@FormParam("newFirst") String newFirst,
			@FormParam("newLast") String newLast,
			@FormParam("newAge") String ageString,
			@FormParam("action") String action,
			@Context HttpServletResponse response,
			@Context HttpServletRequest request) throws IOException {

		Student selectedStudent = dao.getStudentByPKey(pKey);

		// Don't use dao to set things. Only use it to store data. Creates weird
		// dependency
		// Setting is business logic. Don't put it in dao.
		selectedStudent.setFirstName(newFirst);
		selectedStudent.setLastName(newLast);

		Map<String, Object> model = new HashMap<String, Object>();

		try {
			if (ageString != null && !ageString.isEmpty()) {
				// in case browser isn't compliant on 'required'

				int ageInt = Integer.parseInt(ageString);
				selectedStudent.setAge(ageInt);
			}
		} catch (NumberFormatException nfe) {
			model.put("ageError", "Error: you did not input a number for age");
		}

		try {
			dao.update(selectedStudent);
		} catch (SQLException e) {
			System.out.println("Error updating student info");
			e.printStackTrace();
		}

		ResponseUtils.sendRelativeRedirect(request, response, "/home/modify/search");
	}

	/**
	 * REMOVE student page. Allows user to enter ID of student they would like
	 * to remove. Once user selects and confirms to remove the student, they
	 * will be linked to /remove_true confirmation page.
	 */
	@Path("/remove")
	@GET
	@WebMethod
	public String modifyRemoveStudent(
			@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName,
			@QueryParam("age") String ageString) {

		StringBuilder sb = new StringBuilder();

		// Search results
		sb.append("<br/><br/>").append("Results:<br/><br/>\n");
		// sb.append(dao.queryByMultipleFields(firstName,
		// lastName, ageString)));
		// idModify = idInt;
		// sb.append("<form action='http://localhost:8080/home/modify/remove/true'><input type='submit' value='Remove'></form>");

		// Close
		sb.append("</blockquote></font></html></body></p>\n");

		return sb.toString();
	}
}