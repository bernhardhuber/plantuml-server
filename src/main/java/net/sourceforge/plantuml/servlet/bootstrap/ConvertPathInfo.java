/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author berni3
 */
public class ConvertPathInfo {
    // TODO /url-path/get,post/params:encoded,format -> image
    // TODO /url-path/get,post/params:decoded,format -> image
    // /<servlet>/{format}/encoded
    // /<servlet>?format={format}&encoded={encoded}
    // /<servlet>/{format}?encoded={encoded}
    // /<servlet>/{encoded}?format={format}

    public Map<String, String> extractParametersFromPathInfo(String pathInfo) {
        Map<String, String> m = new HashMap<>();

        if (pathInfo != null) {
            String pathInfoNormalized = pathInfo.trim();
            if (pathInfoNormalized.startsWith("/")) {
                pathInfoNormalized = pathInfoNormalized.substring(1);
            }
            final String[] pathInfoSplit = pathInfoNormalized.split("/", 5);
            if (pathInfoSplit.length == 1) {
                // handle "/{encoded}"
                final String encoded = pathInfoSplit[0].trim();
                if (!encoded.isEmpty()) {
                    m.put("encoded", encoded);
                }
            } else if (pathInfoSplit.length == 2) {
                // handle "/{fileFormat}/{encoded}
                final String fileFormat = pathInfoSplit[0].trim();
                final String encoded = pathInfoSplit[1].trim();
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

}
