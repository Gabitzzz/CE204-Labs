package lab4;

import java.util.*;

/***********************************************************************
 *  Lab 4, exercise 3                                                  *
 **********************************************************************/

/***********************************************************************
 *  Exercise 3a                                                        *
 ***********************************************************************
 *  The backbone of this class is the same as PriorityQueue.java from
 *  lecture 3. The main global differences are that, except for the new
 *  sort() method, all the methods, including the constructor, are now
 *  private. Users aren't supposed to create or manipulate a HeapSorter
 *  themselves; the sort() method will do all of that. The public inter-
 *  face is that the user calls sort(myArray) and that's it.
 */
public class HeapSorter {
    /*
     *  We don't initialize items to be a new array: it will be initial-
     *  ized to the array we wish to sort. We initialize size to 1,
     *  since we view the first element of the input array as being a
     *  one-element binary min-heap.
     */
    private int[] items;
    private int size;

    private HeapSorter (int[] items) {
        this.items = items;
        size = 1;
    }

    /*
     *  sort() sorts an array by inserting everything into the heap and
     *  then removing it again. We stop when the heap has size 1, since
     *  a one-element heap is just a single array cell.
     */
    public static void sort (int[] ints) {
        HeapSorter heap = new HeapSorter (ints);
        while (heap.size < ints.length)
            heap.insert();
        while (heap.size > 1)
            heap.extract();
    }

    /*
     *  Functions to calculate the indices of children and parents in
     *  the array. Identical to standard priority queue.
     */
    private static int leftChild (int index)  { return 2*index + 1; }
    private static int rightChild (int index) { return 2*index + 2; }
    private static int parent (int index)     { return (index-1)/2; }

    /*
     *  The function swap() swaps the entries at indices index1 and
     *  index2 in the array. Identical to standard priority queue.
     */
    private void swap (int index1, int index2) {
        int temp = items[index1];
        items[index1] = items[index2];
        items[index2] = temp;
    }

    /*
     *  Insert the next item into the heap. We don't need to be told
     *  what value to insert -- it's whatever value is at items[size].
     *  Therefore, insert() doesn't need to take any argument. Also,
     *  we don't need to extend the array, because it was already
     *  exactly the right size.  So, all we need to do is bubble up
     *  the value at the new leaf, if it's bigger than its parent.
     */
    private void insert () {
        int cur = size;
        size += 1;
        while (cur > 0 && items[cur] < items[parent(cur)]) {
            swap (cur, parent(cur));
            cur = parent(cur);
        }
    }

    /*
     *  next() is renamed extract() and no longer throws an exception:
     *  the method is private and we'll be careful to never call it
     *  when the heap is empty. extract() doesn't return the value
     *  that was taken off the heap. Instead, it just places it after
     *  the heap, where the last leaf of the heap was until we removed
     *  the root and moved the last leaf into its place.
     */
    private void extract () {
        /*
         *  Swap the root and the last leaf, and decrease the size of
         *  the heap.
         */
        int smallestFromHeap = items[0];
        size--;
        items[0] = items[size];
        items[size] = smallestFromHeap;

        /*
         *  Bubble down the value at the root while it's bigger than
         *  its children. Identical to standard priority queue.
         */
        int cur = 0;
        while (true) {
            /*
             *  smallestInFamily(cur) returns the index of the node
             *  containing the smallest number, among cur, its left
             *  child (if it has one) and its right child (if it
             *  has one). If cur has a smaller child, swap it with
             *  that child. If it has no smaller child, we are done.
             */
            if (smallestInFamily (cur) == rightChild (cur)) {
                swap (cur, rightChild (cur));
                cur = rightChild (cur);
            } else if (smallestInFamily (cur) == leftChild (cur)) {
                swap (cur, leftChild (cur));
                cur = leftChild (cur);
            } else
                break;
        }
    }

    /*
     *  smallestInFamily() is identical to the code in the standard
     *  priority queue. It returns the index of the node containing the
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

    private int size () { return size; }
    private boolean isEmpty () { return size == 0; }

    /*
     *  Test code to put some random numbers in an array and sort them.
     */
    public static void main (String[] args) {
        int[] ints = new int[20];
        Random rand = new Random();

        for (int i = 0; i < ints.length; i++)
            ints[i] = rand.nextInt (100);

        System.out.println("Original: " + Arrays.toString (ints));
        sort (ints);
        System.out.println("Sorted:   " + Arrays.toString (ints));
    }
}

/***********************************************************************
 *  Exercise 3b                                                        *
 ***********************************************************************
 *  To sort in ascending order, you would modify the binary min-heap to
 *  be a binary max-heap. A max-heap is identical to a min-heap except
 *  that the value stored in a node must be greater than both its
 *  children, instead of less than. This means that extract() now the
 *  largest element that remains in the heap, so we fill in the array
 *  with the largest element at the end, getting smaller towards the
 *  start. That is, we end up with the array sorted in ascending order.
 *
 *  It's not possible to do this with a binary min-heap because in that
 *  case, the first call to extract() returns the smallest element of
 *  the heap, which must be placed in the first cell of the array.
 *  However, the first cell of the array is being used for the heap, so
 *  we'd have to move the whole heap out of the way. The first call to
 *  extract() would require moving n-1 elements, the second call would
 *  move n-2 and so on, leading to a total amount of 1 + 2 + ... + (n-1)
 *  = Theta(n^2) of copying. This is much too slow.
 *
 *  Actually, there is one way you could do this with a min-heap. You
 *  could sort the array into descending order with a min-heap, and then
 *  do the extra step of reversing the array, in time Theta(n) (swap the
 *  first and last elements, the second and second-to-last, etc.). This
 *  would maintain the overall Theta(n log n) running time of the
 *  algorithm.
 */