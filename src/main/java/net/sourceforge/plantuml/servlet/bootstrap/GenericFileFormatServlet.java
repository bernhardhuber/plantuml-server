/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.servlet.UmlDiagramService;

/**
 *
 * @author berni3
 */
@WebServlet(name = "GenericFileFormatServlet",
        urlPatterns = {"/genericfileformat/*"}
)
public class GenericFileFormatServlet extends UmlDiagramService {

    // use threadlocal for outputFormat value
    // as in #getOutputFormat() there is no HttpRequest anymore
    private static ThreadLocal<String> outputFormatContext = ThreadLocal.withInitial(() -> null);
    private final HttpRequestDumper httpRequestDumper = new HttpRequestDumper();

    @Override
    public FileFormat getOutputFormat() {
        final String outputFormat = outputFormatContext.get();
        final FileFormat fileFormat = convertToFileFormat(outputFormat);
        return fileFormat;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.log(httpRequestDumper.dump(request));
        try {
            outputFormatContext.set(request.getParameter("fileFormat"));
            super.doPost(request, response);
        } finally {
            // clean thread local
            outputFormatContext.remove();
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.log(httpRequestDumper.dump(request));
        try {
            outputFormatContext.set(request.getParameter("fileFormat"));
            super.doGet(request, response);
        } finally {
            // clean thread local
            outputFormatContext.remove();
        }

    }

    FileFormat convertToFileFormat(String fileFormatAsString) {
        FileFormat fileFormat = FileFormat.PNG;
        if (fileFormatAsString != null) {
            try {
                fileFormat = FileFormat.valueOf(fileFormatAsString);
            } catch (java.lang.IllegalArgumentException iaex) {
                final String m = String.format("Bad fileFormat: %s, allowed fileFormat values: %s",
                        fileFormatAsString, Arrays.asList(FileFormat.values()));
                this.getServletContext().log(m, iaex);
            }
        }
        return fileFormat;
    }

}
