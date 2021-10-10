/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import javax.servlet.http.HttpServletRequest;

/**
 * Dump some {@link HttpServletRequest} getter values to a {@link String}.
 *
 * @author berni3
 */
public class HttpRequestDumper {

    String dump(HttpServletRequest req) {
        String res = String.format("Request%n"
                + "server name %s%n"
                + "method %s%n"
                + "request uri %s%n"
                + "contextPath %s%n"
                + "servletPath %s%n"
                + "pathInfo %s%n"
                + "pathTranslated %s%n",
                req.getServerName(),
                req.getMethod(),
                req.getRequestURI(),
                req.getContextPath(),
                req.getServletPath(),
                req.getPathInfo(),
                req.getPathTranslated()
        );
        return res;
    }
}
