# CSE_Lab2

###### 18307130251 蒋晓雯

## Exercise 1

###### 思考分析Semaphore和Mutex的异同，举例说明⼆者适⽤的场景，在何种情况下可以达到相同的功能，在何种情况下具有各⾃的独特性，可以从⼆者考虑问题的⽅向、解决问题的表现、线程和资源的关系等⻆度分析，500字以内。

### Semaphore和Mutex

mutex有两个机制，wait(mutex)和signal(mutex)。signal(mutex)对mutex++，表示放弃锁。wait(mutex)是等待mutex为正，否者一直执行一个判断mutex是否为正的循环，直到mutex为正时对mutex--，表示拿到锁。mutex初始值为1。线程执行时，为了保护critical section的原子性，在执行critical section前wait(mutex)，在执行结束后signal(mutex)。

由此可以推理出，当几个线程并行执行时，第一个判断mutex为正的线程P1拿到锁，并mutex--，此时mutex不再为正，此时其他线程被阻塞在wait(mutex)这一步无法执行自己的critical section，直到P1执行完自己的critical section后signal(mutex)释放锁，mutex重新为正。此时开始第一个判断mutex为正的线程第二个拿到锁再执行自己的critical section，同时其他线程仍被阻塞在wait(mutex)这一步...这把互斥的锁实现了让所有的线程排队，一个一个串行执行的目的。

semaphore的机制也分wait(S)和signal(S)。Semaphore可以表示互斥锁，也可以表示可用资源的个数和已经使用的资源个数。与nutex不同，有不只一个线程可以拿到这个资源，有多少个资源，就有多少线程可以拿到。

mutex可以看做是表示资源的使用权，而semaphore可以看做是资源的使用个数。

相同功能：semaphore可以看做是有counter的mutex，mutex可以看做是最大值为1的semaphore。semaphore表示的可用资源个数为1时，即为可以看做是该资源的使用权，可看做是mutex。而增加mutex使用权的个数，也可以表示为可用资源的个数，可看做是semaphore。

mutex适用场景：写者模式，共享的数据同时只允许一个线程进行修改，就可以使用mutex让这些线程排队，保证每一次写都是原子性的。

semaphore适用场景：对bundered buffer的写，semaphore可用表示可以被写者写的bit的个数（即为已经被读者读过的bit数）。读者读过之后特定的表示资源个数的semaphore++（signal(S)），写者在发现可用资源为正时表示资源个数的semaphore--（wait(S)），这个时候才能够去写。而写的时候仍存在共享数据同时只允许一个线程进行修改的问题，需要用mutex让这些拿到资源的线程排队。

# Exercise 2

考虑下述程序：

```java
int x = 0, y = 0, z = 0;
sem lock1 = 1, lock2 = 1;
process foo{
    z = z + 2;
    P(lock1);
    x = x + 2;
    P(lock2);
    V(lcok1);
    y = y + 2;
    V(lock2);
}
process bar{
	P(lock2);	
    y = y + 1;
    P(lock1);
    x = x + 1;
    V(lock1);
    V(lock2);
    z = z + 1;
}
```




### 1. 在何种情况下程序会产⽣死锁？

并行的情况下，bar的P(lock2)比foo的P(lock2)先执行时会产生死锁。foo拿到了lock1，bar拿到了lock2，foo会被阻塞在P(lock2)，bar会被阻塞在P(lock1)，两个线程都会等待锁的释放，但是由于各自阻塞住了，没有办法执行之后的释放锁的语句，使得两个线程都处于停滞的状态。

### 2. 在死锁状态下，x，y，z的最终值可能是多少？

x=2,y=1,z=2

### 3. 如果程序正常终⽌，x，y，z的最终值可能是多少？（提示：对z的赋值操作不是原⼦性的）

foo(P1)->foo(P2)->foo(V1)->foo(V2)->bar(P2)->bar(P1)->bar(V1)->bar(V2): x=3,y=3,z=3

bar(P2)->bar(P1)->bar(V1)->bar(V2)->foo(P1)->foo(P2)->foo(V1)->foo(V2): x=3,y=3,z=3/x=3,y=3,z=1/x=3,y=3,z=2

# Exercise 3

