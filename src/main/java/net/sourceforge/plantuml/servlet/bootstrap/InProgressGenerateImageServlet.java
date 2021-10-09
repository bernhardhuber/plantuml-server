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
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.servlet.PublicDiagramResponse;
import net.sourceforge.plantuml.servlet.bootstrap.EncodedOrDecodedExtractor.EncodedOrDecoded;
import net.sourceforge.plantuml.servlet.bootstrap.Wrappers.Tuple;

/**
 *
 * @author berni3
 */
@WebServlet(name = "generateImageServlet",
        urlPatterns = {"/xxx-generate-image/*"}
)
public class InProgressGenerateImageServlet extends HttpServlet {

    public static class InProgressRuntimeException extends RuntimeException {

        public InProgressRuntimeException(String string, Throwable thrwbl) {
            super(string, thrwbl);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String umlDefault = String.format("\\@staruml%n"
                + "Alice --> Bob : hello%n"
                + "\\@enduml");
        Map<String, String[]> m = req.getParameterMap();
        final String decoded;
        final Tuple<EncodedOrDecoded, String> umlInput;
        {
            final EncodedOrDecodedExtractor encodedOrDecodedExtractor = new EncodedOrDecodedExtractor();
            Optional<Tuple<EncodedOrDecoded, String>> xx = encodedOrDecodedExtractor.extractEncodedOrDecodedValue(m);
            umlInput = xx.orElseGet(() -> {
                String _uml = umlDefault;
                final Tuple<EncodedOrDecoded, String> _t = new Tuple<>(EncodedOrDecoded.decoded, _uml);
                return _t;
            });

            if (umlInput.getU() == EncodedOrDecoded.decoded) {
                decoded = umlInput.getV();
            } else if (umlInput.getU() == EncodedOrDecoded.encoded) {
                final String encoded = umlInput.getV();
                final EncoderDecoder encoderDecoder = new EncoderDecoder();
                decoded = encoderDecoder.decode(encoded);
            } else {
                decoded = umlDefault;
            }
        }
        final FileFormat fileFormat;
        {
            final FileFormatExtractor fileFormatExtractor = new FileFormatExtractor();
            Optional<FileFormat> yy = fileFormatExtractor.extractFileFormat(m);
            fileFormat = yy.orElse(FileFormat.PNG);
        }
        inProgressGenerateAndSendImage(req, resp, decoded, 0, fileFormat);
    }

    // TODO /url-path/get,post/params:encoded,format -> image
    // TODO /url-path/get,post/params:decoded,format -> image
    // /<servlet>/{format}/encoded
    // /<servlet>?format={format}&encoded={encoded}
    // /<servlet>/{format}?encoded={encoded}
    // /<servlet>/{encoded}?format={format}
    void inProgressGenerateAndSendImage(HttpServletRequest request,
            HttpServletResponse response,
            String uml,
            int idx,
            FileFormat ff) {
        try {
            final PublicDiagramResponse diagramResponse = new PublicDiagramResponse(response, ff, request);
            diagramResponse.sendDiagram(uml, idx);
        } catch (IOException ioex) {
            throw new InProgressRuntimeException("generateAndSendImage", ioex);
        }
    }

}
