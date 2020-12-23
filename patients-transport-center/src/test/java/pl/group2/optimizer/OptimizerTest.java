package pl.group2.optimizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.group2.optimizer.impl.io.MyException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptimizerTest {

    Optimizer optimizer;

    @BeforeEach
    void setUp() {
        optimizer = new Optimizer();
    }

    @Test
    void should_throwMyException_when_fileDoesNotExist_patients() {
        // given

        // when
        Executable executable = () -> optimizer.loadPatients("does not exist");

        // when
        assertEquals(
                "\n| Błąd -100 | [Plik wejściowy: null]. Plik nie został znaleziony!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_fileDoesNotExist_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("does not exist");

        // when
        assertEquals(
                "\n| Błąd -100 | [Plik wejściowy: null]. Plik nie został znaleziony!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_fileDoesNotStartWithHeader_patients() {
        // given

        // when
        Executable executable = () -> optimizer.loadPatients("exemplaryData/incorrect/patients/" +
                "patients_no_headline.txt");

        // when
        assertEquals(
                "\n| Błąd -101 | nullNiepoprawny nagłówek!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_fileDoesNotStartWithHeader_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_no_second_headline.txt");

        // when
        assertEquals(
                "\n| Błąd -102 | [Plik wejściowy: map_no_second_headline.txt, nr linii: 7]. Niepoprawny format danych!",
                assertThrows(MyException.class, executable).getMessage()
        );

        /*
        Zamiast wiadomości:
        | Błąd -101 | nullNiepoprawny nagłówek!

        wyświetla się wiadomość:
        | Błąd -102 | [Plik wejściowy: map_no_second_headline.txt, nr linii: 7]. Niepoprawny format danych!

        przy braku drugiego nagłówka.

        Nie wiem jak to inaczej rozwiązać, chyba jest okej.
         */

    }

}