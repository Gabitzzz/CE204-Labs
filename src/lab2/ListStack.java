package lab2;

/*******************************************************************
 *  Lab 2, exercise 2.                                             *
 *******************************************************************
 *
 *  Here, I just give the solution for a stack. Queues are almost
 *  identical, except that push() is renamed "add()", pop is renamed
 *  remove() and push() uses addToTail() instead of addToHead().
 */
public class ListStack {
    SinglyLinkedList items;

    /*
     *  We use the singly linked list class from exercise 1 to store
     *  the items on the stack. Push() will be implemented by adding
     *  an item to the head of the list and pop() by removing the
     *  item from the head.
     */
    ListStack () {
        items = new SinglyLinkedList ();
    }

    /*
     *  Since I've used a singly linked list, items of the stack (or
     *  queue) must be removed from the head of the list.  This is
     *  because removing from the tail requires time proportional to
     *  the length of the list, but the time of pop()/remove() needs
     *  to be independent of the number of items on the stack.  If
     *  I'd used a doubly linked list, it would be possible to remove
     *  from the tail of the list in constant time, so we could store
     *  the queue/stack either way around.
     */
    public String pop () {
        String s = null;
        if (!items.isEmpty ()) {
            s = items.get (0);
            items.delete (0);
        }
        return s;
    }

    /*
     *  Because items are being removed from the head of the list,
     *  pop() must add the new items there. For a queue, we would use
     *  addToTail().
     */
    public void push (String s) {
        items.addToHead(s);
    }

    public boolean isEmpty () { return items.isEmpty(); }
    public int length () { return items.length (); }

    /*
     *  main() method to test the stack.
     */
    public static void main (String[] args) {
        ListStack stack = new ListStack();

        for (String s : new String[]{"one", "two", "three", "four"})
            stack.push (s);

        while (!stack.isEmpty())
            System.out.println(stack.pop());
    }
}
