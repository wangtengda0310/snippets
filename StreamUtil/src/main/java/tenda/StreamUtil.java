package tenda;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StreamUtil {
    public static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Function<A,R> finisher,
                      Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }

    /**
     * 用逗号合并字符串
     * 是{@link Collectors#collectingAndThen}的变形用法
     * <pre>{@code
     *  String collect = Stream.of("hello", "world", "and", "you")
     *    .collect(
     *      Collectors.collectingAndThen(
     *        Collectors.joining(","),
     *        s -> {
     *          assertEquals("hello,world", s);
     *          return s;
     *        }
     *    )
     *  );
     * }</pre>
     * 用这个方法改写后
     * <pre>{@code
     *  Stream.of("hello", "world", "and", "you")
     *    .collect(tenda.StreamUtil.join(","))
     *    .with(s-> assertEquals("hello,world", s));
     *    .and(System.out::println)
     *    .andReturn(x->x);
     * }</pre>
     * @return {@link JoinCommaThenConsumer}
     */
    public static
    Collector<String
            , StringJoiner
            , JoinCommaThenConsumer>
    join(String splitter) {
        return new CollectorImpl<>(
                () -> new StringJoiner(splitter, "", ""),
                StringJoiner::add
                , StringJoiner::merge,
                s -> new JoinCommaThenConsumerImpl(s.toString()),
                Collections.emptySet());
    }

    public interface JoinCommaThenConsumer {
        JoinCommaMultiConsumer with(Consumer<String> t);
        <T> T andReturn(Function<String, T> t);
    }
    public interface JoinCommaMultiConsumer {
        JoinCommaMultiConsumer and(Consumer<String> t);
        <T> T andReturn(Function<String, T> t);
    }

    private static class JoinCommaThenConsumerImpl implements JoinCommaThenConsumer {
        String t;
        JoinCommaThenConsumerImpl(String t) {
            this.t = t;
        }
        @Override
        public JoinCommaMultiConsumer with(Consumer<String> after) {
            Objects.requireNonNull(after);
            after.accept(t);
            return new JoinCommaMultiConsumerImpl(t);
        }

        @Override
        public <T> T andReturn(Function<String, T> f) {
            return f.apply(t);
        }
    }

    private static class JoinCommaMultiConsumerImpl implements JoinCommaMultiConsumer {
        String t;
        JoinCommaMultiConsumerImpl(String t) {
            this.t = t;
        }
        @Override
        public JoinCommaMultiConsumer and(Consumer<String> after) {
            Objects.requireNonNull(after);
            after.accept(t);
            return new JoinCommaMultiConsumerImpl(t);
        }

        @Override
        public <T> T andReturn(Function<String, T> f) {
            return f.apply(t);
        }
    }


    private static final class Partition<T>
            extends AbstractMap<Boolean, T>
            implements Map<Boolean, T> {
        final T forTrue;
        final T forFalse;

        Partition(T forTrue, T forFalse) {
            this.forTrue = forTrue;
            this.forFalse = forFalse;
        }

        @Override
        public Set<Entry<Boolean, T>> entrySet() {
            return new AbstractSet<Entry<Boolean, T>>() {
                @Override
                public Iterator<Entry<Boolean, T>> iterator() {
                    Entry<Boolean, T> falseEntry = new SimpleImmutableEntry<>(false, forFalse);
                    Entry<Boolean, T> trueEntry = new SimpleImmutableEntry<>(true, forTrue);
                    return Arrays.asList(falseEntry, trueEntry).iterator();
                }

                @Override
                public int size() {
                    return 2;
                }
            };
        }
    }

    /**
     * 对流做分组操作
     * 是{@link Collectors#collectingAndThen}的变形用法
     * <pre>{@code
     * IntStream.range(0,10).boxed()
     * .filter(i->i<5)
     * .forEach(consumer1);
     *
     * IntStream.range(0,10).boxed()
     * .filter(i->i>=5)
     * .forEach(consumer2);
     * }</pre>
     * 用这个方法改写后
     * <pre>{@code
     * IntStream.range(0,10).boxed()
     * .collect(tenda.StreamUtil.ifTest(i->i>=5))
     * .thenConsume(consumer1)
     * .elseConsume(consumer2);
     * }</pre>
     * @return {@link JoinCommaThenConsumer}
     */
    public static
    <T>
    Collector<T
            , Partition<Collection<T>>
            , IfPredicateConsumer<T>>
    ifTest(Predicate<T> predicate) {

        Supplier<Partition<Collection<T>>> supplier = () ->
                new Partition<>(new LinkedList<>(),new LinkedList<>());

        BiConsumer<Partition<Collection<T>>, T>
        accumulator = (result, t) ->
                    (predicate.test(t) ? result.forTrue : result.forFalse).add(t);

        BinaryOperator<Partition<Collection<T>>> merger = (left, right) -> {
            left.forTrue.addAll(right.forTrue);
            left.forFalse.addAll(right.forFalse);
            return new Partition<>(left.forTrue, left.forFalse);
        };

        Function<Partition<Collection<T>>, IfPredicateConsumer<T>>
                finisher = IfPredicateConsumerImpl::new;

        return new CollectorImpl<>
                (supplier, accumulator, merger, finisher, Collections.emptySet());

    }

    /** another implementation equivalent to {@link #ifTest}*/
    public static
    <T>
    Collector<T
            , Collection<T>
            , IfPredicateConsumer<T>>
    ifPassed(Predicate<T> p) {
        return null;
    }


    private static
    <T>
    Collector<T
            , Collection<T>
            , IfPredicateConsumer<T>>
    toPredicateConsumer() {
        return null;
    }

    public interface IfPredicateConsumer<T> {
        ElsePredicateConsumer<T> thenConsume(Consumer<T> t);
        ElsePredicateConsumer<T> thenConsumeAll(Consumer<Collection<T>> t);
    }
    public interface ElsePredicateConsumer<T> {
        void elseConsume(Consumer<T> t);
        void elseConsumeAll(Consumer<Collection<T>> t);
    }

    private static class IfPredicateConsumerImpl<T> implements IfPredicateConsumer<T> {
        Collection<T> testedElements;
        Collection<T> failedElements;
        IfPredicateConsumerImpl(Partition<Collection<T>> partition) {
            this.testedElements = partition.forTrue;
            this.failedElements = partition.forFalse;
        }
        @Override
        public ElsePredicateConsumer<T> thenConsume(Consumer<T> thenDO) {
            Objects.requireNonNull(thenDO);
            if(testedElements!=null) testedElements.forEach(thenDO);
            return new ElsePredicateConsumerImpl<>(failedElements);
        }
        @Override
        public ElsePredicateConsumer<T> thenConsumeAll(Consumer<Collection<T>> thenDO) {
            Objects.requireNonNull(thenDO);
            if(testedElements!=null) thenDO.accept(testedElements);
            return new ElsePredicateConsumerImpl<>(failedElements);
        }

    }

    private static class ElsePredicateConsumerImpl<T> implements ElsePredicateConsumer<T> {
        Collection<T> failTests;
        ElsePredicateConsumerImpl(Collection<T> p) {
            this.failTests = p;
        }
        @Override
        public void elseConsume(Consumer<T> elseDO) {
            Objects.requireNonNull(elseDO);
            if(failTests !=null) failTests.forEach(elseDO);
        }

        @Override
        public void elseConsumeAll(Consumer<Collection<T>> t) {
            t.accept(failTests);
        }

    }


}
