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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.api.PlantumlUtils;
import net.sourceforge.plantuml.code.NoPlantumlCompressionException;
import net.sourceforge.plantuml.code.Transcoder;
import net.sourceforge.plantuml.code.TranscoderUtil;

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
    private HistoryEntryRepository historyEntryRepository = new HistoryEntryRepository();

    @Override
    public void init() throws ServletException {
        this.forwardPath = this.getInitParameter("forwardPath");
        if (this.forwardPath == null) {
            this.forwardPath = "/bootstrap1/newjsp.jsp";
        }

        this.getServletContext().setAttribute("historyEntryRepository", historyEntryRepository);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.setCharacterEncoding("UTF-8");
        String text = request.getParameter("text");
        String encoded = DEFAULT_ENCODED_TEXT;

        try {
            encoded = encodeDecoder.encode(text);
            HistoryEntry he = historyEntryRepository.add(encoded, text);
            request.setAttribute("historyEntryList", this.historyEntryRepository.historyEntryList);
        } catch (IOException e) {
            this.log("doPost", e);
        }
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

    static class EncodeDecoder {

        String encode(String decodedText) throws IOException {
            String encoded = getTranscoder().encode(decodedText);
            return encoded;
        }

        String decode(String encodedText) throws NoPlantumlCompressionException {
            String decoded = getTranscoder().decode(encodedText);
            return decoded;
        }

        Transcoder getTranscoder() {
            return TranscoderUtil.getDefaultTranscoder();
        }
    }

    static class HistoryEntry {

        private final String encoded;
        private final String decoded;
        private final java.time.LocalDateTime ldt;

        public HistoryEntry(String aEncoded, String aDecoded) {
            this(aEncoded, aDecoded, LocalDateTime.now());
        }

        public HistoryEntry(String aEncoded, String aDecoded, LocalDateTime aLdt) {
            this.encoded = aEncoded;
            this.decoded = aDecoded;
            this.ldt = aLdt;
        }

        String[] defaultToEmpty() {
            String[] result = new String[]{
                this.encoded != null ? this.encoded : "",
                this.decoded != null ? this.decoded : ""
            };
            return result;
        }

        @Override
        public String toString() {
            return "HistoryEntry{" + "encoded=" + encoded + ", decoded=" + decoded + ", ldt=" + ldt + '}';
        }

        static Comparator<HistoryEntry> createComparator() {
            return new Comparator<HistoryEntry>() {
                @Override
                public int compare(HistoryEntry he1, HistoryEntry he2) {
                    if (he1 == null) {
                        he1 = new HistoryEntry("", "");
                    }
                    if (he2 == null) {
                        he2 = new HistoryEntry("", "");
                    }
                    String[] he1DefaultToEmpty = he1.defaultToEmpty();
                    String[] he2DefaultToEmpty = he2.defaultToEmpty();
                    String he1CompareVal = he1DefaultToEmpty[0] + he1DefaultToEmpty[1];
                    String he2CompareVal = he2DefaultToEmpty[0] + he2DefaultToEmpty[1];
                    int compareResult = he1CompareVal.compareTo(he2CompareVal);
                    return compareResult;
                }
            };
        }
    }

    static class HistoryEntryRepository {

        private List<HistoryEntry> historyEntryList = new ArrayList<>();

        HistoryEntry add(String encoded, String decoded) {
            HistoryEntry he = new HistoryEntry(encoded, decoded);
            if (!historyEntryListContains(he)) {
                historyEntryList.add(0, he);
            }
            return he;
        }

        private boolean historyEntryListContains(HistoryEntry he) {
            final Comparator<HistoryEntry> comparatorHistoryEntry = HistoryEntry.createComparator();
            boolean isDup = false;
            for (int i = 0; !isDup && i < this.historyEntryList.size(); i++) {
                final HistoryEntry heFromList = this.historyEntryList.get(i);
                isDup = (comparatorHistoryEntry.compare(he, heFromList) == 0);

            }
            return isDup;
        }
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
