package it.unibo.inner;

import it.unibo.inner.api.IterableWithPolicy;
import it.unibo.inner.impl.IterableWithPolicyImpl;

import java.util.List;

import static it.unibo.inner.test.Assertions.assertContentEqualsInOrder;

public class TestIterablePlain {

    private TestIterablePlain() {}

    private static <T> IterableWithPolicy<T> getIterableWithPolicy(T[] elements) {
        return new IterableWithPolicyImpl<>(elements);
    }

    public static void main(final String[] args) {
        String[] test1 = { "pippo", "pluto", "paperino" };

        IterableWithPolicy<String> evenIterable = getIterableWithPolicy(test1);
        assertContentEqualsInOrder(evenIterable, List.of("pippo", "pluto", "paperino"));

        String[] test2 = {};

        IterableWithPolicy<String> emptyIterable = getIterableWithPolicy(test2);
        assertContentEqualsInOrder(emptyIterable, List.of());

        String[] test3 = { "foo" };

        IterableWithPolicy<String> oneIterable = getIterableWithPolicy(test3);
        assertContentEqualsInOrder(oneIterable, List.of("foo"));
    }
}
