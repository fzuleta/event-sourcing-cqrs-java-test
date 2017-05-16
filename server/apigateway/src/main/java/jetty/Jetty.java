package jetty;

import com.google.common.util.concurrent.AbstractService;
import jetty.endpoints.money.GetBalance;
import jetty.endpoints.play.Play;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;

import javax.servlet.DispatcherType;
import java.lang.management.ManagementFactory;
import java.util.EnumSet;

import static common.Functions.trace;


public class Jetty extends AbstractService {
    private Server server;
    public static int httpPort = 8090;
    public static String ip = "0.0.0.0";
    private String cors_allowedOrigins = "*";//"http://localhost:9000";
    private String cors_allowedHeaders = "*";
    private String cors_allowedMethods = "GET, HEAD, OPTIONS, POST";

    @Override
    protected void doStart() {
        try {
            trace("Starting Jetty");
            // === jetty.xml ===
            // Setup Threadpool
            QueuedThreadPool threadPool = new QueuedThreadPool();
            threadPool.setMaxThreads(500);

            // Server
            server = new Server(threadPool);

            // Scheduler
            server.addBean(new ScheduledExecutorScheduler());

            // HTTP Configuration
            HttpConfiguration http_config = new HttpConfiguration();
            http_config.setSendServerVersion(false);

            // Handler Structure
            HandlerCollection handlers = new HandlerCollection();
            ContextHandlerCollection contexts = new ContextHandlerCollection();
            handlers.setHandlers(new Handler[] { contexts, new DefaultHandler() });
            server.setHandler(handlers);

            // Extra options
            server.setDumpAfterStart(false);
            server.setDumpBeforeStop(false);
            server.setStopAtShutdown(true);

            // === jetty-jmx.xml ===
            MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
            server.addBean(mbContainer);

            // HTTP connector
            ServerConnector http = new ServerConnector(server, new HttpConnectionFactory(http_config));
            http.setHost(ip);
            http.setPort(httpPort);
            http.setIdleTimeout(30000);

            // Set the connector
            server.addConnector(http);


            //Servlet -----
            final ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            servletHandler.setContextPath("/");

            servletHandler.addServlet(GetBalance.class,     "/getbalance/*");
            servletHandler.addServlet(Play.class,           "/play/*");

            // Enable CORS - cross origin resource sharing (for http and https)
            FilterHolder cors = new FilterHolder();
            cors.setInitParameter("allowedOrigins", cors_allowedOrigins);
            cors.setInitParameter("allowedHeaders", cors_allowedHeaders);
            cors.setInitParameter("allowedMethods", cors_allowedMethods);
            cors.setFilter(new CrossOriginFilter());
            servletHandler.addFilter(cors, "*", EnumSet.of(
                    DispatcherType.REQUEST,
                    DispatcherType.ASYNC,
                    DispatcherType.FORWARD,
                    DispatcherType.INCLUDE,
                    DispatcherType.ERROR
            ));

            contexts.addHandler(servletHandler);

            server.start();
            server.join();


        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doStop() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
