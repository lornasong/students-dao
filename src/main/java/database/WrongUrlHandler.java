package database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 * Handles URLs that are not set pages e.g. localhost:8080 and give link to
 * redirect to home page. Displays a comic as well!
 * 
 * @author lornasong
 */
public class WrongUrlHandler extends AbstractHandler {

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		if (response.isCommitted()) {
			// don't do anything if we are committed
			return;
		}
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);//404
		
		baseRequest.setHandled(true);
		response.setContentType("text/html");

		StringBuilder sb = new StringBuilder();
		sb.append("<html><font face = 'verdana' color = '#2E1A63'><body>");

		sb.append("<br/><br/><br/>");
		sb.append("<center>");
		sb.append(getComic());
		sb.append("</center>");

		sb.append("<h1>Uh Oh!</h1>");
		sb.append("<p>This page not found. Please try: ");
		sb.append("<a href='/home'>home page<br/></a>");

		sb.append("</p></body></font></html>");

		response.getWriter().append(sb.toString());
	}

	/**
	 * Randomly returns the html string for 1 or 7 Calvin and Hobbes comic to
	 * post in 404 page.
	 */
	private String getComic() {

		List<String> comicImages = new ArrayList<String>();

		Random rand = new Random();
		int randNum = rand.nextInt(7);
		comicImages
				.add("<img src ='http://assets.amuniversal.com/55b2daf0807d01302581001dd8b71c47' width= '600' height='191'></img>");
		comicImages
				.add("<img src = 'http://lh5.ggpht.com/_E-d7A1IDPF0/SflMElwBZAI/AAAAAAAAEbM/0Ofc1GEeT-c/ch851207_thumb1.jpg?imgmax=800'></img>");
		comicImages
				.add("<img src = 'http://chadashim.thegsc.co.il/files/sites/2/2013/01/95587369oa8.gif'></img>");
		comicImages
				.add("<img src = 'http://www.macroeducation.org/wp-content/uploads/2012/07/calvin-hobbes-imaginary-numbers-and-calculus.gif'></img>");
		comicImages
				.add("<img src = 'http://assets.amuniversal.com/e34e55e05e08012ee3bf00163e41dd5b'></img>");
		comicImages
				.add("<img src = 'https://fantasycluster.files.wordpress.com/2013/08/ch-2.jpg'></img>");
		comicImages
				.add("<img src = 'http://2.bp.blogspot.com/-m5tP7Du3PJg/UukvP8SlpKI/AAAAAAAADUc/MsIfjk7uTjo/s1600/CalvinAndHobbes_13.gif'></img>");

		return comicImages.get(randNum);
	}
}
