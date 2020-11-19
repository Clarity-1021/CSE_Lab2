public class Philosopher implements Runnable {

    private static final String PICK_UP_LEFT_FORK = "Picked up left fork";
    private static final String EAT = "Picked up right fork - eating";
    private static final String PUT_DOWN_LEFT_FORK = "Put down left fork";
    private static final String PUT_DOWN_RIGHT_FORK = "Put down right fork";
    private static final String THINK = "Thinking";

    private final Fork leftFork;
    private final Fork rightFork;

    /**
     * 初始化哲学家和他应该用的两把叉子
     */
    public Philosopher(Fork leftFork, Fork rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        boolean pickUpFork = false;//拿到筷子的flag
        try {
            while (!pickUpFork) {
                pickUpFork = pick_up_forks();//拿筷子，拿到筷子了就吃饭
                if (!pickUpFork) {//没拿到，去重新拿
                    continue;
                }
                eat();//拿到了筷子开始吃饭
                put_down_right_fork();//放右筷子
                put_down_left_fork();//放左筷子
                think();//思考
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 拿筷子
     * @return 拿到了就返回true；拿不到就返回false
     */
    private boolean pick_up_forks()  {
        boolean result = false;

        //先锁住左筷子
        synchronized (leftFork) {
            //检查左筷子能不能拿
            if (leftFork.isFree()) {
                //左筷子能拿的话就锁住右筷子
                synchronized (rightFork) {
                    //检查右筷子能不能拿
                    if (rightFork.isFree()) {
                        //右筷子能拿，就拿起两个筷子
                        printAction(PICK_UP_LEFT_FORK);
                        leftFork.pickUp();
                        rightFork.pickUp();
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 吃饭
     */
    private void eat() throws InterruptedException {
        printAction(EAT);
        Thread.sleep(((int) (Math.random() * 100)));
    }

    /**
     * 放左筷子
     */
    private void put_down_left_fork() {
        printAction(PUT_DOWN_LEFT_FORK);
        leftFork.putDown();
    }

    /**
     * 放右筷子
     */
    private void put_down_right_fork() {
        printAction(PUT_DOWN_RIGHT_FORK);
        rightFork.putDown();
    }

    /**
     * 思考
     */
    private void think() throws InterruptedException {
        printAction(THINK);
        Thread.sleep(((int) (Math.random() * 100)));
    }

    /**
     * 打印正在做的事
     */
    private void printAction(String ACTION) {
        System.out.println(Thread.currentThread().getName() + " " + System.nanoTime() + ": " + ACTION);
    }
}
