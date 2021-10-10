/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author berni3
 */
public class ConvertRequestParameterMap {

    Map<String, String> xxx(Map<String, String[]> m) {
        final Map<String, String> mFromParameterMap = new HashMap<>();
        {
            if (1 == 0) {
                for (Entry<String, String[]> e : m.entrySet()) {
                    if (e.getKey() != null
                            && e.getValue() != null
                            && e.getValue().length > 0) {
                        mFromParameterMap.put(e.getKey(), e.getValue()[0]);
                    }
                }
            } else {
                m.entrySet().stream()
                        .filter((_e) -> _e.getKey() != null)
                        .filter((_e) -> _e.getValue() != null && _e.getValue().length > 0)
                        .forEach((_e) -> {
                            mFromParameterMap.put(_e.getKey(), _e.getValue()[0]);
                        });
            }
        }
        return mFromParameterMap;
    }
}
