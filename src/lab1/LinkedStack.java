package lab1;

/***********************************************************************
 *  Lab 1, exercise 1.                                                 *
 ***********************************************************************/

/***********************************************************************
 *  Exercise 1a                                                        *
 ***********************************************************************/

public class LinkedStack /* implements StackADT */ {
    /*
     *  I've commented out the 'implements' statement, so you can run the
     *  code even if the StackADT interface isn't in the right directory.
     *  When I wrote about "implementing the stack ADT" in the lab sheet,
     *  I was thinking in terms of "write some code that does the things
     *  a stack is supposed to do", rather than making a literal statement
     *  about a Java language interface.
     */

    /*
     *  Items on the stack have a pointer to the previous one. I'll
     *  maintain my stack of books on a table analogy, so an item's
     *  'prev' reference points to the item below it.
     */
    private class Item {
        String data;
        Item prev;

        /*
         *  For queues, the most convenient constructor took one argument
         *  because we always set the 'next' reference to null. For
         *  stacks, we always set 'prev' to the current top of the stack.
         */
        Item (String data, Item prev) {
            this.data = data;
            this.prev = prev;
        }
    }

    /*
     *  We need to remember where the front and back of a queue are,
     *  since we remove from the front and add to the back. In a stack,
     *  we both add and remove from the top, so that's all we need to
     *  keep track of.
     */
    Item top = null;
    int size = 0;

    /*
     *  To push, we make a new item, set its 'prev' reference to the
     *  current top of the stack, and set the new item to be the new top.
     */
    void push (String s) {
        top = new Item(s, top);
        size++;
    }

    /*
     *  To pop, we retrieve the string from the current top item, set
     *  the top to be the previous item (i.e., the item below the one
     *  we just removed) and return the string. Since there are no
     *  longer any references to the old top Item, the garbage collec-
     *  tor will clean it up for us.
     */
    String pop () {
        if (isEmpty ())
            return null;

        Item oldTop = top;
        top = top.prev;
        size--;
        return oldTop.data;
    }

    int length() { return size; }
    boolean isEmpty () { return size == 0; }

    /*******************************************************************
     *  Exercise 1b                                                    *
     *******************************************************************
     *  multiPush() just needs to push the strings in the array. Don't
     *  reinvent the wheel by writing all the push code again -- just
     *  call push()!
     */
    void multiPush (String[] strings) {
        for (int i = 0; i < strings.length; i++)
            push (strings[i]);
    }

    /*******************************************************************
     *  Exercise 1c                                                    *
     *******************************************************************
     *  Likewise, multiPop() uses pop() for the actual stack manipula-
     *  tion.  The only thing here is to check that 'n' is a valid
     *  number of things to pop.
     */
    String[] multiPop (int n) {
        if (n < 0)
            n = 0;
        if (n > size)
            n = size;

        String[] strings = new String[n];
        for (int i = 0; i < n; i++)
            strings[i] = pop ();
        return strings;
    }

    /*******************************************************************
     *  Exercise 1d                                                    *
     *******************************************************************
     *  The most idiomatic way to merge the stacks is to find the bottom
     *  element of 'that', and set it's 'prev' reference to the top
     *  element of 'this'. This operation of following a chain of refer-
     *  ences is very common when data structures are implemented this
     *  way -- for example, you'll often see it in trees.
     */
    void merge (LinkedStack that) {
        /*
         *  'cur' will walk down 'that' stack until it finds an item
         *  with no previous item, i.e., the bottom of the stack.
         */
        Item cur = that.top;
        if (cur == null)
            return;    /* 'that' is an empty stack, so nothing to do. */
        while (cur.prev != null) {
            cur = cur.prev;
        }

        /*
         *  Now, patch the two stacks together: 'this's top becomes the
         *  predecessor of 'that's bottom and the top of the combined
         *  stack is 'that's top.
         */
        cur.prev = top;
        top = that.top;
        that.top = null;
        this.size += that.size;
        that.size = 0;
    }

    /*
     *  This is an alternative solution. Just popping everything from
     *  'that' onto 'this' will reverse the order of the added items:
     *  the top of 'that' will end up in the middle of the combined
     *  stack, instead of on the top. However, we can pop all of 'that'
     *  onto a temporary stack, and then pop all of the temporary
     *  stack onto 'this'.
     *
     *  I like this solution because it only uses the operations of
     *  the ADT, which means that it will work for any implementation.
     *  The down-side is that it's rather slow, as it involves creat-
     *  ing and destroying many temporary objects. Often, practical
     *  considerations such as this are higher priorities than the
     *  theoretical purity of operating purely within the ADT.
     */
    void slowerMerge (LinkedStack that) {
        LinkedStack temp = new LinkedStack ();
        while (!that.isEmpty())
            temp.push (that.pop());
        while (!temp.isEmpty ())
            this.push (temp.pop ());
    }
}
