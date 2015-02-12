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

	private final DaoMain dao;

	public SearchService(DaoMain dao) {
		this.dao = dao;
	}

	/**
	 * SEARCH page for students. Search field by full name or id. If id field is
	 * filled, then search will default to search for id. Otherwise it will
	 * search by full name and return all matches.
	 */
	@Path("")
	@GET
	@WebMethod
	public String search(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName,
			@QueryParam("age") String ageString) {

		StringBuilder sb = new StringBuilder();
		
		//Header
		sb.append("<html>\n");
		sb.append("<center><font face = 'verdana'><h1>").append("SEARCH")
				.append("</h1></font></center>\n");
		sb.append("<hr width = '95%' size = '5' color = '#270A33'/>\n");

		//Form
		String tab = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

		sb.append("<body><font face = 'verdana'><blockquote><form>\n");
		sb.append("First Name: <input name='firstName' type='text'/>\n");
		sb.append(tab).append(
				"Last Name: <input name='lastName' type='text'/>\n");
		sb.append(tab).append("Age: <input name='age' type='text'/>\n");
		sb.append("<input type='submit' />\n");
		sb.append("</body></html>\n");
		

		//Results
		sb.append("<br/><br/>").append("Results:<br/><br/>\n");
		sb.append(dao.listToString(dao.queryByMultipleFields(firstName,
				lastName, ageString)));
		
		//Close
		sb.append("<br/></form>\n");
		sb.append("<form action='http://localhost:8080/home'><input type='submit' value='Return Home'></form>\n");
		sb.append("</blockquote></font></p>\n</body>\n</html>\n");

		return sb.toString();
	}
}