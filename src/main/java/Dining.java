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
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).start();
            if (i == 0) {
                //确保第一个哲学家已经拿起了左右两个筷子
                threads.get(i).join(1000);
            }
        }
    }
}
