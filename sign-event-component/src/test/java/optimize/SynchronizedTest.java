package optimize;

public class SynchronizedTest extends Accumulator {
    {id="synchronized";}
    @Override
    public synchronized void accumulate() {
        value += preLoaded[index++%SIZE];
        if (index>=SIZE) index = 0;
    }

    @Override
    public long read() {
        return value;
    }
}
