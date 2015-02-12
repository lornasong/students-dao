package database;

import java.io.File;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import com.j256.simplewebframework.displayer.StringResultDisplayer;
import com.j256.simplewebframework.freemarker.FreemarkerHtmlDisplayer;
import com.j256.simplewebframework.handler.ServiceHandler;
import com.j256.simplewebframework.resource.FileLocator;

import freemarker.template.Configuration;

public class DatabaseWebMain {
	
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

		
		StudentDatabaseWeb database = new StudentDatabaseWeb("Your School");
		DaoMain dao = new DaoMain();
		dao.doMain(null);
		
		// create a service handler
		ServiceHandler serviceHandler = new ServiceHandler();
				
		// register our service that handles requests from simple-web-framework
//		serviceHandler.registerWebService(new HomeService(database));
		serviceHandler.registerWebService(new ModifyService(dao));
		serviceHandler.registerWebService(new SearchService(dao));
//		serviceHandler.registerWebService(new ViewService(dao));
		
		// register a displayer of String results
		serviceHandler.registerResultDisplayer(new StringResultDisplayer());
		
		//Stuff for to use Freemarker instead of stringbuilding html
		//Has errors. Need to press forward twice. Page will render.
		FreemarkerHtmlDisplayer displayer = new FreemarkerHtmlDisplayer();
		FileLocator fileLocator = new FileLocator(new File("target/classes"), new String [] { "index.html" });
		displayer.setFileLocator(fileLocator);
		Configuration configuration = new Configuration();
		displayer.setTemplateConfig(configuration);
		serviceHandler.registerResultDisplayer(displayer);
		
		//Handlers
		HandlerCollection handlers = new HandlerCollection();
		handlers.addHandler(serviceHandler);
		handlers.addHandler(new WrongUrlHandler());
		
		server.setHandler(handlers);
		server.start();
	}

}
