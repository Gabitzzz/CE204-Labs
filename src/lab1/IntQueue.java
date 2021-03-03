package lab1;

/**
 *  This file is identical to Queue.java from the lectures, except that it
 *  defines a queue of ints, rather than a queue of Strings. Please see the
 *  comments in Queue.java for an explanation of how the code works. (Later
 *  in the course, we'll use generics to avoid this kind of duplication.)
 */
public class IntQueue {
    private class Item {
        int value;
        Item next;

        Item (int value) {
            this.value = value;
            this.next = null;
        }
    }

    private Item front = null;
    private Item back = null;
    private int size = 0;

    public void add (int s) {
        if (isEmpty()) {
            front = back = new Item(s);
        } else {
            back.next = new Item(s);
            back = back.next;
        }
        size++;
    }

    public int remove () {
        if (isEmpty())
            return -1;

        int s = front.value;
        front = front.next;
        if (front == null)
            back = null;
        size--;
        return s;
    }

    public boolean isEmpty () { return front == null; }
    public int length ()      { return size;         }
}
