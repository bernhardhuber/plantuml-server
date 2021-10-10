/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.sourceforge.plantuml.servlet.bootstrap.Wrappers.Triple;
import net.sourceforge.plantuml.servlet.bootstrap.Wrappers.Tuple;

/**
 *
 * @author berni3
 */
class EncodedOrDecodedExtractor {

    enum EncodedOrDecoded {
        encoded, decoded;
    }

    Optional<Tuple<EncodedOrDecoded, String>> extractEncodedOrDecodedValue(Map<String, String> parameterMap) {
        final List<Triple<EncodedOrDecoded, String, String>> l = Arrays.asList(
                new Triple<>(EncodedOrDecoded.encoded, "encoded", null),
                new Triple<>(EncodedOrDecoded.decoded, "decoded", null),
                new Triple<>(EncodedOrDecoded.decoded, "text", null)
        );
        for (Triple<EncodedOrDecoded, String, String> t : l) {
            final String parameterValue = parameterMap.getOrDefault(t.getV(), null);
            t.setW(parameterValue);
        }
        final Optional<Tuple<EncodedOrDecoded, String>> res = l.stream()
                .filter(_t -> _t.getW() != null)
                .findFirst()
                .map(t -> new Tuple<EncodedOrDecoded, String>(t.getU(), t.getW()));
        return res;
    }
}
