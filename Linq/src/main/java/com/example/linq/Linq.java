package com.example.linq;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Linq {

    /**
     * Devuelve el primer elemento de la secuencia que satisface una condición especificada
     * o un valor predeterminado (null en este caso) si no se encuentra dicho elemento.
     *
     * @param <T>       El tipo de los elementos de la fuente.
     * @param source    La colección Iterable<T> sobre la que se opera.
     * @param predicate Una función para probar cada elemento en busca de una condición.
     * @return El primer elemento de la secuencia que pasa la prueba en la función de predicado especificada,
     * o null si no se encuentra dicho elemento o la fuente es null.
     */
    public static <T> T firstOrDefault(Iterable<T> source, Predicate<T> predicate) {
        if (!any(source))
            return null;

        return StreamSupport.stream(source.spliterator(), false)
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * Devuelve el primer elemento de la secuencia o un valor predeterminado (null en este caso)
     * si la secuencia no contiene elementos.
     *
     * @param <T>    El tipo de los elementos de la fuente.
     * @param source La colección Iterable<T> sobre la que se opera.
     * @return El primer elemento de la secuencia de entrada, o null si la secuencia está vacía o la fuente es null.
     */
    public static <T> T firstOrDefault(Iterable<T> source) {
        if (!any(source))
            return null;
        return StreamSupport.stream(source.spliterator(), false)
                .findFirst()
                .orElse(null);
    }

    /**
     * Determina si algún elemento de una secuencia satisface una condición.
     *
     * @param <T>       El tipo de los elementos de la fuente.
     * @param source    La colección Iterable<T> sobre la que se opera.
     * @param predicate Una función para probar cada elemento en busca de una condición.
     * @return true si algún elemento de la secuencia de origen pasa la prueba en la función de predicado especificada;
     * de lo contrario, false. Devuelve false si la fuente es null.
     */
    public static <T> boolean any(Iterable<T> source, Predicate<T> predicate) {
        if (!any(source))
            return false;

        return StreamSupport.stream(source.spliterator(), false)
                .anyMatch(predicate);
    }

    /**
     * Determina si una secuencia contiene algún elemento.
     *
     * @param <T>    El tipo de los elementos de la fuente.
     * @param source La colección Iterable<T> sobre la que se opera.
     * @return true si la secuencia de origen contiene algún elemento; de lo contrario, false.
     * Devuelve false si la fuente es null.
     */
    public static <T> boolean any(Iterable<T> source) {
        if (source == null) {
            return false;
        }
        // Para un Iterable, la forma más directa de saber si tiene al menos un elemento
        // es intentar obtener su iterador y ver si tiene un siguiente elemento.
        // StreamSupport.stream(...).findAny().isPresent() también funcionaría pero es un poco más pesado.
        Iterator<T> iterator = source.iterator();
        return iterator.hasNext();
    }

    /**
     * Filtra una secuencia de valores basándose en un predicado.
     * Cada elemento para el que el predicado devuelve true se incluye en la lista resultante.
     *
     * @param <T> El tipo de los elementos de la fuente.
     * @param source La colección Iterable<T> a filtrar.
     * @param predicate Una función para probar cada elemento en busca de una condición.
     * @return Un nuevo List<T> que contiene elementos de la secuencia de entrada que satisfacen la condición.
     *         Devuelve una lista vacía si la fuente es null o no hay elementos que coincidan.
     */
    public static <T> List<T> where(Iterable<T> source, Predicate<T> predicate) {
        if (!any(source) || predicate == null)
            return Collections.emptyList();


        return StreamSupport.stream(source.spliterator(), false)
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve un número especificado de elementos contiguos desde el inicio de una secuencia.
     *
     * @param <T> El tipo de los elementos de la fuente.
     * @param source La colección Iterable<T> de la que se devolverán los elementos.
     * @param count El número de elementos a devolver.
     * @return Un nuevo List<T> que contiene el número especificado de elementos desde el inicio de la secuencia de entrada.
     *         Devuelve una lista vacía si la fuente es null, count es menor o igual a 0, o la fuente tiene menos elementos que count.
     */
    public static <T> List<T> take(Iterable<T> source, int count) {
        if (!any(source) || count <= 0) {
            return Collections.emptyList();
        }
        return StreamSupport.stream(source.spliterator(), false)
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Omite un número especificado de elementos en una secuencia y luego devuelve los elementos restantes.
     *
     * @param <T> El tipo de los elementos de la fuente.
     * @param source La colección Iterable<T> de la que se devolverán los elementos.
     * @param count El número de elementos a omitir antes de devolver los elementos restantes.
     * @return Un nuevo List<T> que contiene los elementos que quedan después de omitir el número especificado
     *         de elementos en la secuencia de entrada.
     *         Devuelve una lista vacía si la fuente es null. Si count es menor o igual a 0, devuelve todos los elementos.
     *         Si count es mayor que el número de elementos, devuelve una lista vacía.
     */
    public static <T> List<T> skip(Iterable<T> source, int count) {
        if (!any(source)) {
            return Collections.emptyList();
        }
        if (count <= 0) {
            // Si count es 0 o negativo, no se omite nada, se devuelven todos los elementos.
            return StreamSupport.stream(source.spliterator(), false)
                    .collect(Collectors.toList());
        }
        return StreamSupport.stream(source.spliterator(), false)
                .skip(count)
                .collect(Collectors.toList());
    }

    /*
    // Ejemplo de uso:
    public static void main(String[] args) {

//        List<Integer> numbers = new ArrayList<>();
//        numbers.add(1); numbers.add(2); // etc.
//        numbers.add(3); numbers.add(4); // etc.
//        numbers.add(5); numbers.add(6); // etc.
//        numbers.add(7); numbers.add(8); // etc.
//        numbers.add(9); numbers.add(10); // etc.

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> emptyList = Collections.emptyList();
        List<String> strings = List.of("apple", "banana", "cherry");

        // Ejemplos de firstOrDefault
        Integer firstEven = Linq.firstOrDefault(numbers, n -> n % 2 == 0);
        System.out.println("Primer número par: " + firstEven); // Salida: Primer número par: 2

        Integer firstGreaterThanTen = Linq.firstOrDefault(numbers, n -> n > 10);
        System.out.println("Primer número mayor que 10: " + firstGreaterThanTen); // Salida: Primer número mayor que 10: null

        Integer firstInEmpty = Linq.firstOrDefault(emptyList);
        System.out.println("Primer elemento en lista vacía: " + firstInEmpty); // Salida: Primer elemento en lista vacía: null

        String firstString = Linq.firstOrDefault(strings);
        System.out.println("Primera cadena: " + firstString); // Salida: Primera cadena: apple

        // Ejemplos de Any
        boolean hasEven = Linq.any(numbers, n -> n % 2 == 0);
        System.out.println("¿Hay algún número par?: " + hasEven); // Salida: ¿Hay algún número par?: true

        boolean hasGreaterThanTen = Linq.any(numbers, n -> n > 10);
        System.out.println("¿Hay algún número mayor que 10?: " + hasGreaterThanTen); // Salida: ¿Hay algún número mayor que 10?: false

        boolean anyInEmpty = Linq.any(emptyList);
        System.out.println("¿Hay algún elemento en la lista vacía?: " + anyInEmpty); // Salida: ¿Hay algún elemento en la lista vacía?: false

        boolean anyString = Linq.any(strings);
        System.out.println("¿Hay alguna cadena?: " + anyString); // Salida: ¿Hay alguna cadena?: true

        // Prueba con fuente null
        System.out.println("firstOrDefault con null: " + Linq.firstOrDefault(null, x -> true)); // Salida: firstOrDefault con null: null
        System.out.println("any con null: " + Linq.any(null, x -> true)); // Salida: any con null: false

        // --- Ejemplos de where ---
        List<Integer> evenNumbers = Linq.where(numbers, n -> n % 2 == 0);
        System.out.println("Números pares: " + evenNumbers); // Salida: Números pares: [2, 4, 6, 8, 10]

        // --- Ejemplos de take ---
        System.out.println("Take 3 de numbers: " + Linq.take(numbers, 3)); // [1, 2, 3]
        System.out.println("Take 0 de numbers: " + Linq.take(numbers, 0)); // []
        System.out.println("Take 100 de numbers: " + Linq.take(numbers, 100)); // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        System.out.println("Take 2 de strings: " + Linq.take(strings, 2));   // [a, b]
        System.out.println("Take 5 de emptyList: " + Linq.take(emptyList, 5)); // []
        System.out.println("Take 3 de null: " + Linq.take(null, 3));        // []

        // --- Ejemplos de skip ---
        System.out.println("Skip 3 de numbers: " + Linq.skip(numbers, 3)); // [4, 5, 6, 7, 8, 9, 10]
        System.out.println("Skip 0 de numbers: " + Linq.skip(numbers, 0)); // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        System.out.println("Skip -1 de numbers: " + Linq.skip(numbers, -1)); // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        System.out.println("Skip 100 de numbers: " + Linq.skip(numbers, 100)); // []
        System.out.println("Skip 2 de strings: " + Linq.skip(strings, 2));   // [c, d, e]
        System.out.println("Skip 1 de emptyList: " + Linq.skip(emptyList, 1)); // []
        System.out.println("Skip 3 de null: " + Linq.skip(null, 3));        // []
    }*/
}
