import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author psy
 * @date 5/12/19.
 */
public class Deque<Item> implements Iterable<Item> {
    private int size;
    private E head;
    private E tail;

    // construct an empty deque
    public Deque() {
        this.size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateAdd(item);
        size++;
        E oldHead = head;
        E newElem = new E(item, oldHead, null);
        head = newElem;
        if (oldHead == null){
            tail = newElem;
        } else  {
            oldHead.previous = newElem;
        }
    }

    // add the item to the end
    public void addLast(Item item) {
        validateAdd(item);
        size++;
        E oldElem = tail;
        E newElem = new E(item, null, tail);

        if (oldElem == null){
            head = newElem;
        } else {
            oldElem.next= newElem;
        }
        tail = newElem;
    }

    private void validateAdd(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }
    // remove and return the item from the front

    public Item removeFirst() {
        validateEmpty();
        Item value = head.value;
        E oldHead = head;
        E newHead = oldHead.next;
        oldHead.next = null;
        if (newHead != null) {
            newHead.previous = null;
        } else {
            tail = null;
        }
        head = newHead;
        size--;
        return value;
    }

    // remove and return the item from the end
    public Item removeLast() {
        validateEmpty();
        Item value = tail.value;
        E oldTail = tail;
        E newTail = tail.previous;
        oldTail.previous = null;
        if(newTail != null) {
            newTail.next = null;
        } else {
            head = null;
        }
        tail = newTail;
        size --;
        return value;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator(){
        return getIterator();
    }

    private Iterator<Item> EMPTY_ITERATOR = new Iterator<Item>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Item next() {
            throw  new NoSuchElementException();
        }
    };

    private Iterator<Item> getIterator() {
        if (isEmpty()){
            return EMPTY_ITERATOR;
        }

        return new Iterator<Item>() {
            private E current = head;
            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                if (current != null){
                    Item value = current.value;
                    current = current.next;
                    return value;
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }


    private class E {
        private Item value;
        private E next;
        private E previous;

        public E() {
        }

        public E(Item value, E next, E previous) {
            this.value = value;
            this.next = next;
            this.previous = previous;
        }
    }


    public static void main(String[] args) {
        test1();
        test2();
        test3();
        testIterator();
    }

    private static void testIterator() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(1);
        deque.addFirst(1);

        int i = 0;
        Iterator<Integer> iter = deque.getIterator();
        while (iter.hasNext()){
            iter.next();
            i ++;
        }
            assert i == 3;
    }

    private static void test2() {
        Deque<Integer> deque;

        deque = new Deque<>();
        deque.isEmpty();
        deque.addFirst(2);
        deque.removeLast();
        deque.addFirst(4);
        Integer expected = deque.removeLast();
        assert expected == 4;
    }

    private static void test3() {
        Deque<Integer> deque;

        deque = new Deque<>();
        deque.isEmpty();
        deque.addLast(2);
        deque.removeFirst();
        deque.addLast(4);
        Integer expected = deque.removeFirst();
        assert expected == 4;
    }

    private static void test1() {
        Deque<Integer> deque = new Deque<>();
        deque.isEmpty();
        deque.isEmpty();
        deque.isEmpty();
        deque.addFirst(4);
        Integer expected = deque.removeLast();
        assert expected == 4;
    }
}