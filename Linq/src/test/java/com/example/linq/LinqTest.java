package com.example.linq;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class LinqTest {
    
    private List<Integer> numbers;
    private List<String> strings;
    private List<Integer> emptyList;
    private List<Integer> singleElementList;
    
    @Before
    public void setUp() {
        numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        strings = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");
        emptyList = Collections.emptyList();
        singleElementList = Collections.singletonList(42);
    }
    
    // Pruebas para firstOrDefault con predicado
    @Test
    public void testFirstOrDefault_WithPredicate_FindsFirstMatchingElement() {
        Integer result = Linq.firstOrDefault(numbers, n -> n > 5);
        assertEquals(Integer.valueOf(6), result);
    }
    
    @Test
    public void testFirstOrDefault_WithPredicate_ReturnsNullWhenNoMatch() {
        Integer result = Linq.firstOrDefault(numbers, n -> n > 100);
        assertNull(result);
    }
    
    @Test
    public void testFirstOrDefault_WithPredicate_WorksWithStrings() {
        String result = Linq.firstOrDefault(strings, s -> s.startsWith("b"));
        assertEquals("banana", result);
    }
    
    @Test
    public void testFirstOrDefault_WithPredicate_ReturnsNullForEmptyList() {
        Integer result = Linq.firstOrDefault(emptyList, n -> n > 0);
        assertNull(result);
    }
    
    @Test
    public void testFirstOrDefault_WithPredicate_ReturnsNullForNullSource() {
        Integer result = Linq.firstOrDefault(null, n -> n > 0);
        assertNull(result);
    }
    
    // Pruebas para firstOrDefault sin predicado
    @Test
    public void testFirstOrDefault_ReturnsFirstElement() {
        Integer result = Linq.firstOrDefault(numbers);
        assertEquals(Integer.valueOf(1), result);
    }
    
    @Test
    public void testFirstOrDefault_ReturnsNullForEmptyList() {
        Integer result = Linq.firstOrDefault(emptyList);
        assertNull(result);
    }
    
    @Test
    public void testFirstOrDefault_WorksWithSingleElementList() {
        Integer result = Linq.firstOrDefault(singleElementList);
        assertEquals(Integer.valueOf(42), result);
    }
    
    // Pruebas para any con predicado
    @Test
    public void testAny_WithPredicate_ReturnsTrueWhenMatchExists() {
        boolean result = Linq.any(numbers, n -> n % 2 == 0);
        assertTrue(result);
    }
    
    @Test
    public void testAny_WithPredicate_ReturnsFalseWhenNoMatch() {
        boolean result = Linq.any(numbers, n -> n > 100);
        assertFalse(result);
    }
    
    @Test
    public void testAny_WithPredicate_ReturnsFalseForEmptyList() {
        boolean result = Linq.any(emptyList, n -> true);
        assertFalse(result);
    }
    
    @Test
    public void testAny_WithPredicate_ReturnsFalseForNullSource() {
        boolean result = Linq.any(null, n -> true);
        assertFalse(result);
    }
    
    // Pruebas para any sin predicado
    @Test
    public void testAny_ReturnsTrueForNonEmptyList() {
        boolean result = Linq.any(numbers);
        assertTrue(result);
    }
    
    @Test
    public void testAny_ReturnsFalseForEmptyList() {
        boolean result = Linq.any(emptyList);
        assertFalse(result);
    }
    
    @Test
    public void testAny_ReturnsFalseForNullSource() {
        boolean result = Linq.any((Iterable<?>) null);
        assertFalse(result);
    }
    
    // Pruebas para where
    @Test
    public void testWhere_ReturnsFilteredList() {
        List<Integer> result = Linq.where(numbers, n -> n % 2 == 0);
        assertEquals(Arrays.asList(2, 4, 6, 8, 10), result);
    }
    
    @Test
    public void testWhere_ReturnsEmptyListWhenNoMatches() {
        List<Integer> result = Linq.where(numbers, n -> n > 100);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testWhere_ReturnsEmptyListForEmptySource() {
        List<Integer> result = Linq.where(emptyList, n -> true);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testWhere_WorksWithStrings() {
        List<String> result = Linq.where(strings, s -> s.length() > 5);
        assertEquals(Arrays.asList("banana", "cherry", "elderberry"), result);
    }
    
    // Pruebas para take
    @Test
    public void testTake_ReturnsFirstNElements() {
        List<Integer> result = Linq.take(numbers, 3);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }
    
    @Test
    public void testTake_ReturnsAllElementsWhenCountExceedsSize() {
        List<Integer> result = Linq.take(numbers, 100);
        assertEquals(numbers, result);
    }
    
    @Test
    public void testTake_ReturnsEmptyListWhenCountIsZero() {
        List<Integer> result = Linq.take(numbers, 0);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testTake_ReturnsEmptyListForNegativeCount() {
        List<Integer> result = Linq.take(numbers, -1);
        assertTrue(result.isEmpty());
    }
    
    // Pruebas para skip
    @Test
    public void testSkip_OmitsFirstNElements() {
        List<Integer> result = Linq.skip(numbers, 3);
        assertEquals(Arrays.asList(4, 5, 6, 7, 8, 9, 10), result);
    }
    
    @Test
    public void testSkip_ReturnsEmptyListWhenSkippingAllElements() {
        List<Integer> result = Linq.skip(numbers, numbers.size());
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testSkip_ReturnsAllElementsWhenSkippingZero() {
        List<Integer> result = Linq.skip(numbers, 0);
        assertEquals(numbers, result);
    }
    
    @Test
    public void testSkip_ReturnsAllElementsWhenSkippingNegative() {
        List<Integer> result = Linq.skip(numbers, -1);
        assertEquals(numbers, result);
    }
    
    @Test
    public void testSkip_ReturnsEmptyListWhenSkippingMoreThanSize() {
        List<Integer> result = Linq.skip(numbers, 100);
        assertTrue(result.isEmpty());
    }

    // Pruebas para select
    @Test
    public void testSelect_TransformsElements() {
        List<String> result = Linq.select(numbers, n -> "N:" + n);
        assertEquals(Arrays.asList("N:1", "N:2", "N:3", "N:4", "N:5", "N:6", "N:7", "N:8", "N:9", "N:10"), result);
    }

    @Test
    public void testSelect_ReturnsEmptyForEmptyOrNullSource() {
        assertTrue(Linq.select(Collections.<Integer>emptyList(), n -> n).isEmpty());
        assertTrue(Linq.select((Iterable<Integer>) null, n -> n).isEmpty());
    }

    // Pruebas para selectMany
    @Test
    public void testSelectMany_FlattensSequences() {
        List<List<Integer>> nested = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3), Arrays.asList());
        List<Integer> flat = Linq.selectMany(nested, x -> x);
        assertEquals(Arrays.asList(1, 2, 3), flat);
    }

    @Test
    public void testSelectMany_ReturnsEmptyForEmptyOrNullSource() {
        assertTrue(Linq.selectMany(Collections.<List<Integer>>emptyList(), x -> x).isEmpty());
        assertTrue(Linq.selectMany((Iterable<List<Integer>>) null, x -> x).isEmpty());
    }

    // Pruebas para findIndex
    @Test
    public void testFindIndex_FindsFirstMatch() {
        int index = Linq.findIndex(strings, s -> s.startsWith("b"));
        assertEquals(1, index);
    }

    @Test
    public void testFindIndex_ReturnsMinusOneWhenNoMatchOrInvalidInput() {
        assertEquals(-1, Linq.findIndex(strings, s -> s.length() > 20));
        assertEquals(-1, Linq.findIndex(Collections.<String>emptyList(), s -> true));
        assertEquals(-1, Linq.findIndex((Iterable<String>) null, s -> true));
        assertEquals(-1, Linq.findIndex(strings, null));
    }

    // Pruebas para distinct
    @Test
    public void testDistinct_RemovesDuplicatesPreservingOrder() {
        List<Integer> withDupes = Arrays.asList(1, 2, 2, 3, 1, 4, 3);
        List<Integer> uniq = Linq.distinct(withDupes);
        assertEquals(Arrays.asList(1, 2, 3, 4), uniq);
    }

    @Test
    public void testDistinct_ReturnsEmptyForEmptyOrNullSource() {
        assertTrue(Linq.distinct(Collections.<Integer>emptyList()).isEmpty());
        assertTrue(Linq.distinct((Iterable<Integer>) null).isEmpty());
    }

    // Pruebas para count
    @Test
    public void testCount_TotalElements() {
        assertEquals(numbers.size(), Linq.count(numbers));
        assertEquals(0, Linq.count(Collections.<Integer>emptyList()));
        assertEquals(0, Linq.count((Iterable<Integer>) null));
    }

    @Test
    public void testCount_WithPredicate() {
        assertEquals(5, Linq.count(numbers, n -> n % 2 != 0));
        assertEquals(0, Linq.count(numbers, n -> n > 100));
        assertEquals(0, Linq.count(Collections.<Integer>emptyList(), n -> true));
        assertEquals(0, Linq.count(numbers, null));
    }
    
    // Pruebas para sum
    @Test
    public void testSum_WithNumbers() {
        double result = Linq.sum(numbers);
        assertEquals(55.0, result, 0.001); // 1+2+...+10 = 55
    }
    
    @Test
    public void testSum_WithSelector() {
        double result = Linq.sum(numbers, n -> n * 2);
        assertEquals(110.0, result, 0.001); // (1+2+...+10)*2 = 110
    }
    
    @Test
    public void testSum_WithStrings() {
        List<String> strNumbers = Arrays.asList("1", "2.5", "3.75");
        double result = Linq.sum(strNumbers);
        assertEquals(7.25, result, 0.001);
    }
    
    @Test
    public void testSum_WithCurrency() {
        List<String> prices = Arrays.asList("$1.99", "2,500.50", "3.75");
        double result = Linq.sum(prices);
        assertEquals(2506.24, result, 0.001);
    }
    
    @Test
    public void testSum_EmptyOrNull() {
        assertEquals(0.0, Linq.sum(emptyList), 0.001);
        assertEquals(0.0, Linq.sum((Iterable<?>)null), 0.001);
    }
    
    // Pruebas para min y max
    @Test
    public void testMinMax_WithNumbers() {
        assertEquals(1.0, Linq.min(numbers), 0.001);
        assertEquals(10.0, Linq.max(numbers), 0.001);
    }
    
    @Test
    public void testMinMax_WithSelector() {
        List<String> strings = Arrays.asList("a", "bb", "ccc", "d");
        assertEquals(1.0, Linq.min(strings, String::length), 0.001);
        assertEquals(3.0, Linq.max(strings, String::length), 0.001);
    }
    
    @Test
    public void testMinMax_EmptyOrNull() {
        assertEquals(0.0, Linq.min(emptyList), 0.001);
        assertEquals(0.0, Linq.max(emptyList), 0.001);
        assertEquals(0.0, Linq.min(null), 0.001);
        assertEquals(0.0, Linq.max(null), 0.001);
    }
    
    // Pruebas para average
    @Test
    public void testAverage_WithNumbers() {
        double result = Linq.average(numbers);
        assertEquals(5.5, result, 0.001); // (1+2+...+10)/10 = 5.5
    }
    
    @Test
    public void testAverage_WithSelector() {
        double result = Linq.average(numbers, n -> n * 2);
        assertEquals(11.0, result, 0.001); // (2+4+...+20)/10 = 11
    }
    
    @Test
    public void testAverage_EmptyOrNull() {
        assertEquals(0.0, Linq.average(emptyList), 0.001);
        assertEquals(0.0, Linq.average((Iterable<?>)null), 0.001);
    }
    
    // Pruebas para orderBy y orderByDescending
    @Test
    public void testOrderBy_Ascending() {
        List<Integer> unordered = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6);
        List<Integer> ordered = Linq.orderBy(unordered);
        assertEquals(Arrays.asList(1, 1, 2, 3, 4, 5, 6, 9), ordered);
    }
    
    @Test
    public void testOrderBy_WithKeySelector() {
        List<String> words = Arrays.asList("banana", "apple", "cherry");
        List<String> ordered = Linq.orderBy(words, s -> s);
        assertEquals(Arrays.asList("apple", "banana", "cherry"), ordered);
    }
    
    @Test
    public void testOrderByDescending_WithKeySelector() {
        List<String> words = Arrays.asList("banana", "apple", "cherry");
        List<String> ordered = Linq.orderByDescending(words, s -> s);
        assertEquals(Arrays.asList("cherry", "banana", "apple"), ordered);
    }
    
    @Test
    public void testOrderBy_EmptyOrNull() {
        assertTrue(Linq.orderBy(emptyList).isEmpty());
        assertTrue(Linq.orderBy(null).isEmpty());
        assertTrue(Linq.orderByDescending(emptyList).isEmpty());
        assertTrue(Linq.orderByDescending(null).isEmpty());
    }
    
    // Pruebas para all
    @Test
    public void testAll_ReturnsTrueWhenAllMatch() {
        assertTrue(Linq.all(numbers, n -> n > 0));
    }
    
    @Test
    public void testAll_ReturnsFalseWhenAnyFails() {
        assertFalse(Linq.all(numbers, n -> n < 5));
    }
    
    @Test
    public void testAll_EmptyListReturnsTrue() {
        assertTrue(Linq.all(emptyList, n -> false));
    }
    
    @Test
    public void testAll_NullPredicateReturnsFalse() {
        assertFalse(Linq.all(numbers, null));
    }
    
    @Test
    public void testAll_NullSourceReturnsTrue() {
        assertTrue(Linq.all(null, n -> true));
    }
}
