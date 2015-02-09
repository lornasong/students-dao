package database;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

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

	private final DaoMain dao;

	private int idModify;

	public ModifyService(DaoMain dao) {
		this.dao = dao;
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

		String tab = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

		StringBuilder sb = new StringBuilder();

		// Page header
		sb.append(modifyPageHeader());

		// Input parameters
		sb.append("<html>\n");
		sb.append("<p><body><font face = 'verdana'><blockquote><form>\n");
		sb.append("<br/>").append("ADD").append("<br/>");
		sb.append("First Name: <input name='firstName' required type='text'/>\n");
		sb.append(tab).append(
				"Last Name: <input name='lastName' required type='text'/>\n");
		sb.append(tab)
				.append("Age: <input name='age' required type='text'/>\n");
		sb.append("<input type='submit' />\n");

		// Return: confirmation that student is added or fail
		sb.append("<br/><br/><br/>\n");

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

//	/**
//	 * EDIT SEARCH student page. Allows user to enter ID of student they would
//	 * like to edit. Once user selects and confirms to edit the student, they
//	 * will be linked to /edit_form to make actual edits to student information.
//	 */
//	@Path("/edit")
//	@GET
//	@WebMethod
//	public String modifyEditStudent(@QueryParam("id") String id) {
//
//		StringBuilder sb = new StringBuilder();
//		sb.append(modifyPageHeader());
//
//		sb.append("<html>\n");
//		sb.append("<p><body><font face = 'verdana'><blockquote><form>\n");
//
//		// Search form
//		sb.append("<br/>")
//				.append("EDIT - Search ID of the Student You Wish To Add")
//				.append("<br/>");
//		sb.append("ID: <input name='id' type='number'/>\n");
//		sb.append("<input type='submit' /></form>\n");
//
//		sb.append("<br/><br/>").append("Results:\n");
//
//		// Check if ID number is valid.
//		try {
//			int idInt = Integer.parseInt(id);
//			sb.append(db.searchStudentByParameters("null", idInt));
//			idModify = idInt;
//			sb.append("<form action='http://localhost:8080/home/modify/edit/form'><input type='submit' value='Edit'></form>");
//
//		} catch (NumberFormatException nfe) {
//			sb.append("The ID is invalid");
//		}
//
//		// Close
//		sb.append("</blockquote></font></html></body></p>\n");
//
//		return sb.toString();
//	}
//
//	/**
//	 * EDIT FORM opens once a user selects the specific student they want to
//	 * edit. A text box opens for: first name, last name, and age. The default
//	 * original first, last, and age are pre-filled in the fields. User can
//	 * modify and submit changes. After submission, user will receive a
//	 * confirmation.
//	 */
//	@Path("/edit/form")
//	@GET
//	@WebMethod
//	public String modifyEditForm(@QueryParam("firstName") String firstName,
//			@QueryParam("lastName") String lastName, @QueryParam("age") int age) {
//
//		String tab = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
//		StringBuilder sb = new StringBuilder();
//
//		sb.append(modifyPageHeader());
//
//		sb.append("<html>\n");
//		sb.append("<p><body><font face = 'verdana'><blockquote><form>\n");
//		sb.append("<br/>").append("EDIT HERE").append("<br/>");
//
//		Student student = db.getStudentByID(idModify);
//
//		// Form with pre-filled values.
//		sb.append(
//				"First Name: <input name='firstName' required type='text' value = '")
//				.append(student.getFirstName()).append("'/>\n");
//		sb.append(tab)
//				.append("Last Name: <input name='lastName' required type='text' value = '")
//				.append(student.getLastName()).append("'/>\n");
//		sb.append(tab)
//				.append("Age: <input name='age' required type='text' value = '")
//				.append(student.getAge()).append("'/>\n");
//		sb.append("<input type='submit' />\n");
//
//		// Initial check if response is committed. If so, edit!
//		if (firstName != null && !firstName.trim().isEmpty()) {
//			sb.append("<br/><br/>").append(
//					db.editStudentName(student, firstName, lastName, age));
//		}
//		sb.append("</blockquote></font></html></body></p>\n");
//		return sb.toString();
//	}
//
//	/**
//	 * REMOVE student page. Allows user to enter ID of student they would like
//	 * to remove. Once user selects and confirms to remove the student, they
//	 * will be linked to /remove_true confirmation page.
//	 */
//	@Path("/remove")
//	@GET
//	@WebMethod
//	public String modifyRemoveStudent(@QueryParam("id") String id) {
//
//		StringBuilder sb = new StringBuilder();
//
//		sb.append(modifyPageHeader());
//
//		sb.append("<html>\n");
//		sb.append("<p><body><font face = 'verdana'><blockquote><form>\n");
//		sb.append("<br/>").append("REMOVE").append("<br/>");
//		sb.append("ID: <input name='id' type='number'/>\n");
//		sb.append("<input type='submit' /></form>\n");
//
//		sb.append("<br/><br/>").append("Results:\n");
//
//		// Try to parse id as an int
//		try {
//			int idInt = Integer.parseInt(id);
//			sb.append(db.searchStudentByParameters("null", idInt));
//			idModify = idInt;
//			sb.append("<form action='http://localhost:8080/home/modify/remove/true'><input type='submit' value='Remove'></form>");
//
//		} catch (NumberFormatException nfe) {
//			sb.append("The ID is invalid");
//		}
//
//		// Close
//		sb.append("</blockquote></font></html></body></p>\n");
//
//		return sb.toString();
//	}
//
//	/**
//	 * REMOVE TRUE student page. Confirmation page for when "Remove" button is
//	 * clicked for student.
//	 */
//	@Path("/remove/true")
//	@GET
//	@WebMethod
//	public String modifyRemoveTrue() {
//
//		StringBuilder sb = new StringBuilder();
//
//		sb.append(modifyPageHeader());
//
//		sb.append("<html>\n");
//		sb.append("<p><body><font face = 'verdana'><blockquote><form>\n");
//		sb.append("<br/>").append("REMOVE").append("<br/>");
//
//		sb.append("<br/>").append(db.removeStudentByID(idModify))
//				.append("<br/>");
//		sb.append("</blockquote></font></html></body></p>\n");
//		return sb.toString();
//	}

}