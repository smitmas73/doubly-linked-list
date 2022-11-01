package data;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DoublyLinkedListTest {

    @InjectMocks
    @Spy
    private DoublyLinkedList doublyLinkedList;

    @Test
    void addEngine_shouldDoNothing_givenNullData() {
        doublyLinkedList.addEngine(null);

        assertThat(doublyLinkedList.getNodeCount()).isZero();
        assertThat(doublyLinkedList.getHead()).isNull();
        assertThat(doublyLinkedList.getTail()).isNull();
    }

    @Test
    void addEngine_shouldCreateNewDLL_whenDLLEmpty() {
        String data = "Engine";

        doReturn(true)
                .when(doublyLinkedList).isEmpty();

        doublyLinkedList.addEngine(data);

        assertThat(doublyLinkedList.getNodeCount()).isOne();
        assertThat(doublyLinkedList.getHead().getData()).isEqualTo(data);
        assertThat(doublyLinkedList.getTail().getData()).isEqualTo(data);
        assertThat(doublyLinkedList.getHead().getPrevious()).isNull();
        assertThat(doublyLinkedList.getHead().getNext()).isNull();
        verify(doublyLinkedList).isEmpty();
    }

    @Test
    void addEngine_shouldCreateNewHeadNode_whenDLLNotEmpty() {
        populateList();
        String data = "Engine";

        doReturn(false)
                .when(doublyLinkedList).isEmpty();

        doublyLinkedList.addEngine(data);

        assertThat(doublyLinkedList.getHead().getData()).isEqualTo(data);
    }

    @Test
    void addCaboose_shouldDoNothing_givenNullData() {
        doublyLinkedList.addCaboose(null);

        assertThat(doublyLinkedList.getNodeCount()).isZero();
        assertThat(doublyLinkedList.getHead()).isNull();
        assertThat(doublyLinkedList.getTail()).isNull();
    }

    @Test
    void addCaboose_shouldCreateNewDLL_whenDLLEmpty() {
        String data = "Caboose";

        doReturn(true)
                .when(doublyLinkedList).isEmpty();

        doublyLinkedList.addCaboose(data);

        assertThat(doublyLinkedList.getNodeCount()).isOne();
        assertThat(doublyLinkedList.getHead().getData()).isEqualTo(data);
        assertThat(doublyLinkedList.getTail().getData()).isEqualTo(data);
        assertThat(doublyLinkedList.getHead().getPrevious()).isNull();
        assertThat(doublyLinkedList.getHead().getNext()).isNull();
        verify(doublyLinkedList).isEmpty();
    }

    @Test
    void addCaboose_shouldCreateNewTailNode_whenDLLNotEmpty() {
        populateList();
        String data = "Caboose";

        doReturn(false)
                .when(doublyLinkedList).isEmpty();

        doublyLinkedList.addCaboose(data);

        assertThat(doublyLinkedList.getTail().getData()).isEqualTo(data);
    }

    @Test
    void addNodeAtIndex_shouldDoNothing_givenNullData() {
        doublyLinkedList.addNodeAtIndex(null, 0);

        assertThat(doublyLinkedList.getNodeCount()).isZero();
        assertThat(doublyLinkedList.getHead()).isNull();
        assertThat(doublyLinkedList.getTail()).isNull();
    }

    @ParameterizedTest
    @MethodSource("addNodeAtIndexTailScenarios")
    void addNodeAtIndex_shouldAddTail(Object data,
                                      Integer index) {
        populateList();

        doublyLinkedList.addNodeAtIndex(data, index);

        assertThat(doublyLinkedList.getTail().getData()).isEqualTo(data);

        verify(doublyLinkedList).addCaboose(data);
    }

    static Stream<Arguments> addNodeAtIndexTailScenarios() {
        return Stream.of(
                Arguments.of("data", null),
                Arguments.of("data", 1000)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void addNodeAtIndex_shouldAddHead_givenNegativeOrZeroIndex(int index) {
        populateList();
        String data = "data";

        doublyLinkedList.addNodeAtIndex(data, index);

        assertThat(doublyLinkedList.getHead().getData()).isEqualTo(data);

        verify(doublyLinkedList).addEngine(data);
    }

    @Test
    void addNodeAtIndex_shouldAddNode_whenDLLPopulated_givenDataAndIndex() {
        populateList();
        Integer previousNodeCount = doublyLinkedList.getNodeCount();
        String data = "data";
        Integer index = 1;

        doublyLinkedList.addNodeAtIndex(data, index);

        Node expectedNewNode = doublyLinkedList.getHead().getNext();

        assertThat(doublyLinkedList.getNodeCount()).isEqualTo(previousNodeCount + 1);
        assertThat(expectedNewNode.getData()).isEqualTo(data);
    }

    @Test
    void removeTailNode_shouldDoNothing_whenDLLIsEmpty() {
        doReturn(true)
                .when(doublyLinkedList).isEmpty();

        assertThat(doublyLinkedList.getNodeCount()).isZero();

        doublyLinkedList.removeTailNode();

        assertThat(doublyLinkedList.getNodeCount()).isZero();
        verify(doublyLinkedList).isEmpty();
    }

    @Test
    void removeTailNode_shouldRemoveCaboose_givenDLLIsNotEmpty() {
        populateList();
        Integer previousCount = doublyLinkedList.getNodeCount();
        Node previousTailPrevious = doublyLinkedList.getTail().getPrevious();

        assertThat(previousCount).isPositive();

        doReturn(false)
                .when(doublyLinkedList).isEmpty();

        doublyLinkedList.removeTailNode();

        assertThat(doublyLinkedList.getTail()).usingRecursiveComparison().isEqualTo(previousTailPrevious);
    }

    @Test
    void removeHeadNode_shouldDoNothing_whenDLLIsEmpty() {
        doReturn(true)
                .when(doublyLinkedList).isEmpty();

        assertThat(doublyLinkedList.getNodeCount()).isZero();

        doublyLinkedList.removeHeadNode();

        assertThat(doublyLinkedList.getNodeCount()).isZero();
        verify(doublyLinkedList).isEmpty();
    }

    @Test
    void removeHeadNode_shouldRemoveEngine_givenDLLIsNotEmpty() {
        populateList();
        Integer previousCount = doublyLinkedList.getNodeCount();
        Node previousHeadNext = doublyLinkedList.getHead().getNext();

        assertThat(previousCount).isPositive();

        doReturn(false)
                .when(doublyLinkedList).isEmpty();

        doublyLinkedList.removeHeadNode();

        assertThat(doublyLinkedList.getHead()).usingRecursiveComparison().isEqualTo(previousHeadNext);
    }

    @Test
    void removeHeadNode_shouldRemoveEngine_whenDLLHasOneNode() {
        doublyLinkedList.addEngine("1");

        assertThat(doublyLinkedList.getNodeCount()).isOne();

        doublyLinkedList.removeHeadNode();

        assertThat(doublyLinkedList.getNodeCount()).isZero();
        assertThat(doublyLinkedList.getHead()).isNull();
        assertThat(doublyLinkedList.getTail()).isNull();
    }

    @Test
    void removeNodeAtIndex_shouldDoNothing_whenDLLIsEmpty() {
        assertThat(doublyLinkedList.getNodeCount()).isZero();

        doublyLinkedList.removeNodeAtIndex(1);

        assertThat(doublyLinkedList.getNodeCount()).isZero();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {-1, 100})
    void removeNodeAtIndex_shouldDoNothing_givenNullOrInvalidIndex(Integer index) {
        populateList();
        Integer previousCount = doublyLinkedList.getNodeCount();

        assertThat(previousCount).isPositive();

        doublyLinkedList.removeNodeAtIndex(index);

        assertThat(doublyLinkedList.getNodeCount()).isEqualTo(previousCount);
    }

    @Test
    void removeNodeAtIndex_shouldRemoveHead_givenZeroIndex() {
        populateList();
        Node expectedNewHead = doublyLinkedList.getHead().getNext();

        doublyLinkedList.removeNodeAtIndex(0);

        assertThat(doublyLinkedList.getHead()).usingRecursiveComparison().isEqualTo(expectedNewHead);
    }

    @Test
    void removeNodeAtIndex_shouldRemoveTail_givenTailIndex() {
        populateList();
        Integer tailIndex = doublyLinkedList.getNodeCount() - 1;
        Node expectedNewTail = doublyLinkedList.getTail().getPrevious();

        doublyLinkedList.removeNodeAtIndex(tailIndex);

        assertThat(doublyLinkedList.getTail()).usingRecursiveComparison().isEqualTo(expectedNewTail);
    }

    @Test
    void removeNodeAtIndex_shouldRemoveNodeAtIndex_givenMidIndex() {
        populateList();
        Integer previousNodeCount = doublyLinkedList.getNodeCount();

        doublyLinkedList.removeNodeAtIndex(1);

        assertThat(doublyLinkedList.getNodeCount()).isEqualTo(previousNodeCount - 1);
        assertThat(doublyLinkedList.getHead().getNext()).usingRecursiveComparison().isEqualTo(doublyLinkedList.getTail());
        assertThat(doublyLinkedList.getTail().getPrevious()).usingRecursiveComparison().isEqualTo(doublyLinkedList.getHead());
    }

    @Test
    void isEmpty_shouldReturnTrue_whenDLLIsEmpty() {
        assertThat(doublyLinkedList.isEmpty()).isTrue();
    }

    @Test
    void isEmpty_shouldReturnFalse_whenDLLIsNotEmpty() {
        populateList();

        assertThat(doublyLinkedList.isEmpty()).isFalse();
    }

    @Test
    void isNotEmpty_shouldReturnFalse_whenDLLIsEmpty() {
        assertThat(doublyLinkedList.isNotEmpty()).isFalse();
    }

    @Test
    void isNotEmpty_shouldReturnTrue_whenDLLIsNotEmpty() {
        populateList();

        assertThat(doublyLinkedList.isNotEmpty()).isTrue();
    }

    private void populateList() {
        List<String> prePopulatedValues = ImmutableList.of("1", "2", "3");

        prePopulatedValues.forEach(doublyLinkedList::addCaboose);
    }
}