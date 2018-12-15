package test;

import tenda.annotation.BindSpring;
import org.springframework.stereotype.Component;

@Component
public class TestBean {
    @BindSpring
    public SomeObject foobar(int i) {
        return new SomeObject(i);
    }
}
