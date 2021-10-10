/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
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
public class GenerateImageServlet extends HttpServlet {

    private final String umlDefault = String.format("\\@staruml%n"
            + "Alice --> Bob : hello%n"
            + "\\@enduml");
    private final HttpRequestDumper httpRequestDumper = new HttpRequestDumper();

    public static class InProgressRuntimeException extends RuntimeException {

        public InProgressRuntimeException(String string, Throwable thrwbl) {
            super(string, thrwbl);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log(httpRequestDumper.dump(req));
        super.doPost(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log(httpRequestDumper.dump(req));

        Map<String, String> mForProcessing = Collections.emptyMap();
        if (mForProcessing.isEmpty()) {
            final Map<String, String> mFromPathInfo;
            {
                final String pathInfo = req.getPathInfo();
                mFromPathInfo = this.extractParametersFromPathInfo(pathInfo);
                this.log(String.format(""
                        + "path info %s%n"
                        + "path info splitted %s%n",
                        pathInfo,
                        mFromPathInfo.toString())
                );
            }
            if (!mFromPathInfo.isEmpty()) {
                mForProcessing = mFromPathInfo;
            }
        }
        if (mForProcessing.isEmpty()) {
            final Map<String, String> mFromParameterMap = new ConvertRequestParameterMap().xxx(req.getParameterMap());
            if (!mFromParameterMap.isEmpty()) {
                mForProcessing = mFromParameterMap;
            }
        }

        //---
        final String decoded;
        final Tuple<EncodedOrDecoded, String> umlInput;
        {
            final EncodedOrDecodedExtractor encodedOrDecodedExtractor = new EncodedOrDecodedExtractor();
            Optional<Tuple<EncodedOrDecoded, String>> xx = encodedOrDecodedExtractor.extractEncodedOrDecodedValue(mForProcessing);
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
            Optional<FileFormat> yy = fileFormatExtractor.extractFileFormat(mForProcessing);
            fileFormat = yy.orElse(FileFormat.PNG);
        }
        generateAndSendImage(req, resp, decoded, 0, fileFormat);
    }

    // TODO /url-path/get,post/params:encoded,format -> image
    // TODO /url-path/get,post/params:decoded,format -> image
    // /<servlet>/{format}/encoded
    // /<servlet>?format={format}&encoded={encoded}
    // /<servlet>/{format}?encoded={encoded}
    // /<servlet>/{encoded}?format={format}
    Map<String, String> extractParametersFromPathInfo(String pathInfo
    ) {
        Map<String, String> m = new HashMap<>();

        if (pathInfo != null) {
            String pathInfoNormalized = pathInfo.trim();
            if (pathInfoNormalized.startsWith("/")) {
                pathInfoNormalized = pathInfoNormalized.substring(1);
            }
            String[] pathInfoSplit = pathInfoNormalized.split("/", 5);
            if (pathInfoSplit.length == 1) {
                final String encoded = pathInfoSplit[0].trim();
                if (!encoded.isEmpty()) {
                    m.put("encoded", encoded);
                }
            } else if (pathInfoSplit.length == 2) {
                final String encoded = pathInfoSplit[0].trim();
                final String fileFormat = pathInfoSplit[1].trim();
                if (!encoded.isEmpty()) {
                    m.put("encoded", encoded);
                }
                if (!fileFormat.isEmpty()) {
                    m.put("fileFormat", fileFormat);
                }
            }
        }
        return m;
    }

    void generateAndSendImage(HttpServletRequest request,
            HttpServletResponse response,
            String uml,
            int idx,
            FileFormat ff
    ) {
        try {
            final PublicDiagramResponse diagramResponse = new PublicDiagramResponse(response, ff, request);
            diagramResponse.sendDiagram(uml, idx);
        } catch (IOException ioex) {
            throw new InProgressRuntimeException("generateAndSendImage", ioex);
        }
    }

}
