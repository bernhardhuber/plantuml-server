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
import net.sourceforge.plantuml.servlet.bootstrap.Wrappers.Tuple;

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log(httpRequestDumper.dump(req));
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    // TODO add mapping url-path/[get,post]/params -> encode, decode
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log(httpRequestDumper.dump(req));

        final String umlDefault = String.format("\\@staruml%n"
                + "Alice --> Bob : hello%n"
                + "\\@enduml");

        final EncodedOrDecodedExtractor encodedOrDecodedExtractor = new EncodedOrDecodedExtractor();
        final ConvertRequestParameterMap convertRequestParameterMap = new ConvertRequestParameterMap();
        final Map<String, String> mFromParameterMap = convertRequestParameterMap.xxx(req.getParameterMap());
        final Optional<Tuple<EncodedOrDecoded, String>> xxxOpt = encodedOrDecodedExtractor.extractEncodedOrDecodedValue(mFromParameterMap);

        Tuple<EncodedOrDecoded, String> xxx = xxxOpt.orElseGet(() -> {
            Tuple<EncodedOrDecoded, String> _t = new Tuple<>(EncodedOrDecoded.decoded, umlDefault);
            return _t;
        });

        if (xxx.getU() == EncodedOrDecoded.decoded) {
            String encoded = encode(xxx.getV());
            resp.getWriter().write(String.format("%n"
                    + "decoded: %s%n"
                    + "encoded: %s%n", xxx.getV(), encoded));
        } else if (xxx.getU() == EncodedOrDecoded.encoded) {
            String decoded = decode(xxx.getV());
            resp.getWriter().write(String.format("%n"
                    + "decoded: %s%n"
                    + "encoded: %s%n", decoded, xxx.getV()));
        }
    }

    String encode(String text) {
        String encoded = encodeDecoder.encode(text);
        return encoded;
    }

    String decode(String text) {
        String decoded = encodeDecoder.decode(text);
        return decoded;
    }

}
