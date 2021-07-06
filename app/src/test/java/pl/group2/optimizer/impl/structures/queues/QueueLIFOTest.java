package pl.group2.optimizer.impl.structures.queues;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class QueueLIFOTest {
    private QueueLIFO<Integer> queue;

    @BeforeEach
    void setUp() {
        queue = new QueueLIFO<>();
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
        Integer itemToAdd = null;

        // when
        Executable executable = () -> queue.add(itemToAdd);

        // then
        assertThrows(IllegalArgumentException.class, executable);
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

        // when
        Executable executable = () -> queue.remove();

        // then
        assertThrows(NoSuchElementException.class, executable);
    }

    @Test
    void should_returnTrue_when_queueIsEmpty() {
        // given

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
        QueueLIFO<String> queueLIFO = new QueueLIFO<>();
        queueLIFO.add("Pierwszy");
        queueLIFO.add("Drugi");

        // when
        String actualFirst = queueLIFO.remove();
        String actualSecond = queueLIFO.remove();

        // then
        String expectedFirst = "Drugi";
        String expectedSecond = "Pierwszy";
        assertEquals(expectedFirst, actualFirst);
        assertEquals(expectedSecond, actualSecond);
    }


    @Test
    public void should_returnProperElements_when_givenItemsAreDoubleType() {
        // given
        QueueLIFO<Double> queueLIFO = new QueueLIFO<>();
        queueLIFO.add(3.4);
        queueLIFO.add(1.2);

        // when
        Double actualFirst = queueLIFO.remove();
        Double actualSecond = queueLIFO.remove();

        // then
        Double expectedFirst = 1.2;
        Double expectedSecond = 3.4;
        assertEquals(expectedFirst, actualFirst);
        assertEquals(expectedSecond, actualSecond);
    }

}