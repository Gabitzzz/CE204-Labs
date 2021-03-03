package lab1;

/*
 * Lab 1, Exercise 2.
 */

public class ArrayStack {
    private String[] entries = new String[10];
    private int size = 0;

    public boolean isEmpty () { return size == 0; }
    public int length ()      { return size;      }

    /*******************************************************************
     *  Exercise 2a.                                                   *
     *******************************************************************
     *  The code to decrease the length of the array is similar to the
     *  code that increases it. I've included a minimum size on the
     *  array: it only shrinks if it has more than ten elements. This is
     *  because, if the is very small, it will fill or empty with just a
     *  few operations, so we'll end up shrinking and growing it very
     *  often.
     */
    public String pop () {
        if (size == 0)
            return null;
        else {
            size--;
            String result = entries[size];

            if (entries.length > 10 && size < entries.length / 4) {
                String[] newEntries = new String[entries.length/2];
                System.arraycopy (entries, 0, newEntries, 0, size);
                copies += size;
                entries = newEntries;
            }

            return result;
        }
    }

    /*******************************************************************
     *  Exercise 2b.                                                   *
     *******************************************************************
     *  If we decrease by 50% when the the array drops to 50% full 50%
     *  full, then the resulting array will be 100% full. This means
     *  that, if the next operation is a push(), we will have to immedi-
     *  ately increase the size of the array again. But, now, the array
     *  is only 50% full so, if the next operation is a pop(), we'll
     *  shrink it again, and so on. This can lead to a lot of wasteful
     *  copying.
     *
     *  By decreasing only when the array is 25% full, the new array is
     *  itself only half-full so it will take many more pushes or pops
     *  before the size needs to change again. Similarly, when the array
     *  is expanded, it is half-full, which is a long way from the point
     *  where it shrinks (25% full) or grows again (100% full).
     *
     *  This is a good general principle if you're designing a structure
     *  that grows when it's full and shrinks when it's becoming empty:
     *  ensure that growing and shrinking both put the structure in a
     *  state where it won't need to grow or shrink again for a while.
     */

    /*******************************************************************
     *  Exercise 2c.                                                   *
     *******************************************************************
     *  First, we use an int field to keep track of how many array
     *  elements have been copied:
     */
    int copies = 0;
    /*
     *  Now, we have two versions of the push() method. pushWithDouble()
     *  is essentially identical to the method presented in the lec-
     *  tures: the only difference is that it adds to 'copies' when it
     *  copies array cells. pushWithAdd() is, again, almost the same
     *  except that it grows the array by adding 100 cells instead of
     *  doubling it.
     */
    public void pushWithDouble (String s) {
        entries[size] = s;
        size++;

        if (size >= entries.length) {
            String[] newEntries = new String[entries.length * 2];
            System.arraycopy (entries, 0, newEntries, 0, entries.length);
            copies += entries.length;
            entries = newEntries;
        }
    }

    public void pushWithAdd (String s) {
        entries[size] = s;
        size++;

        if (size >= entries.length) {
            String[] newEntries = new String[entries.length + 100];
            System.arraycopy (entries, 0, newEntries, 0, entries.length);
            copies += entries.length;
            entries = newEntries;
        }
    }

    /*
     *  A main() method to test the code. It creates a stack for each
     *  version of push() and makes 10,000 pushes to each.  Then it
     *  prints the number of copies made by each version, and the ratio
     *  between them. For 10,000 pushes, the additive method requires
     *  48 times more copying than the doubling method, and this gets
     *  worse as you push more items onto the stack: e.g., 97 times more
     *  for 20,000 pushes and 152 times more for 50,000.
     */
    public static void main (String[] args) {
        ArrayStack adder = new ArrayStack ();
        ArrayStack doubler = new ArrayStack();

        for (int i = 0; i < 50_000; i++) {
            adder.pushWithAdd ("");
            doubler.pushWithDouble ("");
        }

        System.out.println("Adder:   " + adder.copies);
        System.out.println("Doubler: " + doubler.copies);
        System.out.println("Adder required " + adder.copies / doubler.copies
                + " times more copying");
    }
}
