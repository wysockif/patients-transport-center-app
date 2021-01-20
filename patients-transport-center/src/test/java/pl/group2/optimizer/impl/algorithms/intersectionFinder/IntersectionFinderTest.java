package pl.group2.optimizer.impl.algorithms.intersectionFinder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.group2.optimizer.impl.items.Vertex;
import pl.group2.optimizer.impl.items.area.Point;
import pl.group2.optimizer.impl.items.hospitals.Hospital;
import pl.group2.optimizer.impl.items.intersections.Intersection;
import pl.group2.optimizer.impl.items.paths.Path;
import pl.group2.optimizer.impl.items.specialobjects.SpecialObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class IntersectionFinderTest {
    private IntersectionFinder finder;
    private List<Path> paths;

    @BeforeEach
    void setUp() {
        paths = new LinkedList<>();
    }

    @Test
    void should_throwIllegalArgumentException_when_pathsAreUninitialized() {
        // given
        paths = null;
        int maxId = 11;

        // when
        Executable executable = () -> finder = new IntersectionFinder(paths, maxId);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void should_returnEmptyList_ifListOfPathsIsEmpty() {
        // given
        int maxId = 11;
        finder = new IntersectionFinder(paths,maxId);

        // when
        finder.findIntersectionsNaive();
        List<Intersection> actualIntersections = finder.getIntersectionsList();

        // then
        List<Intersection> expectedIntersections = new LinkedList<>();
        assertThat(actualIntersections).hasSameElementsAs(expectedIntersections);
    }

    @Test
    void should_returnEmptyList_when_thereIsOnlyOnePath() {
        // given
        int maxId = 11;
        int anyNumber = 0;
        int anyNumber2 = 1;
        Hospital hospital1 = new Hospital(anyNumber, valueOf(anyNumber), anyNumber, anyNumber, anyNumber, anyNumber);
        Hospital hospital2 = new Hospital(anyNumber2, valueOf(anyNumber2), anyNumber2, anyNumber2, anyNumber2, anyNumber2);
        Path path = new Path(anyNumber, hospital1, hospital2, anyNumber+100);
        paths.add(path);

        // when
        finder = new IntersectionFinder(paths,maxId);
        finder.findIntersectionsNaive();
        List<Intersection> actualIntersections = finder.getIntersectionsList();

        // then
        List<Intersection> expectedIntersections = new LinkedList<>();
        assertThat(actualIntersections).hasSameElementsAs(expectedIntersections);
    }

    @Test
    void should_returnOnlyOneIntersection_when_twoPathsIntersects() {
        // given
        int maxId = 0;
        int hospitalId = 0;

        int firstX1 = 10;
        int firstY1 = 10;
        int firstX2 = 70;
        int firstY2 = 70;

        int secondX1 = 35;
        int secondY1 = 90;
        int secondX2 = 35;
        int secondY2 = 0;

        Hospital hospital1 = new Hospital(hospitalId++, valueOf(hospitalId),firstX1,firstY1,hospitalId,hospitalId);
        Hospital hospital2 = new Hospital(hospitalId++, valueOf(hospitalId),firstX2,firstY2,hospitalId,hospitalId);
        Path path1 = new Path(maxId++, hospital1, hospital2, maxId++);

        Hospital hospital3 = new Hospital(hospitalId++, valueOf(hospitalId),secondX1,secondY1,hospitalId,hospitalId);
        Hospital hospital4 = new Hospital(hospitalId++, valueOf(hospitalId),secondX2,secondY2,hospitalId,hospitalId);
        Path path2 = new Path(maxId++, hospital3, hospital4, maxId++);

        paths.add(path1);
        paths.add(path2);

        // when
        finder = new IntersectionFinder(paths,maxId);
        finder.findIntersectionsNaive();
        List<Intersection> actualIntersections = finder.getIntersectionsList();

        // then
        int expectedSize = 1;
        assertEquals(expectedSize,actualIntersections.size());
    }

    @Test
    void should_returnEmptyList_when_twoPathsNotIntersects() {
        // given
        int maxId = 0;
        int hospitalId = 0;

        int firstX1 = 10;
        int firstY1 = 10;
        int firstX2 = 70;
        int firstY2 = 70;

        int secondX1 = 10;
        int secondY1 = 20;
        int secondX2 = 80;
        int secondY2 = 140;

        Hospital hospital1 = new Hospital(hospitalId++, valueOf(hospitalId),firstX1,firstY1,hospitalId,hospitalId);
        Hospital hospital2 = new Hospital(hospitalId++, valueOf(hospitalId),firstX2,firstY2,hospitalId,hospitalId);
        Path path1 = new Path(maxId++, hospital1, hospital2, maxId++);

        Hospital hospital3 = new Hospital(hospitalId++, valueOf(hospitalId),secondX1,secondY1,hospitalId,hospitalId);
        Hospital hospital4 = new Hospital(hospitalId++, valueOf(hospitalId),secondX2,secondY2,hospitalId,hospitalId);
        Path path2 = new Path(maxId++, hospital3, hospital4, maxId++);

        paths.add(path1);
        paths.add(path2);

        // when
        finder = new IntersectionFinder(paths,maxId);
        finder.findIntersectionsNaive();
        List<Intersection> actualIntersections = finder.getIntersectionsList();

        // then
        List<Intersection> expectedIntersections = new LinkedList<>();
        assertThat(actualIntersections).hasSameElementsAs(expectedIntersections);
    }

    @Test
    void should_returnEmptyList_when_pathsHaveTheSameEndpoint() {
        // given
        int maxId = 0;
        int hospitalId = 0;

        int firstX1 = 10;
        int firstY1 = 10;
        int firstX2 = 70;
        int firstY2 = 70;

        int secondX1 = 10;
        int secondY1 = 10;
        int secondX2 = 80;
        int secondY2 = 140;

        Hospital hospital1 = new Hospital(hospitalId++, valueOf(hospitalId),firstX1,firstY1,hospitalId,hospitalId);
        Hospital hospital2 = new Hospital(hospitalId++, valueOf(hospitalId),firstX2,firstY2,hospitalId,hospitalId);
        Path path1 = new Path(maxId++, hospital1, hospital2, 100);

        Hospital hospital3 = new Hospital(hospitalId++, valueOf(hospitalId),secondX1,secondY1,hospitalId,hospitalId);
        Hospital hospital4 = new Hospital(hospitalId++, valueOf(hospitalId),secondX2,secondY2,hospitalId,hospitalId);
        Path path2 = new Path(maxId++, hospital3, hospital4, 100);

        paths.add(path1);
        paths.add(path2);

        // when
        finder = new IntersectionFinder(paths,maxId);
        finder.findIntersectionsNaive();
        List<Intersection> actualIntersections = finder.getIntersectionsList();

        // then
        List<Intersection> expectedIntersections = new LinkedList<>();
        assertThat(actualIntersections).hasSameElementsAs(expectedIntersections);
    }

}

