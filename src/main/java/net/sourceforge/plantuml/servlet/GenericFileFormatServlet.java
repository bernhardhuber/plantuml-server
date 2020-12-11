/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.plantuml.FileFormat;

/**
 *
 * @author berni3
 */
public class GenericFileFormatServlet extends UmlDiagramService {

     private HttpServletRequest myRequest;

    @Override
    public FileFormat getOutputFormat() {
        FileFormat fileFormat = extractFileFormat(myRequest);
        return fileFormat;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.myRequest = request;
        super.doPost(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.myRequest = request;
        super.doGet(request, response);
    }

    FileFormat extractFileFormat(HttpServletRequest request) {
        FileFormat fileFormat = FileFormat.PNG;
        String fileFormatAsString = request.getParameter("fileFormat");
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
