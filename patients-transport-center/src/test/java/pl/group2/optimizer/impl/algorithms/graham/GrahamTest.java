package pl.group2.optimizer.impl.algorithms.graham;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.hospitals.Hospitals;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObject;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObjects;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class GrahamTest {
    private Graham graham;
    private Hospitals hospitals;
    private SpecialObjects specialObjects;

    @BeforeEach
    void setUp() {
        graham = new Graham();
        hospitals = new Hospitals();
        specialObjects = new SpecialObjects();
    }

    @Test
    void should_throwIllegalArgumentException_when_hospitalsAreUninitialized() {
        // given
        hospitals = null;

        // when
        Executable executable = () -> graham.loadPoints(hospitals, specialObjects);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void should_throwIllegalArgumentException_when_specialObjectsAreUninitialized() {
        // given
        specialObjects = null;

        // when
        Executable executable = () -> graham.loadPoints(hospitals, specialObjects);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void should_throwUnsupportedOperationException_when_pointsAreNotLoaded() {
        // given

        // when
        Executable executable = () -> graham.findConvexHull();

        // then
        assertThrows(UnsupportedOperationException.class, executable);
    }

    @Test
    void should_throwUnsupportedOperationException_when_thereIsNoPoints() {
        // given

        // when
        graham.loadPoints(hospitals, specialObjects);
        Executable executable = () -> graham.findConvexHull();

        // then
        assertThrows(UnsupportedOperationException.class, executable);
    }

    @Test
    void should_returnEmptyList_when_thereIsOnlyOnePoint() {
        // given
        int anyNumber = 0;
        Hospital hospital = new Hospital(anyNumber, valueOf(anyNumber), anyNumber, anyNumber, anyNumber, anyNumber);
        hospitals.add(hospital);

        // when
        graham.loadPoints(hospitals, specialObjects);
        List<Point> actualPoints = graham.findConvexHull();

        // then
        List<Point> expectedPoints = new LinkedList<>();
        assertThat(actualPoints).hasSameElementsAs(expectedPoints);
    }

    @Test
    void should_returnEmptyList_when_thereAreOnlyTwoPoints() {
        // given
        int anyFirstNumber = 0;
        int anySecondNumber = 4;
        Hospital hospital1 = new Hospital(anyFirstNumber, valueOf(anyFirstNumber), anyFirstNumber, anyFirstNumber, anyFirstNumber, anyFirstNumber);
        Hospital hospital2 = new Hospital(anySecondNumber, valueOf(anySecondNumber), anySecondNumber, anySecondNumber, anySecondNumber, anySecondNumber);
        hospitals.add(hospital1);
        hospitals.add(hospital2);

        // when
        graham.loadPoints(hospitals, specialObjects);
        List<Point> actualPoints = graham.findConvexHull();

        // then
        List<Point> expectedPoints = new LinkedList<>();
        assertThat(actualPoints).hasSameElementsAs(expectedPoints);
    }

    @Test
    void should_findConvexHull_when_thereAreAtLeastThreePoints() {
        // given
        int anyFirstNumber = 0;
        int anySecondNumber = -4;
        int anyThirdNumber = 9;
        Hospital hospital1 = new Hospital(anyFirstNumber, valueOf(anyFirstNumber), anyFirstNumber, anyFirstNumber, anyFirstNumber, anyFirstNumber);
        Hospital hospital2 = new Hospital(anySecondNumber, valueOf(anySecondNumber), anySecondNumber, anySecondNumber, anySecondNumber, anySecondNumber);
        SpecialObject specialObject = new SpecialObject(anyThirdNumber, valueOf(anyThirdNumber), anyThirdNumber, anyThirdNumber);
        hospitals.add(hospital1);
        hospitals.add(hospital2);
        specialObjects.add(specialObject);

        // when
        graham.loadPoints(hospitals, specialObjects);
        List<Point> actualPoints = graham.findConvexHull();

        // then
        List<Point> expectedPoints = Arrays.asList(hospital2, hospital1, specialObject);
        assertThat(actualPoints).hasSameElementsAs(expectedPoints);
    }


    @Test
    void should_findConvexHull_when_somePointsHaveTheSameCoordinates() {
        // given
        int anyFirstNumber = 0;
        int anySecondNumber = 4;
        Hospital hospital1 = new Hospital(anyFirstNumber, valueOf(anyFirstNumber), anyFirstNumber, anyFirstNumber, anyFirstNumber, anyFirstNumber);
        Hospital hospital2 = new Hospital(anySecondNumber, valueOf(anySecondNumber), anySecondNumber, anySecondNumber, anySecondNumber, anySecondNumber);
        SpecialObject specialObject = new SpecialObject(anyFirstNumber, valueOf(anyFirstNumber), anyFirstNumber, anyFirstNumber);
        hospitals.add(hospital1);
        hospitals.add(hospital2);
        specialObjects.add(specialObject);

        // when
        graham.loadPoints(hospitals, specialObjects);
        List<Point> actualPoints = graham.findConvexHull();

        // then
        List<Point> expectedPoints = Arrays.asList(hospital1, hospital2, specialObject);
        assertThat(actualPoints).hasSameElementsAs(expectedPoints);
    }
}