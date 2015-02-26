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
		sb.append("<html><body style = 'font-family: Helvetica; background-color: #101E59;color: #C14F3C'>");

		sb.append("<div style = 'height: 25%'></div>");
		sb.append("<div>");
		sb.append("<h1>Uh Oh!</h1>");
		sb.append("<p>This page not found. Please try: ");
		sb.append("<a style = 'color: #C14F3C' href='/home'>home page</a>");
		sb.append("</div>");

		sb.append("</body></html>");

		response.getWriter().append(sb.toString());
	}
}
