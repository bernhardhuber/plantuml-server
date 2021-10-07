/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.sourceforge.net
 *
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.api.PlantumlUtils;
import net.sourceforge.plantuml.servlet.bootstrap.EncodeDecoder.EncodeDecoderException;
import net.sourceforge.plantuml.servlet.bootstrap.Example.ExampleEntryRepository;
import net.sourceforge.plantuml.servlet.bootstrap.Example.ExampleEntryRepositoryFactory;
import net.sourceforge.plantuml.servlet.bootstrap.History.HistoryEntry;
import net.sourceforge.plantuml.servlet.bootstrap.History.HistoryEntryRepository;
import net.sourceforge.plantuml.servlet.bootstrap.Snippet.SnippetEntryRepository;
import net.sourceforge.plantuml.servlet.bootstrap.Snippet.SnippetEntryRepositoryFactory;

/*
 * Original idea from Achim Abeling for Confluence macro
 * See http://www.banapple.de/display/BANAPPLE/plantuml+user+macro
 *
 * This class is the old all-in-one historic implementation of the PlantUml server.
 * See package.html for the new design. It's a work in progress.
 *
 * Modified by Arnaud Roques
 * Modified by Pablo Lalloni
 * Modified by Maxime Sinclair
 *
 */
@SuppressWarnings("serial")
public class PlantUmlServlet1 extends HttpServlet {

    private static final String DEFAULT_ENCODED_TEXT = "SyfFKj2rKt3CoKnELR1Io4ZDoSa70000";

    static {
        OptionFlags.ALLOW_INCLUDE = false;
        if ("true".equalsIgnoreCase(System.getenv("ALLOW_PLANTUML_INCLUDE"))) {
            OptionFlags.ALLOW_INCLUDE = true;
        }
    }

    private final EncodeDecoder encodeDecoder = new EncodeDecoder();
    private String forwardPath = "";
// Components
    private ExampleEntryRepository exampleEntryRepository;
    private HistoryEntryRepository historyEntryRepository;
    private SnippetEntryRepository snippetEntryRepository;

    @Override
    public void init() throws ServletException {
        this.forwardPath = this.getInitParameter("forwardPath");
        if (this.forwardPath == null) {
            this.forwardPath = "/bootstrap/bootstrap1.jsp";
        }

        exampleEntryRepository = new ExampleEntryRepository();
        new ExampleEntryRepositoryFactory().setupFromExampleEntryPeristantStore(exampleEntryRepository);
        this.getServletContext().setAttribute("exampleEntryRepository", this.exampleEntryRepository);

        historyEntryRepository = new HistoryEntryRepository();
        this.getServletContext().setAttribute("historyEntryRepository", this.historyEntryRepository);

        this.snippetEntryRepository = new SnippetEntryRepository();
        new SnippetEntryRepositoryFactory().setupFromSnippetEntryPeristantStore(snippetEntryRepository);
        this.getServletContext().setAttribute("snippetEntryRepository", this.snippetEntryRepository);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding("UTF-8");

        final String text = request.getParameter("text");
        String encoded = DEFAULT_ENCODED_TEXT;
        // history
        try {
            encoded = encodeDecoder.encode(text);
            HistoryEntry he = historyEntryRepository.add(encoded, text);
            request.setAttribute("historyEntryList", this.historyEntryRepository.fetchAllHistoryEntry());
        } catch (EncodeDecoderException e) {
            this.log("doPost", e);
        }
        // example
        request.setAttribute("exampleEntryList", exampleEntryRepository.fetchAllExampleEntry());
        // snippet
        request.setAttribute("snippetEntryList", snippetEntryRepository.fetchAllSnippetEntry());

        //---
        request.setAttribute("decoded", text);
        request.setAttribute("encoded", encoded);

        // check if an image map is necessary
        if (text != null && PlantumlUtils.hasCMapData(text)) {
            request.setAttribute("mapneeded", Boolean.TRUE);
        }

        final String theForwardPath = this.forwardPath;
        final RequestDispatcher dispatcher = request.getRequestDispatcher(theForwardPath);
        dispatcher.forward(request, response);
    }

    static class ImageFetcher {

        static private HttpURLConnection getConnection(URL url) throws IOException {
            if (url.getProtocol().startsWith("https")) {
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setReadTimeout(10000); // 10 seconds
                // printHttpsCert(con);
                con.connect();
                return con;
            } else {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setReadTimeout(10000); // 10 seconds
                con.connect();
                return con;
            }
        }

        static public InputStream getImage(URL url) throws IOException {
            InputStream is = null;
            HttpURLConnection con = getConnection(url);
            is = con.getInputStream();
            return is;
        }
    }
}
