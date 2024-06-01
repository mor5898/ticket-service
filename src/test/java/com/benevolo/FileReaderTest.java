package com.benevolo;

import com.benevolo.includes.utils.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FileReaderTest {

    @Test
    void testRead() throws IOException {

        //when
        String result = FileReader.read("request_bodies/test/testfile.txt");

        //then
        Assertions.assertEquals("This is a test", result);

    }

}
