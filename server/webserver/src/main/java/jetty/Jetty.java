package jetty;

import com.google.common.util.concurrent.AbstractService;
import common.Functions;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.lang.management.ManagementFactory;

import static common.Functions.trace;

public class Jetty extends AbstractService {
    private Server server;
    public static int httpPort = 8888;
    public static String staticFolderLocation = "./web/src";

    @Override
    protected void doStart() {
        try {
            Functions.trace("Starting Jetty");
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
            http_config.addCustomizer(new SecureRequestCustomizer());
            http_config.setSendServerVersion(false);
            http_config.setOutputBufferSize(32768);

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
            http.setHost("0.0.0.0");
            http.setPort(httpPort);
            http.setIdleTimeout(30000);
            trace("setting http: " + httpPort);

            // Set the connector
            server.addConnector(http);


            // === jetty-requestlog.xml ===
            NCSARequestLog requestLog = new NCSARequestLog();
            requestLog.setFilename("./log/jetty_yyyy_mm_dd.request.log");
            requestLog.setFilenameDateFormat("yyyy_MM_dd");
            requestLog.setRetainDays(90);
            requestLog.setAppend(true);
            requestLog.setExtended(true);
            requestLog.setLogCookies(false);
            requestLog.setLogTimeZone("GMT");
            RequestLogHandler requestLogHandler = new RequestLogHandler();
            requestLogHandler.setRequestLog(requestLog);
            handlers.addHandler(requestLogHandler);


            GzipHandler gzipHandler = new GzipHandler();
            gzipHandler.setIncludedMimeTypes(
                    "text/html",
                    "text/plain",
                    "text/xml",
                    "text/css",
                    "application/json",
                    "application/javascript",
                    "text/javascript");
            contexts.addHandler(gzipHandler);


            WebAppContext webapp = new WebAppContext();
            gzipHandler.setHandler(webapp);
            webapp.setBaseResource(Resource.newResource( new File( staticFolderLocation ) ));
            webapp.setContextPath("/");
            webapp.setWelcomeFiles(new String[] { "index.html" });
            webapp.addServlet(DefaultServlet.class, "/");
            contexts.addHandler(webapp);

            // for SPA
            ErrorPageErrorHandler errorMapper = new ErrorPageErrorHandler();
            errorMapper.addErrorPage(404,"/"); // map all 404's to root (aka /index.html)
            webapp.setErrorHandler(errorMapper);

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
