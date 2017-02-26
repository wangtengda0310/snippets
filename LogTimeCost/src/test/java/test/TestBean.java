package test;

import tenda.annotation.LogTimeCost;
import org.springframework.stereotype.Component;
import tenda.annotation.TimeUnit;

@Component
public class TestBean {
    @LogTimeCost()
    public int costMills(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }
    @LogTimeCost(TimeUnit.nano)
    public int costNanos(int i) throws InterruptedException {
        Thread.sleep(i);
        return i;
    }
}
