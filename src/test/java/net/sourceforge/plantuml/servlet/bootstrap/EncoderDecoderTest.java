/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class EncoderDecoderTest {

    @Test
    public void testEncode() {
        EncoderDecoder encoderDecoder = new EncoderDecoder();
        String unencoded = "@startuml\n"
                + "Alice --> Bob: hello\n"
                + "@enduml";
        String result = encoderDecoder.encode(unencoded);
        result = result.replaceAll("[\\r\\n]+", " ");
        System.out.format("testEncode%n"
                + "unencoded: %s%n"
                + "result: %s%n", unencoded, result);
        assertNotNull(result);
        assertEquals("Syp9J4vLqDMrKt3AJx9Io4ZDoSa70000", result);
    }

    @Test
    public void testDecode() {
        EncoderDecoder encoderDecoder = new EncoderDecoder();
        String undecoded = "SyfFKj2rKt3CoKnELR1Io4ZDoSa70000";
        String result = encoderDecoder.decode(undecoded);
        result = result.replaceAll("[\\r\\n]+", " ");
        System.out.format("testDecode%n"
                + "undecoded: %s%n"
                + "result: %s%n", undecoded, result);
        assertNotNull(result);
        assertEquals("@startuml Bob -> Alice : hello @enduml", result);
    }

    @Test
    public void testLoopingEncodeDecode() {
        final String input = "@start Lorem ipsum @end";
        final EncoderDecoder encoderDecoder = new EncoderDecoder();

        final String unencoded = input;

        final String encoded1 = encoderDecoder.encode(unencoded);
        final String decoded1 = encoderDecoder.decode(encoded1);
        assertEquals(input, decoded1);

        final String encoded2 = encoderDecoder.encode(decoded1);
        final String decoded2 = encoderDecoder.decode(encoded2);
        assertEquals(input, decoded2);
    }
}
