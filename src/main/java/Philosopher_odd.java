//示例输出如下：
//Philosopher 4 88519840870182: Thinking
//Philosopher 5 88519840956443: Thinking
//Philosopher 3 88519864404195: Picked up left fork
//Philosopher 5 88519871990082: Picked up left fork
//Philosopher 4 88519874059504: Picked up left fork
//Philosopher 5 88519876989405: Picked up right fork - eating
//Philosopher 2 88519935045524: Picked up left fork

public class Philosopher_odd implements Runnable {

//    为了保证每只叉⼦不被多个⼈拿到，需要为叉⼦上锁，建议使⽤synchronized关键字；
    private static final String THINK_ACTION = "Thinking";
    private static final String LEFT_FORK_ACTION = "Picked up left fork";
    private static final String RIGHT_FORK_ACTION = "Picked up right fork";
    private static final String EAT_LEFT_ACTION = "Picked up right fork - eating";
    private static final String EAT_RIGHT_ACTION = "Picked up left fork - eating";
    private final Object leftFork;
    private final Object rightFork;

    public Philosopher_odd(Object leftFork, Object rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    private void doAction(String action) throws InterruptedException{
        System.out.println(Thread.currentThread().getName() + " " + System.nanoTime() + ": " + action);
        Thread.sleep(((int) (Math.random() * 100)));
    }

    @Override
    public void run(){
        try {
//            可参考伪代码：
//            while(true){
//                think();
//                pick_up_left_fork();
//                pick_up_right_fork();
//                eat();
//                put_down_right_fork();
//                put_down_left_fork();
//            }
            while(true){
                doAction(THINK_ACTION); // thinking
                // your code

            }
        } catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}


