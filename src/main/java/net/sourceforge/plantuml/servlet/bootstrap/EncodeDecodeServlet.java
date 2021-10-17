/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.plantuml.servlet.bootstrap.EncodedOrDecodedExtractor.EncodedOrDecoded;
import net.sourceforge.plantuml.servlet.bootstrap.GenericWrappers.Tuple;

/**
 *
 * @author berni3
 */
@WebServlet(name = "EncodeDecodeServlet",
        urlPatterns = {"/xxx-encode-decode"}
)
public class EncodeDecodeServlet extends HttpServlet {

    private final EncoderDecoder encodeDecoder = new EncoderDecoder();
    private final HttpRequestDumper httpRequestDumper = new HttpRequestDumper();
    final String umlDefault = String.format("\\@staruml%n"
            + "Alice --> Bob : hello%n"
            + "\\@enduml");

    @Override
    // todo implement read json from input stream
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log(httpRequestDumper.dump(req));
        req.setCharacterEncoding("UTF-8");
        final ConvertRequestParameterMap convertRequestParameterMap = new ConvertRequestParameterMap();
        final Map<String, String> mFromParameterMap
                = convertRequestParameterMap
                        .convertMapFromArrayOfValueToSingleValue(req.getParameterMap());
        encodeOrDecodeAndSendResponse(req, resp, mFromParameterMap);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log(httpRequestDumper.dump(req));
        req.setCharacterEncoding("UTF-8");
        final ConvertRequestParameterMap convertRequestParameterMap = new ConvertRequestParameterMap();
        final Map<String, String> mFromParameterMap
                = convertRequestParameterMap
.convertMapFromArrayOfValueToSingleValue(req.getParameterMap());
        encodeOrDecodeAndSendResponse(req, resp, mFromParameterMap);
    }

    /**
     * Convert and send http response.
     * <ul>
     * <li> Convert mFromParameterMap entry to
     * {@code Tuple<EncodedOrDecoded, String>}entry
     * <li>"execute" {@code Tuple<EncodedOrDecoded, String>} entry
     * <li>send its value as http response.
     * </ul>
     *
     * @param req
     * @param resp
     * @param mFromParameterMap
     * @throws IOException
     */
    void encodeOrDecodeAndSendResponse(HttpServletRequest req,
            HttpServletResponse resp,
            Map<String, String> mFromParameterMap) throws IOException {

        final EncodedOrDecodedExtractor encodedOrDecodedExtractor = new EncodedOrDecodedExtractor();

        //---
        // convert mFromParameterMap to encodedOrDecoded
        final Optional<Tuple<EncodedOrDecoded, String>> endodedOrDecodedMapOpt
                = encodedOrDecodedExtractor.extractEncodedOrDecodedValue(mFromParameterMap);
        // default to decoded+umlDefault
        final Tuple<EncodedOrDecoded, String> encodedOrDecoded = endodedOrDecodedMapOpt.orElseGet(() -> {
            Tuple<EncodedOrDecoded, String> _t = new Tuple<>(EncodedOrDecoded.decoded, umlDefault);
            return _t;
        });

        // Now generate response from encodedOrDecoded
        if (encodedOrDecoded.getU() == EncodedOrDecoded.decoded) {
            final String encoded = encode(encodedOrDecoded.getV());
            resp.getWriter().write(String.format("%n"
                    + "decoded: %s%n"
                    + "encoded: %s%n", encodedOrDecoded.getV(), encoded));
        } else if (encodedOrDecoded.getU() == EncodedOrDecoded.encoded) {
            final String decoded = decode(encodedOrDecoded.getV());
            resp.getWriter().write(String.format("%n"
                    + "decoded: %s%n"
                    + "encoded: %s%n", decoded, encodedOrDecoded.getV()));
        }
    }

    String encode(String text) {
        final String encoded = encodeDecoder.encode(text);
        return encoded;
    }

    String decode(String text) {
        final String decoded = encodeDecoder.decode(text);
        return decoded;
    }

}
