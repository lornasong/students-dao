package database;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import com.j256.simplewebframework.displayer.FileResultDisplayer;
import com.j256.simplewebframework.displayer.JsonResultDisplayer;
import com.j256.simplewebframework.displayer.ResultDisplayer;
import com.j256.simplewebframework.displayer.StringResultDisplayer;
import com.j256.simplewebframework.freemarker.FreemarkerHtmlDisplayer;
import com.j256.simplewebframework.handler.LoggingHandler;
import com.j256.simplewebframework.handler.ServiceHandler;
import com.j256.simplewebframework.resource.FileLocator;
import com.j256.simplewebframework.resource.LocalResourceHandler;

import freemarker.template.Configuration;

public class Main {

	private static final int DEFAULT_WEB_PORT = 8080;

	public static void main(String[] args) throws Exception {
		// create jetty server
		Server server = new Server();
		// create the connector which receives HTTPD connections
		SelectChannelConnector connector = new SelectChannelConnector();
		// start it on the default port
		connector.setPort(DEFAULT_WEB_PORT);
		connector.setReuseAddress(true);
		server.addConnector(connector);

		DaoUtil daoMain = new DaoUtil();
		daoMain.configure();

		// create a service handler
		ServiceHandler serviceHandler = new ServiceHandler();

		// register our service that handles requests from simple-web-framework
		serviceHandler.registerWebService(new HomeService());
		serviceHandler.registerWebService(new ModifyService(daoMain
				.getStudentDao()));
		serviceHandler.registerWebService(new ViewService(daoMain
				.getStudentDao()));

		// register a displayer of String results
		serviceHandler.registerResultDisplayer(new StringResultDisplayer());

		// Stuff for to use Freemarker instead of stringbuilding html
		// Reminder: Has errors. Need to press forward twice. Page will render.
		FreemarkerHtmlDisplayer htmlDisplayer = new FreemarkerHtmlDisplayer();
		FileLocator fileLocator = new FileLocator(new File("target/classes"),
				new String[] { "index.html" });
		htmlDisplayer.setFileLocator(fileLocator);
		Configuration configuration = new Configuration();
		htmlDisplayer.setTemplateConfig(configuration);
		serviceHandler.registerResultDisplayer(htmlDisplayer);
		serviceHandler.registerResultDisplayer(new JsonResultDisplayer());
		
		// This handler displays css files, jpegs, etc that don't need to be
		// processed by FreeMarker
		LocalResourceHandler localResourceHandler = new LocalResourceHandler();
		localResourceHandler.setFileLocator(fileLocator);
		localResourceHandler.setDefaultDisplayer(new FileResultDisplayer());
		Map<String, ResultDisplayer> extMap = new HashMap<String, ResultDisplayer>();
		extMap.put("html", htmlDisplayer);

		// Handlers
		HandlerCollection handlers = new HandlerCollection();
		handlers.addHandler(serviceHandler);
		handlers.addHandler(localResourceHandler);
		
		handlers.addHandler(new WrongUrlHandler());

		LoggingHandler loggingHandler = new LoggingHandler();
		loggingHandler.setHandler(handlers);

		server.setHandler(loggingHandler);
		server.start();
	}

}
