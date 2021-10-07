package net.sourceforge.plantuml.servlet;

import java.net.InetSocketAddress;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class ServerUtils {

    private Server server;

    public ServerUtils(boolean start) throws Exception {
        server = new Server(new InetSocketAddress("127.0.0.1", 0));
        final WebAppContext webAppContext = new WebAppContext(server, "src/main/webapp", "/plantuml");
        server.addBean(webAppContext);
        if (start) {
            startServer();
        }
    }

    public ServerUtils() throws Exception {
        this(false);
    }

    public void startServer() throws Exception {
        server.start();
    }

    public void stopServer() throws Exception {
        server.stop();
    }

    public String getServerUrl() {
        final ServerConnector serverConnector = (ServerConnector) server.getConnectors()[0];
        final String host = serverConnector.getHost();
        final int port = serverConnector.getLocalPort();
        return String.format("http://%s:%d/plantuml/", host, port);
    }

}
