import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test.SomeObject;
import test.TestBean;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:Spring.xml","classpath:Spring-test.xml"})
public class BindSpringAspectTest {
    @Autowired
    private TestBean bean;

    @Test
    public void testBind() {
        SomeObject foobar = bean.foobar(10);
        assertNotNull(foobar.bean);
    }
}