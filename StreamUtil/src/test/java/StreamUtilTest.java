import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tenda.StreamUtil;

import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static tenda.StreamUtil.join;

class StreamUtilTest {

    @Test
    void testJoinComma() {
        Stream.of("a", "b", "c")
                .collect(join(","))
                .with(s-> assertEquals("a,b,c",s));


        String returned = Stream.of("1", "2", "3")
                .collect(join(","))
                .andReturn(x->"returned: "+x+";"+x);
        assertEquals("returned: 1,2,3;1,2,3", returned);

        String[] string123 = new String[1];
        String returnedValue = Stream.of("1", "2", "3")
                .collect(join(","))
                .with(s -> string123[0] = s)
                .and(x-> System.out.println("打印 "+x))
                .and(x-> System.out.println("再次打印 "+x))
                .andReturn(x->"returned: "+x+";"+x);
        assertEquals("1,2,3", string123[0]);
        assertEquals("returned: 1,2,3;1,2,3", returnedValue);


        String collect = Stream.of("hello", "world")
        .collect(
            Collectors.collectingAndThen(
                    Collectors.joining(" ")
                    , s -> {
                        assertEquals("hello world", s);
                        return s;
                    })
        );
        assertEquals("hello world", collect);
    }

    @Test
    void ifTest() {
        Predicate<Integer> integerPredicate = x -> x % 2 == 0;
        new Random().ints(0,10)
                .boxed()
                .limit(10)

            // if (predicate.test(iterator.next()))
                .collect(StreamUtil.ifTest(integerPredicate))
            // {
                .thenConsume(x-> {
                    assertTrue(integerPredicate.test(x));
                    System.out.println("偶数"+x);
                })
            // } else {
                .elseConsume(x-> {
                    assertFalse(integerPredicate.test(x));
                    System.out.println("奇数"+x);
                })
            // }
        ;
    }
    @Test
    void testConsumerGenerics() {
        IntStream.range(0,10).boxed()
                .collect(StreamUtil.ifTest(x-> x%2==0))
                .thenConsumeAll(c -> Assertions.assertEquals("[0, 2, 4, 6, 8]",c.toString()))
                .elseConsumeAll(c -> Assertions.assertEquals("[1, 3, 5, 7, 9]",c.toString()));
    }
}