package orientdb;


import com.google.common.util.concurrent.AbstractService;
import com.orientechnologies.orient.client.remote.OServerAdmin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;
import com.orientechnologies.orient.server.config.*;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import common.Functions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

import static common.Functions.trace;

public class ODBManager extends AbstractService {
    private OServer server;
    public String odbHome                       = "";
    public int minPool                          = 1;
    public int maxPool                          = 50;
    public OrientGraphFactory orientDBfactory   = null;
    public String odb_db                        = "";
    public String port                          = "2424";
    public String portRange                     = "2424";
    public String odb_user                      = "root";
    public String odb_pass                      = "root";
    public String odb_DB_TYPE                   = "plocal"; //"memory";
    public String odb_INPUT_FILE                = "";
    public int odb_maxRetries                   = 100;
    public boolean dbExists                     = false;
    public String connType                      = "remote";
    public String connServer                    = "localhost";
    public String classToCheckIfDBExists        = "Player";
    public String localFolder                   = "/Users/gg/git/gglat/.docker/ggodb";

    public ODBManager() {

    }

    private OServerEntryConfiguration createConfigProperty(String name, String value) {
        OServerEntryConfiguration prop = new OServerEntryConfiguration();
        prop.name = name;
        prop.value = value;
        return prop;
    }

    @Override
    protected void doStart() {
        try {
            System.setProperty("ORIENTDB_HOME", odbHome);

            OServerConfiguration cfg = new OServerConfiguration();
            cfg.network = new OServerNetworkConfiguration();

            // Protocol Listeners
            List<OServerNetworkProtocolConfiguration> protocolConfigurationList = new ArrayList<>();
            cfg.network.protocols = protocolConfigurationList;

            OServerNetworkProtocolConfiguration binaryProtocol = new OServerNetworkProtocolConfiguration();
            binaryProtocol.name="binary";
            binaryProtocol.implementation="com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary";
            protocolConfigurationList.add(binaryProtocol);

            // Network Listeners
            List<OServerNetworkListenerConfiguration> listenerConfigurationList = new ArrayList<>();
            cfg.network.listeners = listenerConfigurationList;

            OServerNetworkListenerConfiguration binaryListener = new OServerNetworkListenerConfiguration();
            binaryListener.protocol="binary";
            binaryListener.socket="default";
            binaryListener.portRange=portRange;
            binaryListener.ipAddress="0.0.0.0";
            listenerConfigurationList.add(binaryListener);

            // Users
            OServerUserConfiguration[] oServerUserConfigurations = new OServerUserConfiguration[1];
            cfg.users=oServerUserConfigurations;

            OServerUserConfiguration user0 = new OServerUserConfiguration();
            user0.name = odb_user;
            user0.password = odb_pass;
            user0.resources = "*";
            oServerUserConfigurations[0] = user0;

            //Properties
            OServerEntryConfiguration[] oServerEntryConfigurations = new OServerEntryConfiguration[7];
            cfg.properties = oServerEntryConfigurations;

            oServerEntryConfigurations[0] = createConfigProperty("db.pool.min",                     minPool+"");
            oServerEntryConfigurations[1] = createConfigProperty("db.pool.max",                     maxPool+"");
            oServerEntryConfigurations[2] = createConfigProperty("profiler.enabled",                "true");
            oServerEntryConfigurations[3] = createConfigProperty("server.cache.staticResources",    "false");
            oServerEntryConfigurations[4] = createConfigProperty("log.console.level",               "info");
            oServerEntryConfigurations[5] = createConfigProperty("log.file.level",                  "fine");
            oServerEntryConfigurations[6] = createConfigProperty("plugin.dynamic",                  "false");

            server = OServerMain.create();
            server.startup(cfg);
            server.activate();

        } catch (Exception e){
            e.printStackTrace();

        } finally {
            initSteps();
        }
    }

    protected void initSteps(){

    }


    @Override
    protected void doStop() {
        try {
            server.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OrientGraph getOrientDBGraph(){
        OrientGraph graph = null;
        try {
            graph = createDB();

        } catch (Exception e) {
            e.printStackTrace();
            graph = null;
        }

        return graph;
    }

    public void createDBFactory() {
        if (orientDBfactory == null) {
            orientDBfactory = new OrientGraphFactory(
                    connType + ":" + connServer + ":" + port + "/" + odb_db,
                    odb_user,
                    odb_pass
            ).setupPool(minPool, maxPool);
        }
    }

    public void closeDBFactory() {
        if (orientDBfactory != null) orientDBfactory.close();
        orientDBfactory = null;
    }

    public boolean doesDBExist(String dbName) throws Exception {
        OServerAdmin serverAdmin = new OServerAdmin(
                connType+":" + connServer + ":"+port
        ).connect(odb_user, odb_pass);
        boolean b = doesDBExist(serverAdmin, dbName);
        serverAdmin.close();
        return b;
    }

    public boolean doesDBExist(OServerAdmin serverAdmin, String dbName) throws Exception {
        return serverAdmin.existsDatabase(odb_db, odb_DB_TYPE);
    }

    public OrientGraph createDB() throws Exception {
        OrientGraph graph = orientDBfactory != null ? orientDBfactory.getTx() : null;

        if (!dbExists || graph == null) {
            if (graph != null) { graph.shutdown(); }

            trace("Checking if Database needs to be re-created");

            OServerAdmin serverAdmin = new OServerAdmin(
                    connType+":" + connServer + ":"+port
            ).connect(odb_user, odb_pass);

            trace("Connected as Admin");

            if (!doesDBExist(serverAdmin, odb_db)) {
                serverAdmin.createDatabase(odb_db, "graph", odb_DB_TYPE);
                trace("DB Created");
            }
            serverAdmin.close();

            createDBFactory();
            graph = orientDBfactory.getTx();
            if (orientDBfactory != null && graph.getVertexType(classToCheckIfDBExists) == null) {
                trace("Rebuilding the database");

                ClassLoader classLoader = getClass().getClassLoader();
                File file = new File(classLoader.getResource("osql/create_eventstore.osql").getFile());

                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    // If NOT
                    if (!(line.contains("-- ")
                            || line.contains("CREATE DATABASE")
                            || line.contains("CONNECT remote:")
                            || line.contains("DISCONNECT")
                            || line.trim().length() == 0
                    )) {
                        trace(line);
                        graph.command(new OCommandSQL(line)).execute();
                    }
                }

                scanner.close();
                graph.commit();
                trace("DB Created");
            }
            graph.shutdown();
            closeDBFactory();

            dbExists = true;
            trace("DB's good to go");

            // TODO Notify a server admin that the DB was recreated
        }

        createDBFactory();
        graph = orientDBfactory.getTx();
        return graph;
    }

    public boolean db_action (Function<OrientGraph, Boolean> func) {
        //Add it to in-memory
        OrientGraph graph = null;
        Boolean success = false;

        try {
            // Functions.trace("opening db...");
            graph = getOrientDBGraph();
            if (graph == null) {
                //TODO send an ALERT -> email or something
                Functions.trace("DB connection is not possible, for whatever reason");
                return false;
            }

            success = func.apply(graph);

        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (graph != null) {
                    graph.rollback();
                    graph.shutdown();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } finally {
            // trace("closing db...");
            try {
                if (graph != null) {
                    if (success.equals(false)) {
                        graph.rollback();
                    }
                    graph.shutdown();

                    // close the connection?
                    // orientDBfactory.close();
                    // orientDBfactory = null;
                }
            } catch (Exception e1) { /* Do nothing */ }

            return success;
        }
    }
}
