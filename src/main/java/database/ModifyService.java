package database;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;

import com.j256.simplewebframework.freemarker.ModelView;

/**
 * Includes all web services with url "/home/modify" base. Which includes the
 * modify default page, add, edit, and remove pages.
 * 
 * Allows users to perform these three actions on their database
 * 
 * @author lornasong
 *
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
	 * MODIFY HOME page which only has header of options. Body is blank
	 */
	@Path("")
	@GET
	@WebMethod
	public ModelView modify() {
		Map<String, Object> model = new HashMap<String, Object>();

		return new ModelView(model, "/modifyHome.html");
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

	@Path("/editStudent/{pKey}")
	@GET
	@WebMethod
	public ModelView edit(@PathParam("pKey") Integer pKey,
			@QueryParam("newFirst") String newFirst,
			@QueryParam("newLast") String newLast,
			@QueryParam("newAge") String ageString,
			@QueryParam("action") String action) {

		Student selectedStudent = dao.getStudentByPKey(pKey);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("pKey", pKey);
		model.put("firstName", selectedStudent.getFirstName());
		model.put("lastName", selectedStudent.getLastName());
		model.put("age", selectedStudent.getAge());
		
		Integer ageInt = null;
		try {
			if (ageString != null && !ageString.isEmpty()) {
				ageInt = Integer.parseInt(ageString);
				dao.updateStudentInformation(pKey, newFirst, newLast, ageInt);
			}
		} catch (NumberFormatException nfe) {
			model.put("ageError", "Error: you did not input a number for age");
		}

		return new ModelView(model, "/editStudent.html");
	}


	/**
	 * ADD student page. Has the modify header. Allows user to add students to
	 * database. User must include First name, Last name, and Age information.
	 */
	@Path("/add")
	@GET
	@WebMethod
	public ModelView modifyAddStudent(@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName, @QueryParam("ageString") String ageString) {

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

	// /**
	// * EDIT FORM opens once a user selects the specific student they want to
	// * edit. A text box opens for: first name, last name, and age. The default
	// * original first, last, and age are pre-filled in the fields. User can
	// * modify and submit changes. After submission, user will receive a
	// * confirmation.
	// */
	// @Path("/edit/form")
	// @GET
	// @WebMethod
	// public String modifyEditForm(@QueryParam("firstName") String firstName,
	// @QueryParam("lastName") String lastName, @QueryParam("age") int age) {
	//
	// String tab = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	// StringBuilder sb = new StringBuilder();
	//
	// sb.append(modifyPageHeader());
	//
	// sb.append("<html>\n");
	// sb.append("<p><body><font face = 'verdana'><blockquote><form>\n");
	// sb.append("<br/>").append("EDIT HERE").append("<br/>");
	//
	// Student student = db.getStudentByID(idModify);
	//
	// // Form with pre-filled values.
	// sb.append(
	// "First Name: <input name='firstName' required type='text' value = '")
	// .append(student.getFirstName()).append("'/>\n");
	// sb.append(tab)
	// .append("Last Name: <input name='lastName' required type='text' value = '")
	// .append(student.getLastName()).append("'/>\n");
	// sb.append(tab)
	// .append("Age: <input name='age' required type='text' value = '")
	// .append(student.getAge()).append("'/>\n");
	// sb.append("<input type='submit' />\n");
	//
	// // Initial check if response is committed. If so, edit!
	// if (firstName != null && !firstName.trim().isEmpty()) {
	// sb.append("<br/><br/>").append(
	// db.editStudentName(student, firstName, lastName, age));
	// }
	// sb.append("</blockquote></font></html></body></p>\n");
	// return sb.toString();
	// }
	//
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
	//
	// /**
	// * REMOVE TRUE student page. Confirmation page for when "Remove" button is
	// * clicked for student.
	// */
	// @Path("/remove/true")
	// @GET
	// @WebMethod
	// public String modifyRemoveTrue() {
	//
	// StringBuilder sb = new StringBuilder();
	//
	// sb.append(modifyPageHeader());
	//
	// sb.append("<html>\n");
	// sb.append("<p><body><font face = 'verdana'><blockquote><form>\n");
	// sb.append("<br/>").append("REMOVE").append("<br/>");
	//
	// sb.append("<br/>").append(db.removeStudentByID(idModify))
	// .append("<br/>");
	// sb.append("</blockquote></font></html></body></p>\n");
	// return sb.toString();
	// }

}