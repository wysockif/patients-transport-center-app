package pl.group2.optimizer.impl.structures.queues;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class QueueFIFOTest {
    private QueueFIFO<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new QueueFIFO<>();
    }

    @Test
    void should_addItem_when_itemIsInitialized() {
        // given
        Integer itemToAdd = 5;

        // when
        queue.add(itemToAdd);
        int actualSize = queue.size();

        // then
        int expectedSize = 1;
        assertEquals(expectedSize, actualSize);
    }

    @Test
    void should_throwIllegalArgumentException_when_givenItemToAddIsNull() {
        // given
        queue = new QueueFIFO<>();

        // when
        Integer itemToAdd = null;
        Executable executable = () -> queue.add(itemToAdd);

        // then
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void should_returnFrontItem_when_queueIsNotEmpty() {
        // given
        queue.add(10);
        queue.add(123);

        // when
        Integer actual = queue.front();

        // then
        int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    void should_throwNoSuchElementException_when_cannotReturnFrontItemFromEmptyQueue() {
        // given
        queue = new QueueFIFO<>();

        // when
        Executable executable = () -> queue.front();

        // then
        assertThrows(NoSuchElementException.class, executable);
    }

    @Test
    void should_popItem_when_queueIsNotEmpty() {
        // given
        queue.add(10);

        // when
        Integer actual = queue.remove();

        // then
        int expected = 10;
        assertEquals(expected, actual);
    }

    @Test
    void should_throwNoSuchElementException_when_cannotPopItemFromEmptyQueue() {
        // given
        queue = new QueueFIFO<>();

        // when
        Executable executable = () -> queue.remove();

        // then
        assertThrows(NoSuchElementException.class, executable);
    }

    @Test
    void should_returnTrue_when_queueIsEmpty() {
        // given
        queue = new QueueFIFO<>();

        // when
        boolean isEmpty = queue.isEmpty();

        // then
        assertTrue(isEmpty);
    }

    @Test
    void should_returnFalse_when_queueIsNotEmpty() {
        // given
        queue.add(10);

        // when
        boolean isEmpty = queue.isEmpty();

        // then
        assertFalse(isEmpty);
    }

    @Test
    void should_returnProperSize_when_queueIsEmpty() {
        // given
        queue = new QueueFIFO<>();

        // when
        int actualSize = queue.size();

        // then
        int expectedSize = 0;
        assertEquals(expectedSize, actualSize);
    }

    @Test
    void should_returnProperSize_when_queueIsNotEmpty() {
        // given
        queue.add(10);

        // when
        int actualSize = queue.size();

        // then
        int expectedSize = 1;
        assertEquals(expectedSize, actualSize);
    }

    @Test
    void should_returnTrue_when_queueContainsSpecifiedItem() {
        // given
        queue.add(10);

        // when
        boolean contains = queue.contains(10);

        // then
        assertTrue(contains);
    }

    @Test
    void should_returnFalse_when_queueDoesNotContainSpecifiedItem() {
        // given
        queue.add(10);

        // when
        boolean contains = queue.contains(100);

        // then
        assertFalse(contains);
    }

    @Test
    public void should_returnProperElements_when_givenItemsAreStringType() {
        // given
        QueueFIFO<String> queueFIFO = new QueueFIFO<>();
        queueFIFO.add("Pierwszy");
        queueFIFO.add("Drugi");

        // when
        String actualFirst = queueFIFO.remove();
        String actualSecond = queueFIFO.remove();

        // then
        String expectedFirst = "Pierwszy";
        String expectedSecond = "Drugi";
        assertEquals(expectedFirst, actualFirst);
        assertEquals(expectedSecond, actualSecond);
    }


    @Test
    public void should_returnProperElements_when_givenItemsAreDoubleType() {
        // given
        QueueFIFO<Double> queueFIFO = new QueueFIFO<>();
        queueFIFO.add(1.2);
        queueFIFO.add(3.4);

        // when
        Double actualFirst = queueFIFO.remove();
        Double actualSecond = queueFIFO.remove();

        // then
        Double expectedFirst = 1.2;
        Double expectedSecond = 3.4;
        assertEquals(expectedFirst, actualFirst);
        assertEquals(expectedSecond, actualSecond);
    }

}