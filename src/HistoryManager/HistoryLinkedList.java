package historymanager;

import java.util.ArrayList;
import java.util.List;

/*Реализуем класс связанного списка для хранения истории просмотров*/
public class HistoryLinkedList<T> {

    /*Реализуем класс узла, где у нас хранится информация о просмотренной задаче, а также информация на предыдущий и
     * последующий элемент списка*/
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

    private Node<T> head;   //указатель на голову
    private Node<T> tail;   //указатель на хвост
    private int size = 0;

    public Node<T> getTail() {

        return tail;
    }

    /*Метод для добавления узла в конец списка*/
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

    /*Собираем и выводим в ArrayList информацию, хранящуюся в HistoryLinkedList*/

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

    /*Метод для удаления узла из списка*/
    public boolean removeNode(Node<T> node) {
        final Node<T> prevNode = node.prev;
        final Node<T> nextNode = node.next;

        if (prevNode == null && nextNode == null) {         //проверка, если список у нас состоит из одного элемента
            this.head = node;
            return false;
        } else {                                            //рассматриваем случаи, когда в списке более одного элемента
            if (node.prev == null && nextNode != null) {    //если элемент находится в голове списка
                nextNode.prev = null;
                this.head = nextNode;
                node.data = null;
            } else if (node.next == null && prevNode != null) { //если элемент нахоится в конце списка
                prevNode.next = null;
                this.tail = prevNode;
                node.data = null;
            } else if (node.prev != null && node.next != null) {    //случай, если элемент находится в середине списка
                nextNode.prev = prevNode;
                prevNode.next = nextNode;
                node.data = null;
            }
            size--;
            return true;
        }

    }

}
