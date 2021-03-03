package lab4;

import java.util.*;

import lab4.PriorityQueue;

/***********************************************************************
 *  Lab 4, exercise 1                                                  *
 **********************************************************************/

public class PriorityQueueSorter {
    /*
     *  A couple of symbolic constants so we don't have to remember
     *  what the second parameter of sort() does.
     */
    public static final boolean ASCENDING_ORDER  = true;
    public static final boolean DESCENDING_ORDER = false;

    /*
     *  To sort, we just insert all the elements of the array into the
     *  priority queue and then repeatedly call next() to get them out
     *  again, in increasing order. To sort into ascending order, write
     *  them back into the array left-to-right; for descending order,
     *  write them right-to-left.
     *
     *  The running time is O(n log n). We make n calls to insert and
     *  n calls to next, and each of these calls takes time O(log n).
     *  So the total is O(2n log n), which is the same as O(n log n)
     *  (discarding the constant factor).
     */
    public static void sort (int[] ints, boolean ascending) {
        PriorityQueue pq = new PriorityQueue();

        for (int i : ints)
            pq.insert(i);

        if (ascending)
            for (int i = 0; i < ints.length; i++)
                ints[i] = pq.next();
        else
            for (int i = ints.length-1; i >= 0; i--)
                ints[i] = pq.next();
    }

    /*
     *  Test code to put some random numbers in an array and sort them.
     */
    public static void main (String[] args) {
        int[] ints = new int[20];
        Random rand = new Random();

        for (int i = 0; i < ints.length; i++)
            ints[i] = rand.nextInt (100);

        System.out.println("Original:   " + Arrays.toString (ints));
        sort (ints, ASCENDING_ORDER);
        System.out.println("Ascending:  " + Arrays.toString (ints));
        sort (ints, DESCENDING_ORDER);
        System.out.println("Descending: " + Arrays.toString (ints));
    }
}
