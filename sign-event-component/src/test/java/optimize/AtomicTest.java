package optimize;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AtomicTest extends Accumulator {
    {id="atomic";}

    private volatile int index = 0;
    private AtomicLong value = new AtomicLong(0);
    @Override
    public void accumulate() {
        // 注意 多个Atomic类不能同时工作 这里凑合用以下
        int i = index++;
        value.getAndAdd(preLoaded[i%SIZE]);
        if (i >= SIZE) {
            index = 0;
        }
    }

    @Override
    public long read() {
        return value.get();
    }
}
