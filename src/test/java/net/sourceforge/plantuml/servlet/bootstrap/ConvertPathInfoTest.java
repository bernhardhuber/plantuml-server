/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class ConvertPathInfoTest {

    public ConvertPathInfoTest() {
    }

    /**
     * Test of extractParametersFromPathInfo method, of class ConvertPathInfo.
     */
    @Test
    public void testExtractParametersFromPathInfo_encoded() {
        final String pathInfo = "/some-encoded-data";
        final ConvertPathInfo instance = new ConvertPathInfo();
        final Map<String, String> result = instance.extractParametersFromPathInfo(pathInfo);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("some-encoded-data", result.get("encoded"));
        assertEquals(null, result.get("fileFormat"));
    }

    /**
     * Test of extractParametersFromPathInfo method, of class ConvertPathInfo.
     */
    @Test
    public void testExtractParametersFromPathInfo_fileformat_encoded() {
        final String pathInfo = "/some-file-format/some-encoded-data";
        final ConvertPathInfo instance = new ConvertPathInfo();
        final Map<String, String> result = instance.extractParametersFromPathInfo(pathInfo);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("some-encoded-data", result.get("encoded"));
        assertEquals("some-file-format", result.get("fileFormat"));
    }

}
