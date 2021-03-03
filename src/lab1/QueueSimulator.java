package lab1;

/*******************************************************************
 *  Lab 1, exercise 3.                                             *
 *******************************************************************/

import lab1.IntQueue;

import java.util.*;

public class QueueSimulator {
    /*
     *  Constants for t_a and t_s, the average time between arrivals in
     *  the queue and the average service time, respectively. Edit these
     *  constants and re-run the program to see the effect of varying
     *  the parameters. One customer arrives into the system as a whole
     *  every ARRIVAL_TIME minutes (on average), whereas each server
     *  takes an average of SERVICE_TIME minutes to deal with one cust-
     *  omer. Note that, if there are n servers, one customer will be
     *  served, on average, every SERVICE_TIME/n minutes.
     */
    private static final double ARRIVAL_TIME = 10;
    private static final double SERVICE_TIME = 40;

    /*
     *  main() method to run the simulations.
     */
    public static void main (String[] args) {
        nServersOneQueue (5);
        nServersNQueues (5);
    }

    /*
     *  Simulation time in minutes.
     */
    private static final int SIMULATION_LENGTH = 10_000;

    /*
     *  Used to denote a server who is idle because their queue is empty.
     */
    private static final int IDLE = -1;

    /*
     *  Random number generator for the probabilistic parts of the
     *  simulation.
     */
    private static final Random rand = new Random();

    /*******************************************************************
     *  Exercise 3a (and 3c).                                          *
     *******************************************************************
     *  The following method simulates a system where n servers each
     *  have their own queue. Exercise 3a is the case n=1, so you can
     *  investigate the scenario in that exercise by altering
     *  ARRIVAL_TIME and SERVICE_TIME and running nServersNQueues(1) in
     *  main().
     */
    private static void nServersNQueues (int n) {
        /*
         *  Create the n queues.
         */
        IntQueue[] queues = new IntQueue[n];
        for (int i = 0; i < n; i++)
            queues[i] = new IntQueue();

        /*
         *  serving[i] is either IDLE if the server is idle, or the
         *  id (i.e., the arrival time) of the customer who they are
         *  serving.
         */
        int[] serving = new int[n];

        /*
         *  At the start of the simulation, every server is idle.
         */
        Arrays.fill (serving, IDLE);

        /*
         *  Variables for collecting statistics.
         */
        int served = 0;
        double totalTime = 0;

        /*
         *  Main simulation loop.  Each time around the loop is one
         *  minute.
         */
        for (int t = 0; t < SIMULATION_LENGTH; t++) {
            /*
             *  With probability 1/t_a, a customer arrives.  They go
             *  to a random queue.  (Of course, if n=1, there's no
             *  choice.
             */
            if (rand.nextDouble () < 1/ARRIVAL_TIME)
                queues[rand.nextInt(n)].add (t);

            /*
             *  For each server in turn, if they're serving somebody,
             *  they finish with probability 1/t_s and become idle.
             *  In this case, the customer arrived at time serving[i]
             *  and it's now time t, so we can calculate their total
             *  time in the system.
             */
            for (int i = 0; i < n; i++)
                if (serving[i] != IDLE && rand.nextDouble () < 1/SERVICE_TIME) {
                    totalTime += t - serving[i];
                    served++;
                    serving[i] = IDLE;
                }
            /*
             *  For each server in turn, if they're idle (possibly
             *  because they just became idle in the previous for loop)
             *  and there's sombody in their queue, start serving that
             *  person.
             */
            for (int i = 0; i < n; i++) {
                if (serving[i] == IDLE && !queues[i].isEmpty())
                    serving[i] = queues[i].remove();
            }
        }

        /*
         *  Print statistics.
         */
        System.out.println("Customers served:     " + served);
        System.out.println("Average waiting time: " + totalTime / (double)served);
    }

    /*******************************************************************
     *  Exercise 3b.                                                   *
     *******************************************************************
     *  If t_s is much less than t_a, customers arrive rarely and are
     *  served fast. This means that the queue is usually empty, so an
     *  average customer will be served almost immediately and, there-
     *  fore, the amount of time they spend in the system will be about
     *  the average service time -- slightly more, because they might
     *  have to queue briefly.
     *
     *  If t_s > t_a, the queue just gets longer and longer: people are
     *  arriving in the queue faster than they're being served.
     *
     *  If t_s = t_a, you might expect things to be OK -- on average, a
     *  person arrives every t_a minutes but also one person leaves. In
     *  fact, though, the queue gets longer and longer again. The reason
     *  is that it's possible to be unlucky and either have several
     *  customers arrive in rapid succession, or have several customers
     *  take longer than average to be served. This makes the queue get
     *  longer. Once the queue is long, it will take a while to clear
     *  and, if you're unlucky again in that time, the queue gets even
     *  longer. Then it takes even longer to clear and you have even more
     *  chance of being unlucky and everything snowballs.
     *
     *  On the other hand, you might be lucky: you might have a period of
     *  time with few customers arriving and/or a succession of customers
     *  who are served quickly. Why doesn't this compensate for the
     *  unlucky periods? The answer is that bad luck can build up forever
     *  but good luck can't: you can't do better than an empty queue, but
     *  however long your queue is, it can still get worse.  For example,
     *  suppose that being unlucky for an hour adds 20 people to the
     *  queue and being lucky for an hour removes 20 people. If you're
     *  unlucky for an hour, then lucky for the next hour, then unlucky
     *  for an hour, the queue ends up with 20 people in it. However, it's
     *  just as likely that you'll be lucky for the first hour, then
     *  unlucky for two hours straight, ending up with 40 people in the
     *  queue. That first hour of good luck didn't help you, because you
     *  weren't able to remove people from the queue who hadn't arrived
     *  yet. To put it another way, the queue can't have negative length,
     *  which introduces an asymmetry.
     */

    /*******************************************************************
     *  Exercise 3c.                                                   *
     *******************************************************************
     *  Very similar to nServersNQueues(), except that now, all the
     *  servers share the same queue and take a customer from that when-
     *  ever they're free.
     */
    private static void nServersOneQueue (int n) {
        IntQueue queue = new IntQueue ();
        int[] serving = new int[n];

        Arrays.fill (serving, IDLE);

        int served = 0;
        double totalTime = 0;

        for (int t = 0; t < SIMULATION_LENGTH; t++) {
            if (rand.nextDouble () < 1/ARRIVAL_TIME)
                queue.add (t);
            for (int i = 0; i < n; i++)
                if (serving[i] != IDLE && rand.nextDouble () < 1/SERVICE_TIME) {
                    totalTime += t - serving[i];
                    served++;
                    serving[i] = IDLE;
                }
            for (int i = 0; i < n; i++) {
                if (serving[i] == IDLE && !queue.isEmpty())
                    serving[i] = queue.remove();
            }
        }

        System.out.println("Customers served:     " + served);
        System.out.println("Average waiting time: " + totalTime / (double)served);
    }
    /*
     *  On average, all the servers sharing the same queue is much more
     *  efficient. This is because servers will never be idle unless
     *  there are no customers at all. In a system with one queue per
     *  server, a server whose queue is empty will do no work, even if
     *  there are customers waiting elsewhere. Looking at it from the
     *  customer's perspective, with one queue per server, you'll be
     *  delayed if anybody in your queue is slow. With a shared queue,
     *  you're only delayed if all the servers are dealing with slow
     *  customers.
     */
}
