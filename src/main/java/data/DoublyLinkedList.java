package data;

import com.google.common.annotations.VisibleForTesting;
import lombok.Data;

import java.util.Objects;

@Data
public class DoublyLinkedList {
    private Node head;
    private Node tail;
    private Integer nodeCount = 0;

    public void addEngine(Object data) {
        if (Objects.isNull(data)) {
            System.out.println("No data provided.");
            return;
        }

        Node node = new Node()
                .setData(data);

        if (isEmpty()) {
            head = node;
            tail = node;
            nodeCount++;
            return;
        }

        head.setPrevious(node);
        head = node
                .setNext(head)
                .setPrevious(null);
        nodeCount++;
    }

    public void addCaboose(Object data) {
        if (Objects.isNull(data)) {
            System.out.println("No data provided.");
            return;
        }

        Node node = new Node()
                .setData(data);

        if (isEmpty()) {
            head = node;
            tail = node;
            nodeCount++;
            return;
        }

        tail.setNext(node);
        tail = node
                .setPrevious(tail)
                .setNext(null);
        nodeCount++;
    }

    public void addNodeAtIndex(Object data, Integer index) {
        if (Objects.isNull(data)) {
            System.out.println("No data provided.");
            return;
        }

        if (Objects.isNull(index) || index > nodeCount + 1) {
            addCaboose(data);
            return;
        }

        if (index <= 0) {
            addEngine(data);
            return;
        }

        Node indexNode = head;
        Node newNode = new Node()
                .setData(data);
        for (int i = 0; i < index; i++) {
            indexNode = indexNode.getNext();
        }

        newNode.setPrevious(indexNode.getPrevious())
                .setNext(indexNode);
        indexNode.getPrevious().setNext(newNode);
        indexNode.setPrevious(newNode);
        nodeCount++;
    }

    public void removeTailNode() {
        if (isEmpty()) {
            System.out.println("List is currently empty.");
            return;
        }

        tail = tail.getPrevious().setNext(null);
        nodeCount--;
    }

    public void removeHeadNode() {
        if (isEmpty()) {
            System.out.println("List is currently empty.");
            return;
        }

        if (Objects.nonNull(head.getNext())) {
            head = head.getNext().setPrevious(null);
        } else {
            head = null;
            tail = null;
        }
        nodeCount--;
    }

    public void removeNodeAtIndex(Integer index) {
        if (isEmpty()) {
            System.out.println("List is currently empty.");
            return;
        }

        if (Objects.isNull(index)
                || index >= nodeCount
                || index < 0) {
            System.out.println("Invalid index provided");
            return;
        }

        if (index == 0) {
            removeHeadNode();
            return;
        }

        if (index == nodeCount - 1) {
            removeTailNode();
            return;
        }

        Node indexNode = head;
        for (int i = 0; i < index; i++) {
            indexNode = indexNode.getNext();
        }

        Node tmpNode = indexNode;
        indexNode.getPrevious().setNext(tmpNode.getNext());
        indexNode.getNext().setPrevious(tmpNode.getPrevious());
        nodeCount--;
    }

    public void printContents() {
        System.out.println(asString());
    }

    public String asString() {
        StringBuilder content = new StringBuilder();
        if (isNotEmpty()) {
            Node current = head;
            content.append("{ ");
            while (Objects.nonNull(current)) {
                content.append("[ ").append(current.getData()).append(" ] ");
                current = current.getNext();
            }
            content.append("}");
            return content.toString();
        }

        return "List is currently empty";
    }

    @VisibleForTesting
    boolean isEmpty() {
        return nodeCount == 0;
    }

    @VisibleForTesting
    boolean isNotEmpty() {
        return !isEmpty();
    }
}
