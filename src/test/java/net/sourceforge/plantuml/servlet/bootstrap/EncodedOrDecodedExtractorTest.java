/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.sourceforge.plantuml.servlet.bootstrap.EncodedOrDecodedExtractor.EncodedOrDecoded;
import net.sourceforge.plantuml.servlet.bootstrap.Wrappers.Tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class EncodedOrDecodedExtractorTest {

    public EncodedOrDecodedExtractorTest() {
    }

    /**
     * Test of extractEncodedOrDecodedValue method, of class
     * EncodedOrDecodedExtractor.
     */
    @Test
    public void testExtractEncodedOrDecodedValue_encoded() {
        final Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("encoded", "some_encoded_value");

        final EncodedOrDecodedExtractor instance = new EncodedOrDecodedExtractor();
        final Optional<Tuple<EncodedOrDecoded, String>> result = instance.extractEncodedOrDecodedValue(parameterMap);
        assertNotNull(result);
        assertEquals(true, result.isPresent());
        assertEquals(EncodedOrDecoded.encoded, result.get().getU());
        assertEquals("some_encoded_value", result.get().getV());
    }

    /**
     * Test of extractEncodedOrDecodedValue method, of class
     * EncodedOrDecodedExtractor.
     */
    @Test
    public void testExtractEncodedOrDecodedValue_decoded() {
        final Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("decoded", "some_decoded_value");

        final EncodedOrDecodedExtractor instance = new EncodedOrDecodedExtractor();
        final Optional<Tuple<EncodedOrDecoded, String>> result = instance.extractEncodedOrDecodedValue(parameterMap);
        assertNotNull(result);
        assertEquals(true, result.isPresent());
        assertEquals(EncodedOrDecoded.decoded, result.get().getU());
        assertEquals("some_decoded_value", result.get().getV());
    }

    /**
     * Test of extractEncodedOrDecodedValue method, of class
     * EncodedOrDecodedExtractor.
     */
    @Test
    public void testExtractEncodedOrDecodedValue_text() {
        final Map<String, String> parameterMap = new HashMap<>();
        parameterMap.put("text", "some_decoded_value");

        final EncodedOrDecodedExtractor instance = new EncodedOrDecodedExtractor();
        final Optional<Tuple<EncodedOrDecoded, String>> result = instance.extractEncodedOrDecodedValue(parameterMap);
        assertNotNull(result);
        assertEquals(true, result.isPresent());
        assertEquals(EncodedOrDecoded.decoded, result.get().getU());
        assertEquals("some_decoded_value", result.get().getV());
    }

}
