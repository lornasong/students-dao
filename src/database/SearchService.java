package database;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Web service for url 'home/search' which allows users to search for students
 * by full name or id.
 * 
 * @author lornasong
 */
@WebService
@Produces({ "text/html" })
@Path("/home/search")
public class SearchService {

	private final StudentDatabaseWeb db;

	public SearchService(StudentDatabaseWeb db) {
		this.db = db;
	}

	/**
	 * SEARCH page for students. Search field by full name or id. If id field is
	 * filled, then search will default to search for id. Otherwise it will
	 * search by full name and return all matches.
	 */
	@Path("")
	@GET
	@WebMethod
	public String search(@QueryParam("fullName") String fullName,
			@QueryParam("id") String id) {

		StringBuilder sb = new StringBuilder();

		sb.append("<html>\n");
		sb.append("<center><font face = 'verdana'><h1>").append("SEARCH")
				.append("</h1></font></center>\n");
		sb.append("<hr width = '95%' size = '5' color = '#270A33'/>\n");

		sb.append("<body>\n");
		sb.append("<p><font face = 'verdana'><blockquote><form>\n");
		sb.append("Full Name: <input name='fullName' type='text'/>\n");
		sb.append("ID: <input name='id' type='text'/>\n");
		sb.append("<input type='submit' />\n");

		sb.append("<br/><br/>").append("Results: <br/>\n");

		// Case that ID is empty. Query using full name
		if (id != null && !id.trim().isEmpty()) {
			try {
				int idInt = Integer.parseInt(id);
				sb.append(db.searchStudentByParameters(fullName, idInt));
			} catch (NumberFormatException nfe) {
				sb.append("The ID is invalid");
			}

		} else {
			sb.append(db.searchStudentByParameters(fullName, 0));
		}

		sb.append("<br/></form>\n");
		sb.append("<form action='http://localhost:8080/home'><input type='submit' value='Return Home'></form>\n");
		sb.append("</blockquote></font></p>\n</body>\n</html>\n");

		return sb.toString();
	}
}