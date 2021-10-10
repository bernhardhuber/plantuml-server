/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.io.IOException;
import java.util.Collections;
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
import net.sourceforge.plantuml.servlet.bootstrap.GenericWrappers.Tuple;

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
        req.setCharacterEncoding("UTF-8");
        generateAndSend(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.log(httpRequestDumper.dump(req));
        req.setCharacterEncoding("UTF-8");
        generateAndSend(req, resp);
    }

    void generateAndSend(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String> mForProcessing = Collections.emptyMap();
        // handle pathInfo
        if (mForProcessing.isEmpty()) {
            final Map<String, String> mFromPathInfo;
            {
                final String pathInfo = req.getPathInfo();
                final ConvertPathInfo convertPathInfo = new ConvertPathInfo();
                mFromPathInfo = convertPathInfo.extractParametersFromPathInfo(pathInfo);
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
        // handle parameterMap
        if (mForProcessing.isEmpty()) {
            final ConvertRequestParameterMap convertRequestParameterMap = new ConvertRequestParameterMap();
            final Map<String, String> mFromParameterMap
                    = convertRequestParameterMap.convertMapFromArrayOfValueToSingleValue(req.getParameterMap());
            if (!mFromParameterMap.isEmpty()) {
                mForProcessing = mFromParameterMap;
            }
        }

        //---
        // extract decoded uml text
        final String decoded;
        {
            final EncodedOrDecodedExtractor encodedOrDecodedExtractor = new EncodedOrDecodedExtractor();
            Optional<Tuple<EncodedOrDecoded, String>> encodedOrDecodedValueOpt
                    = encodedOrDecodedExtractor.extractEncodedOrDecodedValue(mForProcessing);

            final Tuple<EncodedOrDecoded, String> umlInput
                    = encodedOrDecodedValueOpt.orElseGet(() -> {
                        String _uml = umlDefault;
                        final Tuple<EncodedOrDecoded, String> _t = new Tuple<>(EncodedOrDecoded.decoded, _uml);
                        return _t;
                    });
            // convert if uml text is encoded
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
        // extract and convert file format
        final FileFormat fileFormat;
        {
            final FileFormatExtractor fileFormatExtractor = new FileFormatExtractor();
            final Optional<FileFormat> fileFormatOpt = fileFormatExtractor.extractFileFormat(mForProcessing);
            fileFormat = fileFormatOpt.orElse(FileFormat.PNG);
        }
        generateImageAndSend(req, resp, decoded, 0, fileFormat);
    }

    void generateImageAndSend(HttpServletRequest request,
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
