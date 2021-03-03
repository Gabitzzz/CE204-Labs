package lab3;

/*******************************************************************
 *  Lab 3, exercise 2.                                             *
 *******************************************************************
 *  The first part of the class is just the basic binary tree class
 *  from the lectures, except that the nodes store ints rather than
 *  Strings.
 */

public class BinaryTree {
    private int value;
    private BinaryTree left;
    private BinaryTree right;

    public BinaryTree (int value) { this (value, null, null); }

    public BinaryTree (int value, BinaryTree left, BinaryTree right) {
        this.value = value;
        this.left  = left;
        this.right = right;
    }

    public boolean isLeaf () { return left == null && right == null; }
    public BinaryTree leftChild ()  { return left;  }
    public BinaryTree rightChild () { return right; }
    public int value () { return value; }

    /***************************************************************
     *  Exercise 2a.                                               *
     ***************************************************************
     *  The number of nodes in a tree is one plus the size of the
     *  left subtree (if it exists) plus the size of the right sub-
     *  tree (if it exists).
     */
    public int size () {
        int leftSize = (left == null) ? 0 : left.size ();
        int rightSize = (right == null) ? 0 : right.size ();
        return 1 + leftSize + rightSize;
    }

    /***************************************************************
     *  Exercise 2b.                                               *
     ***************************************************************
     *  If a tree has only one node, its height is zero. Otherwise,
     *  the height is one greater than the height of the tallest
     *  subtree.
     */
    public int height () {
        if (left == null && right == null)
            return 0;

        int leftHeight = (left == null) ? 0 : left.height ();
        int rightHeight = (right == null) ? 0 : right.height ();
        return 1 + Math.max (leftHeight, rightHeight);
    }

    /***************************************************************
     *  Exercise 2c.                                               *
     **************************************************************/
    public BinaryTree mirror () {
        BinaryTree mirrorLeft = (left == null) ? null : left.mirror ();
        BinaryTree mirrorRight = (right == null) ? null : right.mirror ();
        return new BinaryTree(value, mirrorRight, mirrorLeft);
    }

    /***************************************************************
     *  Exercise 2d.                                               *
     ***************************************************************
     *  We know the main property of a binary search tree: the
     *  values in the left subtree must be less than the value at
     *  the root, and the values in the right subtree must be
     *  greater than it.
     *
     *  Now, Suppose the root of a binary search tree holds the
     *  value x and its left child holds value y. The left child's
     *  right subtree can only contain values that are greater than
     *  y (because they're in y's right subtree) and values less
     *  than x (because they're in x's left subtree). We can gener-
     *  alize this by saying that, if a node z is in a subtree where
     *  all values are between min and max, then we must have:
     *      i) min <= z <= max
     *     ii) every value in z's left subtree must be between
     *         min and z-1
     *    iii) every value in z's right subtree must be between
     *         z+1 and max.
     *
     *  The final piece of the picture is that the root node must
     *  be within the range of values representable in an int.
     */
    public boolean isBinarySearchTree () {
        return isBSTHelper (Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private boolean isBSTHelper (int min, int max) {
        return min <= value && value <= max
                && (left == null || left.isBSTHelper (min, value-1))
                && (right == null || right.isBSTHelper (value+1, max));
    }

    public static void main (String[] args) {
        /*
         *  tree1:     5         tree2:     5
         *           /   \                /   \
         *          /     \              /     \
         *         3       8            3       8
         *          \     / \            \     / \
         *           4   7   9            6   7   9
         */
        BinaryTree tree1 =
                new BinaryTree (5,
                        new BinaryTree (3, null, new BinaryTree (4)),
                        new BinaryTree (8, new BinaryTree (7), new BinaryTree (9)));
        BinaryTree tree2 =
                new BinaryTree (5,
                        new BinaryTree (3, null, new BinaryTree (6)),
                        new BinaryTree (8, new BinaryTree (7), new BinaryTree (9)));

        System.out.println("tree1 is a BST? " + tree1.isBinarySearchTree());
        System.out.println("tree2 is a BST? " + tree2.isBinarySearchTree());

        /*
         *  tree2 shows that it's not enough to test that every node is
         *  greater than its left child and less than its right child.
         */
    }
}
