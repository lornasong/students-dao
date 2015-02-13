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
 * 
 * @author lornasong
 *
 */
@WebService
@Produces({ "text/html" })
@Path("/home")
public class HomeService {

	private final StudentDatabaseWeb db;

	public HomeService(StudentDatabaseWeb db) {
		this.db = db;
	}

	@GET
	@WebMethod
	public ModelView home() {
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("schoolName", db.getSchoolName());
		return new ModelView(model, "/home.html");
	}

	@GET
	@Path("/test2")
	@WebMethod
	public String test2() {

		return "hi";
	}

	@GET
	@WebMethod
	@Path("/test")
	public ModelView test() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", "gray");
		return new ModelView(model, "/test.html");
	}

	/*
	 * Deprecated for Freemarker.
	 * 
	 * @GET
	 * 
	 * @WebMethod public String home() {
	 * 
	 * StringBuilder sb = new StringBuilder();
	 * 
	 * sb.append("<center><font face = 'verdana'><h1>Welcome to ");
	 * sb.append(db.getSchoolName()); sb.append("</h1></font></center>\n");
	 * sb.append
	 * ("<hr width = '95%' size = '5' color = '#270A33' align = 'center'>");
	 * 
	 * sb.append("<font face = 'verdana'><blockquote>\n").append("<br/>\n");
	 * sb.append("Please Select an Action Your Database:")
	 * .append("<br/><br/>");
	 * 
	 * sb.append(
	 * "<form action='home/modify'><input type='submit' value='Modify'></form>\n"
	 * ); sb.append(
	 * "<form action='home/search'><input type='submit' value='Search'></form>\n"
	 * ); sb.append(
	 * "<form action='home/view_id'><input type='submit' value='View & Export'>\n"
	 * ); sb.append("</form></blockquote></font></body></html>\n");
	 * 
	 * return sb.toString(); }
	 */
}