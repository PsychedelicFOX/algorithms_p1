import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author psy
 * @date 5/12/19.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] elems ;
    private int size;
    private int tail;
    // construct an empty randomized queue
    public RandomizedQueue(){
        elems = (Item[]) new Object[]{};
    }
    // is the randomized queue empty?
    public boolean isEmpty(){
        return size == 0;
    }
    // return the number of items on the randomized queue
    public int size(){
        return size;
    }
    // add the item
    public void enqueue(Item item){
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (needIncreaseCapacity(elems)){
            increaseCapacity();
        }


        elems[tail] = item;

        size ++;
        tail ++;

        if (needDecreaseCapacity(elems)){
            decreaseCapacity();
        }

    }

    private void increaseCapacity() {
        elems = copyToNewArrayAndUpdateTail(isEmpty() ? 1 : elems.length * 2, elems);
    }

    private void decreaseCapacity() {
        elems = copyToNewArrayAndUpdateTail(elems.length / 2, elems );
    }

    private boolean needDecreaseCapacity(Item[] elems) {
        return !isEmpty() && elems.length / tail == 4;
    }

    private boolean needIncreaseCapacity(Item[] items) {
        return tail == items.length;
    }

    private Item[] copyToNewArrayAndUpdateTail(int capacity, Item[] elems) {
        // index in new storage
        int j = 0;
        Item[] newStorage = (Item[]) new Object[capacity];
        for (Item elem : elems) {
            if (elem != null) {
                newStorage[j] = elem;
                j++;
            }
        }
        tail = j;
        size = j;
        return newStorage;
    }

    // remove and return a random item
    public Item dequeue(){
        checkIfIsEmpty();
        final Item value = dequeueRandom(getRandomIndex(),false);
        if (needDecreaseCapacity(elems)){
            decreaseCapacity();
        }
        size --;
        return value;
    }

    private int getRandomIndex(){
        return StdRandom.uniform(tail);
    }

    private Item dequeueRandom(int seed, boolean peek){

        Item value = null;

        while (value == null){
            if (elems[seed] != null){
                value = elems[seed];
                if (!peek) {
                    elems[seed] = null;
                }
                return value;
            }
            seed = StdRandom.uniform(tail);

        }

        return value;
    }

    private Item dequeRecurcively(int seed, boolean peek){
        if (elems[seed] != null){
            final Item value = elems[seed];
            if (!peek) {
                elems[seed] = null;
            }
            return value;
        }
        return dequeRecurcively(StdRandom.uniform(tail), peek);
    }

    private void checkIfIsEmpty() {
        if (isEmpty()){
            throw new NoSuchElementException();
        }
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkIfIsEmpty();
        return  dequeueRandom(StdRandom.uniform(tail),true);
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){
        return getIterator(copy());
    }

    private Iterator<Item> getIterator(RandomizedQueue<Item> copy) {
        return new Iterator<Item>() {
            // todo -  use Fisher–Yates shuffle and iterate over plain array
            RandomizedQueue<Item> queue = copy;
            @Override
            public boolean hasNext() {
                return !queue.isEmpty();
            }

            @Override
            public Item next() {
                if (queue.isEmpty()){
                    throw new NoSuchElementException();
                }
                return copy.dequeue();
            }
        };
    }

    private RandomizedQueue<Item> copy(){
        RandomizedQueue<Item> copy = new RandomizedQueue<>();
        copy.size = this.size;
        copy.tail = this.tail;
        copy.elems = elems.clone();

        return copy;
    }

    public static void main(String[] args){
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();

    }

    private static void test1() {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(2);
        Integer value = rq.dequeue();
        assert value == 2;
    }

    private static void test2() {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(2);
        rq.enqueue(3);
        Integer value = rq.dequeue();
        assert value == 2 || value == 3;
    }

    private static void test3() {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(2);
        rq.enqueue(3);
        rq.dequeue();
        int value = rq.size();
        assert value == 1;
    }

    private static void test4() {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(2);
        rq.enqueue(3);
        rq.sample();
        int value = rq.size;
        assert value == 2;
    }

    private static void test5() {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(20);
        rq.dequeue();
        rq.enqueue(36);
        int val = rq.dequeue();
        assert val == 36;

    }

    private static void test6() {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for (int i = 0; i < 5000 ; i++) {
            rq.enqueue(1);
            rq.enqueue(2);
            rq.enqueue(3);
        }

        int one_count = 0, two_count = 0, three_count = 0;

        for (int i = 0; i< 1000000; i++){
            Integer val = rq.sample();
            if (val == 1)
                one_count ++;
            if (val == 2)
                two_count ++;
            if (val == 3)
                three_count ++;

        }

        assert 1 - one_count / (double) two_count <= 0.01;
        assert 1 - three_count / (double) two_count <= 0.01;
        assert 1 - one_count / (double) three_count <= 0.01;


    }

    private static void test7() {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        for (int i = 0; i < 5000 ; i++) {
            rq.enqueue(1);
            rq.enqueue(2);
            rq.enqueue(3);
        }

        int one_count = 0, two_count = 0, three_count = 0;

        for (Integer i : rq){
            if (i == 1)
                one_count ++;
            if (i == 2)
                two_count ++;
            if (i == 3)
                three_count ++;

        }

        assert 1 - one_count / (double) two_count <= 0.01;
        assert 1 - three_count / (double) two_count <= 0.01;
        assert 1 - one_count / (double) three_count <= 0.01;


    }

}
