package lab2;

/*******************************************************************
 *  Lab 2, exercise 1.                                             *
 ******************************************************************/

public class SinglyLinkedList {
    private class Item {
        String value;
        Item next; /* No 'prev' reference in a singly linked list. */

        Item (String value, Item next) {
            this.value = value;
            this.next = next;
        }
    }

    /***************************************************************
     *  Exercise 2b.                                               *
     ***************************************************************
     *  We still keep a reference to the head and tail of the
     *  list, though tail is only needed for addToTail.  The init-
     *  ializer fulfills the role of create().
     */
    private Item head = null;
    private Item tail = null;
    private int length = 0;

    /*
     *  get() uses the helper method getItem to get the index-th
     *  Item in the list, and then returns its stored value.
     */
    public String get (int index) {
        if (index < 0 || index >= length)
            return null;
        else
            return getItem(index).value;
    }

    /*
     *  I've used addToHead() and addToTail() here. You could do
     *  it the other way around by including the code here and
     *  implementing addToHead(s) by just calling insert (0, s).
     */
    public void insert (int index, String value) {
        if (index <= 0)
            addToHead (value);
        else if (index >= length)
            addToTail (value);
        else {
            Item cur = getItem (index-1);
            cur.next = new Item (value, cur.next);
            length++;
        }
    }

    /*
     *  In a doubly linked list, delete() uses the prev refer-
     *  ence to access the previous item from the one being
     *  deleted. Here, we don't have prev; the easiest way is to
     *  look up the (index-1)th item and delete its next. Note
     *  that it would be inefficient to call both getItem(index)
     *  and getItem(index-1) as this would require walking
     *  through the list twice.
     */
    public void delete (int index) {
        if (index < 0 || index >= length)
            return;

        if (length == 1)
            head = tail = null;
        else if (index == 0)
            head = head.next;
        else {
            Item cur = getItem (index-1);
            cur.next = cur.next.next;
        }
        length--;
    }

    public boolean isEmpty () { return head == null; }
    public int length ()      { return length; }

    /*
     *  getItem returns the Item object that is in position
     *  'index' in the list. The method is for internal use only
     *  so it's private. The caller is responsible for ensuring that
     *  0 <= index < length.
     */
    private Item getItem (int index) {
        Item cur = head;
        for (int i = 0; i < index; i++)
            cur = cur.next;
        return cur;
    }

    /***************************************************************
     *  Exercise 2b.                                               *
     ***************************************************************/
    public void addToHead (String value) {
        if (head == null)
            head = tail = new Item (value, null);
        else
            head = new Item (value, head);
        length++;
    }

    public void addToTail (String value) {
        if (tail == null)
            head = tail = new Item (value, null);
        else {
            tail.next = new Item(value, null);
            tail = tail.next;
        }
        length++;
    }
}
