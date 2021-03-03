package lab3;

/*******************************************************************
 *  Lab 3, exercise 1.                                             *
 *******************************************************************
 *  There are two main ways of solving this exercise. The approach
 *  I've taken here is to write a Node class that contains an Object
 *  and its priority. The alternative is to just have two separate
 *  arrays -- one for the priorities and one for the items -- and
 *  ensure that they're kept synchronized (i.e., whenever you swap
 *  entries i and j of the priority array, also swap entries i
 *  and j of the item array).
 */

public class ObjectPriorityQueue {
    public class EmptyException extends RuntimeException {}

    private class Node {
        int priority;
        Object data;

        Node (int priority, Object data) {
            this.priority = priority;
            this.data = data;
        }
    }

    private Node[] items = new Node[10];
    private int size = 0;

    private static int leftChild (int index)  { return 2*index + 1; }
    private static int rightChild (int index) { return 2*index + 2; }
    private static int parent (int index)     { return (index-1)/2; }

    private void swap (int index1, int index2) {
        Node temp = items[index1];
        items[index1] = items[index2];
        items[index2] = temp;
    }

    /*
     *  Insert an item with priority p.
     */
    public void insert (int p, Object obj) {
        if (size == items.length) {
            Node[] newItems = new Node[size*2];
            System.arraycopy (items, 0, newItems, 0, size);
            items = newItems;
        }

        int cur = size;

        items[size++] = new Node (p, obj);
        while (cur > 0 && items[cur].priority < items[parent(cur)].priority) {
            swap (cur, parent(cur));
            cur = parent(cur);
        }
    }

    /*
     *  Return the object that has the highest assigned priority.
     */
    public Object next () throws EmptyException {
        if (isEmpty())
            throw new EmptyException ();

        Node next = items[0]; /* Remember value at root. */
        size--;

        items[0] = items[size]; /* Move last leaf to root. */
        int cur = 0;
        while (true) {
            if (smallestInFamily (cur) == rightChild (cur)) {
                swap (cur, rightChild (cur));
                cur = rightChild (cur);
            } else if (smallestInFamily (cur) == leftChild (cur)) {
                swap (cur, leftChild (cur));
                cur = leftChild (cur);
            } else
                break;
        }
        return next.data;
    }

    private int smallestInFamily (int index) {
        int lc = leftChild (index);
        int rc = rightChild (index);

        if (rc  < size && items[rc].priority < items[index].priority
                && items[rc].priority < items[lc].priority)
            return rc;
        if (lc < size && items[lc].priority < items[index].priority)
            return lc;
        else
            return index;
    }

    public int size () { return size; }
    public boolean isEmpty () { return size == 0; }

    /*
     *  Test code that inserts integers into the queue. Strings
     *  "String20", "String19", ..., "String1" are inserted into the
     *  queue, with priorities 20, 19, ..., 1, and then retrieved.
     *  This is a better test than inserting in the order 1, ..., 20,
     *  since that wouldn't require any bubbling up in the insertions.
     */
    public static void main (String[] args) {
        ObjectPriorityQueue opq = new ObjectPriorityQueue();

        for (int i = 20; i > 0; i--)
            opq.insert (i, "String"+Integer.toString (i));

        while (!opq.isEmpty())
            System.out.println(opq.next());
    }
}
