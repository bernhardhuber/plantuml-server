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
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.servlet.bootstrap.GenericWrappers.Tuple;

/**
 * Extract {@link FileFormat}.
 *
 * @author berni3
 */
class FileFormatExtractor {

    public Optional<FileFormat> extractFileFormat(Map<String, String> parameterMap) {
        final List<Tuple<String, FileFormat>> l = Arrays.asList(
                new Tuple<>("format", null),
                new Tuple<>("fileFormat", null)
        );
        for (Tuple<String, FileFormat> t : l) {
            final String parameterValue = parameterMap.getOrDefault(t.getU(), null);
            if (parameterValue == null) {
                continue;
            }
            //---
            FileFormat fileFormat = null;
            try {
                fileFormat = FileFormat.valueOf(parameterValue);
            } catch (IllegalArgumentException iaex) {
                fileFormat = null;
            }
            t.setV(fileFormat);
        }
        //---
        final Optional<FileFormat> fileFormatOpt = l.stream()
                .filter(_t -> _t.getV() != null)
                .findFirst()
                .map(_t -> _t.getV());
        return fileFormatOpt;
    }

}
