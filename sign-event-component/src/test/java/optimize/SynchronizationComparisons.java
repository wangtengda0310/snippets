package optimize;

public class SynchronizationComparisons {
    static BaseLine baseLine = new BaseLine();
    static SynchronizedTest synchronizedTest = new SynchronizedTest();
    static LockTest lockTest = new LockTest();
    static AtomicTest atomicTest = new AtomicTest();

    static void test() {
        System.out.println("=================");
        System.out.printf("%-12s :%13d\n","Cycles", Accumulator.cycles);
        baseLine.timedTest();
        synchronizedTest.timedTest();
        lockTest.timedTest();
        atomicTest.timedTest();

        Accumulator.report(synchronizedTest, baseLine);
        Accumulator.report(lockTest, baseLine);
        Accumulator.report(atomicTest, baseLine);
        Accumulator.report(synchronizedTest, lockTest);
        Accumulator.report(synchronizedTest, atomicTest);
        Accumulator.report(lockTest, atomicTest);
    }

    public static void main(String[] args) {
        int iterations = 5;
        System.out.println("Warmup");
        // the first time fills the thread pool
        baseLine.timedTest();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();

        // the initial test doesnt include the cost of starting the threads for the first time
        // produce multiple data points:
        for (int i = 0; i < iterations; i++) {
            test();
            Accumulator.cycles *= 2;
        }
        Accumulator.exec.shutdown();
    }
}
