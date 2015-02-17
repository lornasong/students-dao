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

	@GET
	@WebMethod
	public ModelView home() {
		Map<String, Object> model = new HashMap<String, Object>();

		return new ModelView(model, "/home.html");
	}

	@GET
	@WebMethod
	@Path("/test")
	public ModelView test() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("name", "gray");
		return new ModelView(model, "/test.html");
	}

}