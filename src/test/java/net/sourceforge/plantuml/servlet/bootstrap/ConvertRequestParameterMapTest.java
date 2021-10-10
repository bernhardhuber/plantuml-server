/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class ConvertRequestParameterMapTest {

    public ConvertRequestParameterMapTest() {
    }

    /**
     * Test of convertMapFromArrayOfValueToSingleValue method, of class
     * ConvertRequestParameterMap.
     */
    @Test
    public void testConvertMapFromArrayOfValueToSingleValue() {
        Map<String, String[]> m = new HashMap<>();
        ConvertRequestParameterMap instance = new ConvertRequestParameterMap();
        Map<String, String> result = instance.convertMapFromArrayOfValueToSingleValue(m);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    /**
     * Test of convertMapFromArrayOfValueToSingleValue method, of class
     * ConvertRequestParameterMap.
     */
    @Test
    public void testConvertMapFromArrayOfValueToSingleValue_encoded() {
        final Map<String, String[]> m = new HashMap<>();
        m.put("encoded", new String[]{"some-encoded-value"});
        final ConvertRequestParameterMap instance = new ConvertRequestParameterMap();
        final Map<String, String> result = instance.convertMapFromArrayOfValueToSingleValue(m);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("some-encoded-value", result.get("encoded"));
        assertEquals(null, result.get("fileFormat"));
    }

    /**
     * Test of convertMapFromArrayOfValueToSingleValue method, of class
     * ConvertRequestParameterMap.
     */
    @Test
    public void testConvertMapFromArrayOfValueToSingleValue_fileFormat_encoded() {
        final Map<String, String[]> m = new HashMap<>();
        m.put("encoded", new String[]{"some-encoded-value"});
        m.put("fileFormat", new String[]{"some-fileFormat-value"});
        final ConvertRequestParameterMap instance = new ConvertRequestParameterMap();
        final Map<String, String> result = instance.convertMapFromArrayOfValueToSingleValue(m);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("some-encoded-value", result.get("encoded"));
        assertEquals("some-fileFormat-value", result.get("fileFormat"));
    }

}
