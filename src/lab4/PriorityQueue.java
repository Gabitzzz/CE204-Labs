package lab4;

import java.util.Arrays;

/***********************************************************************
 *  Lab 4, exercise 2                                                  *
 **********************************************************************/

public class PriorityQueue {
    /*******************************************************************
     *  Exercise 2a.                                                   *
     ******************************************************************/

    /*
     *  bubbleUp() bubbles the value at array position 'index' up
     *  the tree until it's greater than its parent. The code is
     *  taken from trees.PriorityQueue.insert().
     */
    private void bubbleUp (int index) {
        while (index > 0 && items[index] < items[parent(index)]) {
            swap (index, parent(index));
            index = parent(index);
        }
    }

    /*
     *  bubbleDown() bubbles the value at array position 'index'
     *  down until it's less than both its children. The code is
     *  taken from trees.PriorityQueue.next().
     */
    private void bubbleDown (int index) {
        while (true) {
            /*
             *  smallestInFamily(cur) returns the index of the node
             *  containing the smallest number, among cur, its left
             *  child (if it has one) and its right child (if it
             *  has one). If cur has a smaller child, swap it with
             *  that child. If it has no smaller child, we are done.
             */
            if (smallestInFamily (index) == rightChild (index)) {
                swap (index, rightChild (index));
                index = rightChild (index);
            } else if (smallestInFamily (index) == leftChild (index)) {
                swap (index, leftChild (index));
                index = leftChild (index);
            } else
                break;
        }
    }

    /*
     *  find(p) returns the index of the array cell that contains p,
     *  or -1 if p is not in the array. In principle, this could be
     *  made more efficient by traversing the tree and using the
     *  fact that, if you're in a node that has value greater than p,
     *  p cannot be in your left or right subtree. However, my guess
     *  is that the extra overhead of the traversal would outweigh
     *  any gain from skipping over parts of the tree. Maybe you
     *  should try it?
     */
    private int find (int p) {
        for (int i = 0; i < size; i++)
            if (items[i] == p)
                return i;
        return -1;
    }

    /*
     *  changePriority() finds a node with priority oldP and changes
     *  its priority to newP. If the node is now less than its
     *  parent, it's bubbled up; otherwise, it's bubbled down.
     *  bubbleDown() might do nothing, but it's easier to call it
     *  and let it do nothing than it is to check whether it would
     *  do anything before calling.
     */
    public void changePriority (int oldP, int newP) {
        int index = find (oldP);
        if (index < 0)
            return;

        items[index] = newP;
        if (index > 0 && items[parent(index)] > newP)
            bubbleUp(index);
        else
            bubbleDown(index);
    }
    /*
     *  The running time of changePriority() is O(n) because this
     *  is how long it takes to find the correct node in the tree.
     *  Having found the item, bubbling up/down takes additional
     *  time O(log n) but this is a lower-order term.
     *  n + log n = O(n).
     */

    /*******************************************************************
     *  Exercise 2b.                                                   *
     ******************************************************************/
    /*
     *  delete() deletes an item with the given priority, if one exists.
     *  It replaces the value at that point in the tree with the value
     *  at the last leaf and bubbles up or down if needed.
     */
    public void delete (int p) {
        int index = find (p);
        if (p < 0)
            return;

        items[index] = items[--size];
        if (index > 0 && items[parent(index)] > items[index])
            bubbleUp(index);
        else
            bubbleDown(index);
    }
    /*
     *  The running time of delete() is O(n), for the same reason as
     *  changePriority().
     */

    /*******************************************************************
     *  Exercise 2c.                                                   *
     *******************************************************************
     *  insertThenNext() has the same effect as inserting p into the
     *  min-heap, then calling next(). If p is less than the current
     *  root of the heap, we just return it without modifying the heap.
     *  Otherwise, we will return the current root. We replace it with
     *  p and then bubble down.
     */
    public int insertThenNext (int p) {
        int rootValue = items[0];

        if (p < rootValue)
            return p;

        items[0] = p;
        bubbleDown (0);
        return rootValue;
    }
    /*
     *  The running time is O(log n), for the same reason as
     *  changePriority() and delete().  However, insertThenNext(p) is
     *  faster than insert(p); next(); because it makes at most one
     *  call to bubbleDown(), whereas insert(p); next(); makes one
     *  call to bubbleUp() and one call to bubbleDown(). The result
     *  is that insertThenNext() rearranges the tree less (and not at
     *  all if p is less than the current root, which is the situation
     *  in which insert()'s call to bubbleUp() would do the most work).
     */

    /*******************************************************************
     *  Everything from here is more or less the same as the the       *
     *  priority queue class from lecture 3. insert() and next() have  *
     *  been modified to use bubbleUp() and bubbleDown(), respectively.*
     ******************************************************************/
    /*
     *  An exception that will be thrown by next() if called on an empty
     *  priority queue. This allows all integer priorities to be stored,
     *  instead of, e.g., reserving negative priorities to mean errors.
     */
    public class EmptyException extends RuntimeException {}

    private int[] items = new int[10];
    int size = 0;

    /*
     *  Functions to calculate the indices of children and parents in
     *  the array.
     */
    private static int leftChild (int index)  { return 2*index + 1; }
    private static int rightChild (int index) { return 2*index + 2; }
    private static int parent (int index)     { return (index-1)/2; }

    /*
     *  The function swap() swaps the entries at indices index1 and
     *  index2 in the array.
     */
    private void swap (int index1, int index2) {
        int temp = items[index1];
        items[index1] = items[index2];
        items[index2] = temp;
    }

    /*
     *  Insert an item with priority p. Code is identical to
     *  lecture code, except that bubbleUp() has been extracted
     *  into a method.
     */
    public void insert (int p) {
        /*
         *  Extend the array if it is full.
         */
        if (size == items.length) {
            int[] newItems = new int[size*2];
            System.arraycopy (items, 0, newItems, 0, size);
            items = newItems;
        }

        /*
         *  We insert in the next available leaf position (at the end
         *  of the used part of the array) and bubble up if necessary.
         */
        items[size] = p;
        bubbleUp (size);
        size++;
    }

    /*
     *  Return the highest priority (smallest number) in the tree. The
     *  value to be returned is the current root of the tree. We
     *  replace it in the tree with the value in the last leaf, which
     *  is then bubbled down if necessary.  Code is identical to
     *  lecture code, except that bubbleUp() has been extracted
     *  into a method.
     */
    public int next () throws EmptyException {
        if (isEmpty())
            throw new EmptyException ();

        int next = items[0];
        items[0] = items[--size];
        bubbleDown (0);
        return next;
    }

    /*
     *  smallestInFamily returns the index of the node containing the
     *  smallest value among 'index', 'index's left child (if it exists)
     *  and 'index's right child (if it exists). Note that, because of
     *  the way the tree is organized, if the right child exists, the
     *  left child must exist, too.
     */
    private int smallestInFamily (int index) {
        int lc = leftChild (index);
        int rc = rightChild (index);

        if (rc < size && items[rc] < items[index]
                && items[rc] < items[lc])
            return rc;
        if (lc < size && items[lc] < items[index])
            return lc;
        else
            return index;
    }

    public int size () { return size; }
    public boolean isEmpty () { return size == 0; }

    /*
     *  Test code to insert some values into the queue and check
     *  they come back out again.
     */
    public static void main (String[] args) {
        PriorityQueue pq = new PriorityQueue();

        for (int i = 0; i < 10; i++)
            pq.insert(30-i);

        while (!pq.isEmpty()) {
//            System.out.println(Arrays.toString(pq.items));
            System.out.println(pq.next());
        }
    }
}
