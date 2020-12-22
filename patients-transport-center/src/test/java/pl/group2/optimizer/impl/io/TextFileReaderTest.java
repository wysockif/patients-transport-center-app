package pl.group2.optimizer.impl.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextFileReaderTest {
    TextFileReader textFileReader;

    void setUp(String fileName) {
        textFileReader = new TextFileReader(fileName);
    }

    @Test
    void should_throwMyException_whenFileDoesNotExist() {
        setUp("does not exist");
        textFileReader.readData();
    }

}