###### 多个进程共享U个资源，1个进程1次可以获取（request）1个资源，但是可能释放（release）多个。使⽤信号量做同步，合理进⾏P，V操作，实现request和release⽅法的伪代码，确保request和release操作是原⼦性的。提前声明和初始化你⽤到的变量。
```java
int free = U;
# your variables
sem lock1 = 1;//同时只有一个线程能改变共享变量free的值
sem lock2 = free;//用来通知request有可用的资源

# <await (free > 0) free = free - 1;>
request(){
	# your code
    P(lock2);
    P(lock1);
    free--;
    V(lock1);
}

# <free = free + number;>
release(int number){
	# your code
    P(lock1);
    free = free + number;
    V(lock1);
    while (int i = 0; i < number; i++) {
        V(lock2);
    }
}
```

# Exercise 4

思路：总是同时拿起左右两个筷子，在锁住左边筷子的时候如果左筷子空闲就锁住右筷子检查是否空闲，如果右筷子空闲，才拿起两只筷子开始吃饭，否则重新拿筷子。

可能产生死锁：如果所有哲学家都锁住自己的左筷子，检查到空闲，开始尝试锁住自己的右筷子，那么所有哲学家的右筷子此时都被自己右边的哲学家锁住，产生死锁。

解决方法：确保第一个哲学家已经拿起了左右两只筷子，再让别的哲学家开始拿筷子吃饭。

测试1：

```bash
Philosopher 1 4201489844400: Picked up left fork
Philosopher 1 4201490216800: Picked up right fork - eating
Philosopher 1 4201538422500: Put down right fork
Philosopher 1 4201539047900: Put down left fork
Philosopher 1 4201539132699: Thinking
Philosopher 2 4201603835400: Picked up left fork
Philosopher 4 4201604161999: Picked up left fork
Philosopher 4 4201604336300: Picked up right fork - eating
Philosopher 2 4201605255400: Picked up right fork - eating
Philosopher 4 4201655405000: Put down right fork
Philosopher 4 4201655483100: Put down left fork
Philosopher 4 4201655517500: Thinking
Philosopher 2 4201699358199: Put down right fork
Philosopher 2 4201699710699: Put down left fork
Philosopher 3 4201699772900: Picked up left fork
Philosopher 3 4201699983300: Picked up right fork - eating
Philosopher 2 4201700319100: Thinking
Philosopher 5 4201700401400: Picked up left fork
Philosopher 5 4201700456300: Picked up right fork - eating
Philosopher 5 4201747692300: Put down right fork
Philosopher 5 4201748300399: Put down left fork
Philosopher 5 4201748512000: Thinking
Philosopher 3 4201784272300: Put down right fork
Philosopher 3 4201784362300: Put down left fork
Philosopher 3 4201784401099: Thinking
```

测试2：

```bash
Philosopher 1 4152846405700: Picked up left fork
Philosopher 1 4152846778299: Picked up right fork - eating
Philosopher 1 4152915336000: Put down right fork
Philosopher 1 4152915642500: Put down left fork
Philosopher 1 4152915747000: Thinking
Philosopher 2 4153003988400: Picked up left fork
Philosopher 2 4153005112799: Picked up right fork - eating
Philosopher 4 4153005580299: Picked up left fork
Philosopher 4 4153005783300: Picked up right fork - eating
Philosopher 4 4153038698499: Put down right fork
Philosopher 4 4153039000900: Put down left fork
Philosopher 4 4153039187100: Thinking
Philosopher 2 4153039377899: Put down right fork
Philosopher 2 4153039815800: Put down left fork
Philosopher 2 4153039868300: Thinking
Philosopher 5 4153039893100: Picked up left fork
Philosopher 5 4153039995600: Picked up right fork - eating
Philosopher 3 4153040046299: Picked up left fork
Philosopher 3 4153040158200: Picked up right fork - eating
Philosopher 3 4153060186300: Put down right fork
Philosopher 3 4153060264400: Put down left fork
Philosopher 3 4153060312700: Thinking
Philosopher 5 4153119115600: Put down right fork
Philosopher 5 4153119400200: Put down left fork
Philosopher 5 4153122347499: Thinking
```

