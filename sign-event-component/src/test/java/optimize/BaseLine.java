package optimize;

public class BaseLine extends Accumulator {
    {id = "baseline";}

    @Override
    public void accumulate() {
        value += preLoaded[index++%SIZE];
        if (index >= SIZE) index = 0;
    }

    @Override
    public long read() {
        return value;
    }
}
