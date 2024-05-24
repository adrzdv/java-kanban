package historyManagerPackage;

import java.util.ArrayList;
import java.util.List;

public class HistoryLinkedList<T> {


    class Node<E> {
        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    public Node<T> getTail() {

        return tail;
    }

    public void linkLast(T element) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(oldTail, element, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
    }

    public List<T> getTasks() {

        List<T> returnList = new ArrayList<>();
        if (head != null) {
            Node<T> currentNode = head;
            int i = 0;
            while (i < this.size) {
                returnList.add(currentNode.data);
                currentNode = currentNode.next;
                i++;
            }
        }
        return returnList;
    }

    public boolean removeNode(Node<T> node) {
        final Node<T> prevNode = node.prev;
        final Node<T> nextNode = node.next;

        if (prevNode == null && nextNode == null) {
            this.head = node;
            return false;
        } else {
            if (node.prev == null && nextNode != null) {
                nextNode.prev = null;
                this.head = nextNode;
                node.data = null;
            } else if (node.next == null && prevNode != null) {
                prevNode.next = null;
                this.tail = prevNode;
                node.data = null;
            } else if (node.prev != null && node.next != null) {
                nextNode.prev = prevNode;
                prevNode.next = nextNode;
                node.data = null;
            }
            size--;
            return true;
        }

    }

}
