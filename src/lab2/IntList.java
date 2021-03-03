package lab2;

/*******************************************************************
 *  Lab 2, exercise 1.                                             *
 ******************************************************************/

/*******************************************************************
 *  Exercise 2a.                                                   *
 ******************************************************************/

public class IntList {
    private int value;
    private IntList tail;

    public IntList (int value) { this.value = value; }

    /*
     *  The only way to change the head of the list is to return
     *  a new list that has that head.
     */
    public IntList cons (int value) {
        IntList list = new IntList (value);
        list.tail = this;
        return list;
    }

    public int head() { return value; }
    public IntList tail() { return tail; }

    /***************************************************************
     *  Exercise 2b.                                               *
     ***************************************************************
     *  I put the code to make the list 1, ..., n in the main()
     *  method, along with test code for the other parts of the
     *  exercise.
     */
    public static void main (String[] args) {
        final int SIZE = 10;
        IntList list = new IntList (SIZE);
        for (int i = SIZE-1; i > 0; i--)
            list = list.cons (i);

        System.out.println("List is " + list);
        System.out.println("List containsWord 5? " + list.contains (5));
        System.out.println("List containsWord 25? " + list.contains (25));
        System.out.println("Sum is " + list.sum());
        System.out.println("Evens values are " + list.evenValues());
        System.out.println("Odd indices are " + list.everyOtherValue());
        System.out.println("Even indices are " + list.otherEveryOtherValue());
        System.out.println("Reversed list is " + list.reverse());
    }

    /***************************************************************
     *  Exercise 2c.                                               *
     ***************************************************************
     *  Compute the length of a list. If tail is null, then 'this'
     *  is the only item in the list, so it has length 1.  Other-
     *  wise, the list is one item plus its tail.
     */
    public int length() {
        return (tail == null) ? 1 : 1 + tail.length();
    }

    /***************************************************************
     *  Exercise 2d.                                               *
     ***************************************************************
     *  Convert the list to a string. If the tail is null, the value
     *  in this object is the only value in the list, so we return
     *  its string representation. Otherwise, 'this' is the first
     *  item of a list, so we output its value, a comma and the rest
     *  of the list.
     */
    public String toString () {
        if (tail == null)
            return Integer.toString (value);
        else
            return Integer.toString (value) + ", " + tail.toString ();
    }

    /***************************************************************
     *  Exercise 2e.                                               *
     ***************************************************************
     *  A list contains the integer 'i' if either the first item is
     *  'i' or there is a tail and the tail contains 'i'.
     */
    public boolean contains (int i) {
        return (value == i) || (tail != null && tail.contains (i));
    }

    /***************************************************************
     *  Exercise 2f.                                               *
     ***************************************************************
     *  The sum of a one-item list is just the value it holds; the
     *  sum of a longer list is the first item plus the sum of the
     *  rest of the list.
     */
    public int sum () {
        if (tail == null)
            return this.value;
        else
            return this.value + tail.sum ();
    }

    /***************************************************************
     *  Exercise 2g.                                               *
     ***************************************************************
     *  I'm going to call the first, third, fifth, ... elements of a
     *  list the "odd-position elements". This means I'm indexing
     *  from one. Please don't sue me. If your solution indexed from
     *  zero, that's fine; the code will be slightly different.
     *
     *  If you assume the list is really long, then its odd-position
     *  elements are the first element, followed by the odd-position
     *  elements of the list you get by deleting the first two
     *  elements. Or, to put it another way, you get the answer by
     *  taking the odd-position elements of that sublist (which you
     *  get by calling tail.tail.everyOtherValue(), with the first
     *  element stuck on the front (which you get by calling cons()
     *  on that list.
     *
     *  That works if the list's length is at least three. If the
     *  list's length is one or two, the output is just the first
     *  element. Notice that I'm not using the length() method to
     *  find out the length: that would be inefficient, as it must
     *  walk all the way down the list to calculate its exact
     *  length. We don't need to know the exact length: we just
     *  need to know if it's one, two or something else.
     */
    public IntList everyOtherValue () {
        if (tail == null || tail.tail == null)
            return new IntList (value);
        else
            return tail.tail.everyOtherValue().cons (value);
    }

    /***************************************************************
     *  Exercise 2h.                                               *
     ***************************************************************
     *  You could get the even-position elements using a very
     *  similar method to part 2g. The easier way is just to observe
     *  that the even position elements of a list are the odd-posi-
     *  tion elements of its tail.
     */
    public IntList otherEveryOtherValue () {
        if (tail == null)
            return null;
        else
            return tail.everyOtherValue();
    }

    /***************************************************************
     *  Exercise 2i.                                               *
     ***************************************************************
     *  Now, we want to extract the even numbers from the list. We
     *  first extract the evens from the tail of the list and store
     *  that list in a local variable, as we'll need to refer to it
     *  a few times. We add the head of 'this' list to the front
     *  of the result, if it's even and the result is non-null.
     */
    public IntList evenValues () {
        IntList theRest = null;
        if (tail != null)
            theRest = tail.evenValues();

        if (theRest == null)
            return (value%2 == 0) ? new IntList (value) : null;
        else
            return (value%2 == 0) ? theRest.cons (value) : theRest;
    }

    /***************************************************************
     *  Exercise 2j.                                               *
     ***************************************************************
     *  To reverse a list, we use a helper method that takes the
     *  list that's been processed so far as an argument.
     *
     *  [1,2,3].reverse calls [1,2,3].reverseHelper([])
     *  which calls [2,3].reverseHelper([1])
     *  which calls [3].reverseHelper([2,1])
     *  which returns [3,2,1].
     *
     *  Essentially, we're using the fact that a list that only
     *  allows addition and removal at the head is essentially just
     *  a stack, and you can reverse a stack by popping everything
     *  and pushing the items onto a new stack
     */
    public IntList reverse () {
        if (tail == null)
            return new IntList (value);
        else
            return tail.reverseHelper (new IntList (value));
    }

    private IntList reverseHelper (IntList listSoFar) {
        if (tail == null)
            return listSoFar.cons (value);
        else
            return tail.reverseHelper(listSoFar.cons(value));
    }
}
