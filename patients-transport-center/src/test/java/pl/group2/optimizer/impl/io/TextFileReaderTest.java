package pl.group2.optimizer.impl.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class TextFileReaderTest {
    TextFileReader textFileReader;

    void setUp(String fileName) {
        textFileReader = new TextFileReader(fileName);
    }

    @Test
    void should_throwMyException_whenFileDoesNotExist() {
        // given
        setUp("does not exist");

        // when
        Executable executable = () -> textFileReader.readData();

        //then
        assertThrows(MyException.class, executable);
    }

}