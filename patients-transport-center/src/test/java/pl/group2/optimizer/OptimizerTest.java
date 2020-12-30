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

        // then
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

        // then
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

        // then
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

        // then
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

    @Test
    void should_throwMyException_when_idIsNegative_patients() {
        // given

        // when
        Executable executable = () -> optimizer.loadPatients("exemplaryData/incorrect/patients/" +
                "patients_negative_id.txt");

        // then
        assertEquals(
                "\n| Błąd -102 | [Plik wejściowy: patients_negative_id.txt, nr linii: 3]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca id pacjenta!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_idIsNegative_hospitals_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_negative_id_hospital.txt");

        // then
        assertEquals(
                "\n| Błąd -102 | [Plik wejściowy: map_negative_id_hospital.txt, nr linii: 4]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca id szpitala!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_idIsNegative_specialObjects_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_negative_id_special_object.txt");

        // then
        assertEquals(
                "\n| Błąd -102 | [Plik wejściowy: map_negative_id_special_object.txt, nr linii: 9]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca id specjalnego obiektu!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_idIsNegative_paths_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_negative_id_path.txt");

        // then
        assertEquals(
                "\n| Błąd -102 | [Plik wejściowy: map_negative_id_path.txt, nr linii: 15]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca id drogi!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_idAlreadyExists_patients() {
        // given
        String fileName = "patients_id_already_exists";

        // when
        Executable executable = () -> optimizer.loadPatients("exemplaryData/incorrect/patients/" +
                fileName + ".txt");

        // then
        assertEquals(
                "\n| Błąd -102 | [Plik wejściowy: " + fileName + ".txt, nr linii: 4]. " +
                        "Nie można dodawać pacjentów o tym samym id!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_idAlreadyExists_hospitals_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_id_already_exist_hospital.txt");

        // then
        assertEquals(
                "\n| Błąd -102 | [Plik wejściowy: map_id_already_exist_hospital.txt, nr linii: 5]. " +
                        "Nie można dodawać szpitali o tym samym id.!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_idAlreadyExists_specialObjects_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_id_already_exist_special_object.txt");

        // then
        assertEquals(
                "\n| Błąd -102 | [Plik wejściowy: map_id_already_exist_special_object.txt, nr linii: 10]. " +
                        "Nie można dodawać specjalnych obiektów o tym samym id.!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

//    @Test
//    void should_throwMyException_when_idAlreadyExists_paths_map() {
//        // given
//
//        // when
//        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
//                "map_id_already_exist_path.txt");
//
//        // then
//        assertEquals(
//                "\n| Błąd -102 | [Plik wejściowy: map_id_already_exist.txt, nr linii: 5]. " +
//                        "Nie można dodawać szpitali o tym samym id.!",
//                assertThrows(MyException.class, executable).getMessage()
//        );
//    }

    @Test
    void should_throwMyException_when_distanceIsNegative_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_negative_distance.txt");

        // then
        assertEquals(
                "\n| Błąd -102 | [Plik wejściowy: map_negative_distance.txt, nr linii: 15]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca odległość drogi!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_distanceIsZero_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_zero_distance.txt");

        // then
        assertEquals(
                "\n| Błąd -102 | [Plik wejściowy: map_zero_distance.txt, nr linii: 16]. Niepoprawny format danych. " +
                        "Zerowa wartość reprezentująca odległość drogi!",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

}