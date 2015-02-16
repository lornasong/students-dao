package database;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

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
	@GET()
	@WebMethod
	public ModelView edit(@PathParam("pKey") Integer pKey, @QueryParam("newFirst") String newFirst,
			@QueryParam("newLast") String newLast,
			@QueryParam("newAge") String ageString) {

		Student selectedStudent = dao.getStudentByPKey(pKey);
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("pKey", pKey);
		model.put("firstName", selectedStudent.getFirstName());
		model.put("lastName", selectedStudent.getLastName());
		model.put("age", selectedStudent.getAge());
		return new ModelView(model, "/editStudent.html");
	}
	
	
	
	
	
	/**
	 * This is the header and buttons that will show up on all modification type
	 * pages. That is, on the Add, Edit, and Remove pages. Header will have
	 * button options to go to each page as well as "return home"
	 */
	public String modifyPageHeader() {

		StringBuilder sb = new StringBuilder();

		// Header Title
		sb.append("<html>\n");
		sb.append("<center><font face = 'verdana'><h1>")
				.append("MODIFICATIONS").append("</h1></font></center>\n");
		sb.append("<hr width = '95%' size = '5' color = '#270A33'>");

		// Buttons: Add, Edit, Remove, Home
		sb.append("<body><blockquote>");
		sb.append("<form action='http://localhost:8080/home'><input type='submit' value='Return Home' style = 'float: right'></form>");
		sb.append("<form action='http://localhost:8080/home/modify/remove'><input type='submit' value='Remove a Student' style = 'float: right'></form>");
		sb.append("<form action='http://localhost:8080/home/modify/edit'><input type='submit' value='Edit a Student' style = 'float: right'></form>");
		sb.append("<form action='http://localhost:8080/home/modify/add'><input type='submit' value='Add a Student' style = 'float: right'></form>");
		sb.append("</blockquote></body></html>\n");
		return sb.toString();
	}

	/**
	 * This is the search form with user input fields for First Name, Last Name,
	 * and Age.
	 */
	public String queryingParametersRequired() {

		StringBuilder sb = new StringBuilder();

		sb.append("<html>\n");

		// Query Fields: first name, last name, age
		String tab = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

		sb.append("<body><font face = 'verdana'><blockquote><form>\n");
		sb.append("First Name: <input name='firstName' required type='text'/>\n");
		sb.append(tab).append(
				"Last Name: <input name='lastName' required type='text'/>\n");
		sb.append(tab)
				.append("Age: <input name='age' required type='text'/>\n");
		sb.append("<input type='submit' />\n");
		sb.append("</blockquote></font></body></html>\n");

		return sb.toString();
	}

	public String queryingParameters() {

		StringBuilder sb = new StringBuilder();

		sb.append("<html>\n");

		// Query Fields: first name, last name, age
		String tab = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

		sb.append("<body><font face = 'verdana'><blockquote><form>\n");
		sb.append("First Name: <input name='firstName' type='text'/>\n");
		sb.append(tab).append(
				"Last Name: <input name='lastName' type='text'/>\n");
		sb.append(tab).append("Age: <input name='age' type='text'/>\n");
		sb.append("<input type='submit' />\n");
		sb.append("</blockquote></font></body></html>\n");

		return sb.toString();
	}

	/**
	 * MODIFY HOME page which only has header of options. Body is blank
	 */
	@Path("")
	@GET
	@WebMethod
	public String modify() {
		return modifyPageHeader();
	}

	/**
	 * ADD student page. Has the modify header. Allows user to add students to
	 * database. User must include First name, Last name, and Age information.
	 */
	// TODO: make age into stringAge. Parse it into int in method.
	// Make pages for when requests don't make sense. When user types in wrong
	// input type, should be handled. not show E messages. Return content from
	// page.
	// Do data input handling in method.
	@Path("/add")
	@GET
	@WebMethod
	public String modifyAddStudent(@QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName, @QueryParam("age") int age) {

		StringBuilder sb = new StringBuilder();

		// Page header
		sb.append(modifyPageHeader());

		// Input parameters
		sb.append("<html>\n");
		sb.append("<p><body><font face = 'verdana'><blockquote><form>\n");
		sb.append("<br/>").append("ADD").append("<br/>");
		sb.append(queryingParametersRequired());

		// Return: confirmation that student is added or fail
		sb.append("<br/><br/><br/>Result:\n");

		if (firstName != null && !firstName.trim().isEmpty()) {
			dao.addStudentToDatabase(firstName, lastName, age);
			sb.append("Student has been added to database");
		} else {
			sb.append("Failed to add student");
		}

		// Close
		sb.append("</form></blockform></font></html></body></p>\n");

		return sb.toString();
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

		sb.append(modifyPageHeader());

		// Search form
		sb.append("<html>\n");
		sb.append("<p><body><font face = 'verdana'><blockquote><form>\n");
		sb.append("<br/>").append("REMOVE").append("<br/>");
		sb.append(queryingParameters());

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