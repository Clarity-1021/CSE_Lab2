public class Fork {

    /**
     * 叉子是否被拿起的状态flag
     */
    private boolean picked;

    /**
     * 初始化一个叉子是没有被拿起的状态
     */
    public Fork() {
        this.picked = false;
    }

    /**
     * 叉子是否没有被占用是可以拿起的
     */
    public boolean isFree() {
        return !picked;
    }

    /**
     * 拿起叉子
     */
    public void pickUp() {
        this.picked = true;
    }

    /**
     * 放下叉子
     */
    public synchronized void putDown() {
        this.picked = false;
    }
}
