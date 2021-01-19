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
                "[Plik wejściowy: null]. Plik nie został znaleziony",
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
                "[Plik wejściowy: null]. Plik nie został znaleziony",
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
                "nullNiepoprawny nagłówek",
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
                "[Plik wejściowy: map_no_second_headline.txt, nr linii: 7]. Niepoprawny format danych",
                assertThrows(MyException.class, executable).getMessage()
        );

    }

    @Test
    void should_throwMyException_when_idIsNegative_patients() {
        // given
        optimizer.createWindow(optimizer);

        // when
        Executable executable = () -> optimizer.loadPatients("exemplaryData/incorrect/patients/" +
                "patients_negative_id.txt");

        // then
        assertEquals(
                "[Plik wejściowy: patients_negative_id.txt, nr linii: 3]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca id pacjenta",
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
                "[Plik wejściowy: map_negative_id_hospital.txt, nr linii: 4]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca id szpitala",
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
                "[Plik wejściowy: map_negative_id_special_object.txt, nr linii: 9]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca id specjalnego obiektu",
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
                "[Plik wejściowy: map_negative_id_path.txt, nr linii: 15]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca id drogi",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_idAlreadyExists_patients() {
        // given
        String fileName = "patients_id_already_exists";
        optimizer.createWindow(optimizer);

        // when
        Executable executable = () -> optimizer.loadPatients("exemplaryData/incorrect/patients/" +
                fileName + ".txt");

        // then
        assertEquals(
                "[Plik wejściowy: patients_id_already_exists.txt, nr linii: 4]. Nie można dodawać pacjentów o tym samym id",
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
                "[Plik wejściowy: map_id_already_exist_hospital.txt, nr linii: 5]. " +
                        "Nie można dodawać szpitali o tym samym id.",
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
                "[Plik wejściowy: map_id_already_exist_special_object.txt, nr linii: 10]. " +
                        "Nie można dodawać specjalnych obiektów o tym samym id.",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_distanceIsNegative_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_negative_distance.txt");

        // then
        assertEquals(
                "[Plik wejściowy: map_negative_distance.txt, nr linii: 15]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca odległość drogi",
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
                "[Plik wejściowy: map_zero_distance.txt, nr linii: 16]. " +
                        "Niepoprawny format danych. Zerowa wartość reprezentująca odległość drogi",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_IntegerValueIsTooBig() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/" +
                "max_int_value.txt");

        // then
        assertEquals(
                "[Plik wejściowy: max_int_value.txt, nr linii: 13]. Nieudana konwersja danej: \"2147483648\"",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_numberOfBedsIsNegative_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_beds_are_negative.txt");

        // then
        assertEquals(
                "[Plik wejściowy: map_beds_are_negative.txt, nr linii: 4]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca liczbę łóżek szpitala.",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_numberOfAvailableBedsIsNegative_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_beds_are_negative2.txt");

        // then
        assertEquals(
                "[Plik wejściowy: map_beds_are_negative2.txt, nr linii: 2]. " +
                        "Niepoprawny format danych. Ujemna wartość reprezentująca liczbę łóżek szpitala.",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_xHasFloatValue_map() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/map/" +
                "map_float_hospitals");

        // then
        assertEquals(
                "[Plik wejściowy: map_float_hospitals, nr linii: 5]. Nieudana konwersja danej: \"10.2\"",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

    @Test
    void should_throwMyException_when_xHasFloatValue_patients() {
        // given

        // when
        Executable executable = () -> optimizer.loadMap("exemplaryData/incorrect/patients/" +
                "patients_float");

        // then
        assertEquals(
                "[Plik wejściowy: patients_float, nr linii: 2]. Niepoprawny format danych",
                assertThrows(MyException.class, executable).getMessage()
        );
    }

}