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
}
