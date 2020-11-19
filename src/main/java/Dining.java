import java.util.ArrayList;
import java.util.List;

public class Dining {

    public static void main(String[] args) throws Exception {
        Philosopher[] philosophers = new Philosopher[5];
        Fork[] forks = new Fork[philosophers.length];

        for (int i = 0; i < forks.length; i++) {
            // initialize fork object
            forks[i] = new Fork();
        }

        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < philosophers.length; i++) {
            // initialize Philosopher object
            philosophers[i] = new Philosopher(forks[i], forks[(i + 1) % (philosophers.length - 1)]);
            threads.add(new Thread(philosophers[i], "Philosopher " + (i + 1)));
        }

        //开始吃饭
        for (Thread t : threads) {
            t.start();
        }
    }
}
