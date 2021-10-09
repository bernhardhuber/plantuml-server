/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import net.sourceforge.plantuml.FileFormat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class FileFormatExtractorTest {

    /**
     * Test of extractFileFormat method, of class FileFormatExtractor.
     */
    @Test
    public void testExtractFileFormat_no_matching_name() {
        final Map<String, String[]> parameterMap = new HashMap<>();

        final FileFormatExtractor instance = new FileFormatExtractor();

        final Optional<FileFormat> result = instance.extractFileFormat(parameterMap);
        assertNotNull(result);
        assertFalse(result.isPresent());
    }

    /**
     * Test of extractFileFormat method, of class FileFormatExtractor.
     */
    @Test
    public void testExtractFileFormat_fileFormat() {
        final Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("fileFormat", new String[]{"PNG"});

        final FileFormatExtractor instance = new FileFormatExtractor();

        final Optional<FileFormat> result = instance.extractFileFormat(parameterMap);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(FileFormat.PNG, result.get());
    }

    /**
     * Test of extractFileFormat method, of class FileFormatExtractor.
     */
    @Test
    public void testExtractFileFormat_format() {
        final Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("format", new String[]{"PNG"});

        final FileFormatExtractor instance = new FileFormatExtractor();

        final Optional<FileFormat> result = instance.extractFileFormat(parameterMap);
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(FileFormat.PNG, result.get());
    }

}
