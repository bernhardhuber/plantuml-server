/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * Converter from {@link HttpServletRequest#getParameterMap()} of type
 * {@code Map<String,String[]>} to {@code Map<String,String>}.
 *
 * @author berni3
 */
public class ConvertRequestParameterMap {

    /**
     * Convert {@code Map<String,String[]>} to {@code Map<String,String>}.
     * <p>
     * Filter by following criteria:
     * <pre>
     * allow only entries
     * WHERE
     * key != null and
     * value has at least one value
     * </pre>
     *
     * @param m source map value
     * @return source map converted to {@code Map<String,String>}
     */
    public Map<String, String> convertMapFromArrayOfValueToSingleValue(Map<String, String[]> m) {
        final Map<String, String> mFromParameterMap = new HashMap<>();
        // stream implementation variant
        // allow only entries WHERE
        //   key != null and
        //   value has at least one value
        m.entrySet().stream()
                .filter((_e) -> _e.getKey() != null)
                .filter((_e) -> _e.getValue() != null && _e.getValue().length > 0)
                .forEach((_e) -> {
                    mFromParameterMap.put(_e.getKey(), _e.getValue()[0]);
                });
        return mFromParameterMap;
    }
}
