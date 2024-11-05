package it.unibo.functional;

import it.unibo.functional.api.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A special utility class with methods that transform collections using {@link Function}s provided as parameters.
 */
public final class Transformers {

    private Transformers() { }

    /**
     * A function that applies a transformation to each element of an {@link Iterable}, obtaining multiple elements,
     * and then builds a "flat" list of these transformed elements.
     * For instance, {@code [1, 2, 3, 4, 5]} could use {@code flattenTransform} to transform only the odd numbers into
     * squares, passing a function that returns an empty {@link List} for even numbers and a List with the square of
     * the input otherwise, obtaining as output {@code [1, 9, 25]}.
     *
     * @param base the elements on which to operate
     * @param transformer the {@link Function} to apply to each element. It must transform the elements into a
     *     (possibly empty) collection of other elements.
     * @return A "flattened" list of the produced elements
     * @param <I> input elements type
     * @param <O> output elements type
     */
    public static <I, O> List<O> flattenTransform(
        final Iterable<? extends I> base,
        final Function<I, ? extends Collection<? extends O>> transformer
    ) {
        final var result = new ArrayList<O>();
        for (final I input : Objects.requireNonNull(base, "The base iterable cannot be null")) {
            result.addAll(transformer.call(input));
        }
        return result;
    }

    /**
     * A function that applies a transformation to each element of an {@link Iterable},
     * returning a list of these transformed elements.
     * For instance, {@code [1, 2, 3, 4, 5]} could use {@code transform} to produce a list of squares by passing
     * a function that computes the square of the input, thus obtaining {@code [1, 4, 9, 16, 25]}.
     * <b>NOTE:</b> this function is a special flattenTransform whose function always return a list with a single
     * element
     *
     * @param base the elements on which to operate
     * @param transformer the {@link Function} to apply to each element.
     * @return A transformed list where each input element is replaced with the produced elements
     * @param <I> input elements type
     * @param <O> output elements type
     */
    public static <I, O> List<O> transform(final Iterable<I> base, final Function<I, O> transformer) {
        return flattenTransform(base, new Function<>() {
            @Override
            public List<? extends O> call(I input) {
                return List.of(transformer.call(input));
            }
        });
    }

    /**
     * A function that takes an iterable of collections, and returns a flatten list of the elements of the inner
     * collections.
     * For instance, {@code [[1], [2, 3], [4, 5], []]} could use {@code flatten} to produce a flat list, thus obtaining
     * {@code [1, 2, 3, 4, 5]}.
     * <b>NOTE:</b> this function is a special flattenTransform whose input is an iterable of collections,
     * and whose function simply returns each collection (identity).
     *
     * @param base the collections on which to operate
     * @return A flattened list with the elements of each collection in the input
     * @param <I> type of the collection elements
     */
    public static <I> List<? extends I> flatten(final Iterable<? extends Collection<? extends I>> base) {
        return flattenTransform(base, Function.identity());
    }

    /**
     * A function that applies a test to each element of an {@link Iterable}, returning a list containing only the
     * elements that pass the test.
     * For instance, {@code [1, 2, 3, 4, 5]} could use {@code select} to filter only the odd numbers, thus obtaining
     * {@code [1, 3, 5]}.
     * <b>NOTE:</b> this function is a special flattenTransform whose function returns a list with a single element if
     * the element passes the test, and an empty list otherwise.
     *
     * @param base the elements on which to operate
     * @param test the {@link Function} to use to test whether the elements should be selected.
     * @return A list containing only the elements that passed the test
     * @param <I> elements type
     */
    public static <I> List<I> select(final Iterable<I> base, final Function<I, Boolean> test) {
        return flattenTransform(base, new Function<>() {
            @Override
            public List<? extends I> call(I input) {
                return test.call(input) ? List.of(input) : List.of();
            }      
        });
    }

    /**
     * A function that applies a test to each element of an {@link Iterable}, returning a list containing only the
     * elements that do not pass the test.
     * For instance, {@code [1, 2, 3, 4, 5]} could use {@code select} to reject all the even numbers, thus obtaining
     * {@code [1, 3, 5]}.
     * <b>NOTE:</b> this function is a special select whose test function return value is negated.
     *
     * @param base the elements on which to operate
     * @param test the {@link Function} to use to test whether the elements should be discarded.
     * @return A list containing only the elements that passed the test
     * @param <I> elements type
     */
    public static <I> List<I> reject(final Iterable<I> base, final Function<I, Boolean> test) {
        return select(base, new Function<>() {
            @Override
            public Boolean call(I input) {
                return !test.call(input);
            }
        });
    }
}
