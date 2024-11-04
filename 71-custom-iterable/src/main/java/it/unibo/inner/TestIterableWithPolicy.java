package it.unibo.inner;

import it.unibo.inner.api.IterableWithPolicy;
import it.unibo.inner.api.Predicate;
import it.unibo.inner.impl.IterableWithPolicyImpl;
import it.unibo.inner.test.api.Product;
import it.unibo.inner.test.impl.ProductImpl;

import java.util.Arrays;
import java.util.List;

import static it.unibo.inner.test.Assertions.assertContentEqualsInOrder;

public class TestIterableWithPolicy {

    private TestIterableWithPolicy() {}

    private static <T> IterableWithPolicy<T> getIterableWithPolicy(T[] elements, Predicate<T> filter) {
        return new IterableWithPolicyImpl<T>(elements, filter);
    }

    private static <T> IterableWithPolicy<T> getIterableWithPolicy(T[] elements) {
        return new IterableWithPolicyImpl<T>(elements);
    }

    public static void main(String[] args) {
        String[] test1 = { "pippo", "pluto", "foo", "bar" };
        Predicate<String> filterPippoPluto = new Predicate<>() {
            public boolean test(String elem) {
                return elem.equals("pippo") || elem.equals("pluto");
            }
        };
        Predicate<String> filterFooBar = new Predicate<>() {
            public boolean test(String elem) {
                return elem.equals("foo") || elem.equals("bar");
            }
        };

        IterableWithPolicy<String> evenIterable = getIterableWithPolicy(test1, filterPippoPluto);
        IterableWithPolicy<String> oddIterable = getIterableWithPolicy(test1, filterFooBar);

        assertContentEqualsInOrder(evenIterable, List.of("pippo", "pluto"));
        assertContentEqualsInOrder(oddIterable, List.of("foo", "bar"));

        Predicate<String> filterOutAll = new Predicate<>() {
            public boolean test(String elem) {
                return false;
            }
        };
        Predicate<String> takeAll = new Predicate<>() {
            public boolean test(String elem) {
                return true;
            }
        };

        IterableWithPolicy<String> emptyIterable = getIterableWithPolicy(test1, filterOutAll);
        IterableWithPolicy<String> allIterable = getIterableWithPolicy(test1, takeAll);

        assertContentEqualsInOrder(emptyIterable, List.of());
        assertContentEqualsInOrder(allIterable, List.of("pippo", "pluto", "foo", "bar"));

        IterableWithPolicy<String> switchPolicy = getIterableWithPolicy(test1);

        // By default, if no Predicate is given, the iterator should iterate all the elements
        assertContentEqualsInOrder(switchPolicy, List.of("pippo", "pluto", "foo", "bar"));

        switchPolicy.setIterationPolicy(filterOutAll);

        // After setting a new policy, the iterator should return no elements
        assertContentEqualsInOrder(switchPolicy, List.of());


        // Test with products

        Product prod1 = new ProductImpl("Prod 1", 0);
        Product prod2 = new ProductImpl("Prod 2", 100);
        Product prod3 = new ProductImpl("Prod 3", 20);
        Product prod4 = new ProductImpl("Prod 4", 35);
        Product prod5 = new ProductImpl("Prod 5", 450);

        Product[] productsTest = { prod1, prod2, prod3, prod4, prod5 };
        Predicate<Product> filterAllAvailable = new Predicate<Product>() {
            public boolean test(Product elem) {
                return elem.getQuantity() > 0;
            }
        };
        Predicate<Product> filterGraterThanFifty = new Predicate<Product>() {
            public boolean test(Product elem) {
                return elem.getQuantity() > 50;
            }
        };
        Predicate<Product> takeOnlyProductOne = new Predicate<Product>() {
            public boolean test(Product elem) {
                return elem.getName().equals("Prod 1");
            }
        };

        IterableWithPolicy<Product> availableProducts = getIterableWithPolicy(productsTest, filterAllAvailable);
        assertContentEqualsInOrder(availableProducts, Arrays.asList(prod2, prod3, prod4, prod5));

        IterableWithPolicy<Product> expensiveProducts = getIterableWithPolicy(productsTest, filterGraterThanFifty);
        assertContentEqualsInOrder(expensiveProducts, Arrays.asList(prod2, prod5));

        IterableWithPolicy<Product> onlyProductOne = getIterableWithPolicy(productsTest, takeOnlyProductOne);
        assertContentEqualsInOrder(onlyProductOne, List.of(prod1));
    }
}
