package lab3;

import java.util.Random;

/*******************************************************************
 *  Lab 3, exercise 3.                                             *
 *******************************************************************
 *  A program to test the height of randomly generated binary search
 *  trees. Bruce Reed's paper "The height of a random binary search
 *  tree" (Journal of the ACM, vol. 50, no. 3, 2003) proves that,
 *  for large n, if you put the numbers 0, ..., n-1 into a BST in a
 *  random order, the average height of the resulting tree is
 *  4.311ln n - 1.953ln(ln n) + k for some constant k.
 *
 *  The lab exercise says to make the tree by adding n random
 *  numbers from the full range that an int variable can hold. I
 *  assumed that would be a reasonable approximation to adding
 *  the numbers 0, ..., n-1 in random order. You'll get a small
 *  number of duplicates but I figured there wouldn't be enough to
 *  make much difference to the height of the tree. However, I found
 *  the results weren't very good. I thought this might be because
 *  on average, there are large gaps between the random numbers and
 *  this might affect the shape of the tree.
 *
 *  Because of that, I rewrote my code to use random orderings of
 *  0, ..., n-1. To do that, I set up an array containing the
 *  numbers in order, and then use the Fisher-Yates shuffling
 *  algorithm to create a random permutation of them. You should
 *  remember Fisher-Yates as the correct way to shuffle an array,
 *  since usually people just do some random-looking swaps that
 *  don't generate all the possibilities with equal probability.
 *  (Fisher-Yates is not examinable.)
 *
 *  The results still weren't as good as I would have liked but
 *  1000 trials with trees of size 0.25M, 0.5M, 1M, 2M and 4M
 *  nodes suggests that the value of k is about -5.3. The proof only
 *  applies to "large enough" trees but I'm surprised the results
 *  weren't being consistent even in the first decimal place when
 *  considering trees as large as four million nodes.  Nonetheless,
 *  I think this is reasonable confirmation of the actual size.
 */

public class BSTMeasurement {
    private class Node {
        int value;
        Node left, right;

        Node (int value) {
            this.value = value;
            left = right = null;
        }

        /*
         *  The getHeight method is essentially the same as
         *  exercise 2b.
         */
        int getHeight () {
            if (left == null && right == null)
                return 0;

            int leftHeight = (left == null) ? 0 : left.getHeight();
            int rightHeight = (right == null) ? 0 : right.getHeight();
            return 1 + Math.max (leftHeight, rightHeight);
        }
    }

    private Node root = null;

    void insert (int i) {
        if (root == null) {
            root = new Node(i);
            return;
        }
        Node cur = root;
        while (true) {
            if (cur.value == i)
                return;
            else if (i < cur.value) {
                if (cur.left != null) {
                    cur = cur.left;
                } else {
                    cur.left = new Node(i);
                    return;
                }
            } else {
                if (cur.right != null) {
                    cur = cur.right;
                } else {
                    cur.right = new Node(i);
                    return;
                }
            }
        }
    }

    public int getHeight () {
        return (root == null) ? 0 : root.getHeight();
    }

    static Random r = new Random();

    /*
     *  This method uses the Fisher-Yates shuffle to randomly permute
     *  an array. The input, in this case, will be the array {1, ..., n}
     *  for some n.
     */
    static void shuffle (int[] ints) {
        for (int i = ints.length-1; i > 0; i--) {
            int j = r.nextInt (i+1);
            int temp = ints[i];
            ints[i] = ints[j];
            ints[j] = temp;
        }
    }

    /*
     *  main() creates ITERATIONS random BSTs by inserting the
     *  numbers 0, ..., SIZE-1 in random orders: the orders are
     *  obtained using the Fisher-Yates method, via shuffle().
     *  main() computes the average height of these trees and
     *  outputs it, along with the average height predicted by
     *  Reed's formula 4.331 ln SIZE - 1.953 ln (ln SIZE) + k for
     *  some constant k.
     *
     *  Results are as follows:
     *
     *  SIZE        ITERATIONS   value of k
     *    250,000      1000        -5.34
     *    500,000      1000        -5.35
     *  1,000,000      1000        -5.27
     *  2,000,000      1000        -5.38
     *  4,000,000      1000        -5.25
     */
    public static void main (String[] args) {
        final int SIZE = 4_000_000;
        final int ITERATIONS = 1000;

        double total = 0;

        int[] ints = new int[SIZE];

        for (int t = 0; t < ITERATIONS; t++) {
            BSTMeasurement bst = new BSTMeasurement();
            for (int i = 0; i < SIZE; i++)
                ints[i] = i;
            shuffle (ints);

            for (int i = 0; i < SIZE; i++)
                bst.insert(ints[i]);

            total += bst.getHeight ();
        }
        total /= ITERATIONS;
        double prediction = 4.311d * Math.log(SIZE) - 1.953d * Math.log(Math.log(SIZE));
        System.out.println("Measured = " + total);
        System.out.println("Prediction = " + prediction + " + k");
        System.out.println("So k = " + (total - prediction));
    }
}
