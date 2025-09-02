package com.example.linq;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
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
     * Determina si todos los elementos de una secuencia satisfacen una condición.
     *
     * @param <T>       El tipo de los elementos de la fuente.
     * @param source    La colección Iterable<T> sobre la que se opera.
     * @param predicate Una función para probar cada elemento en busca de una condición.
     * @return true si cada elemento de la secuencia de origen pasa la prueba en el predicado especificado,
     * o si la secuencia está vacía; de lo contrario, false. Devuelve true si la fuente es null.
     */
    public static <T> boolean all(Iterable<T> source, Predicate<T> predicate) {
        if (source == null) {
            return true;
        }
        if (predicate == null) {
            return false;
        }
        for (T item : source) {
            if (!predicate.test(item)) {
                return false;
            }
        }
        return true;
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
     * Proyecta cada elemento de una secuencia en una nueva forma.
     * Equivalente a LINQ Select. Si la fuente es null/está vacía o el selector es null,
     * devuelve una lista vacía.
     *
     * @param <T>       Tipo de los elementos de entrada.
     * @param <R>       Tipo de los elementos resultantes.
     * @param source    La colección Iterable<T> a transformar.
     * @param selector  Función que transforma cada elemento T en un valor de tipo R.
     * @return          Un nuevo List<R> con los resultados de aplicar el selector a cada elemento.
     */
    public static <T, R> List<R> select(Iterable<T> source, Function<T, R> selector) {
        if (!any(source) || selector == null) {
            return Collections.emptyList();
        }

        return StreamSupport.stream(source.spliterator(), false)
                .map(selector)
                .collect(Collectors.toList());
    }

    /**
     * Proyecta cada elemento de una secuencia a una secuencia (Iterable) y aplana las secuencias resultantes en una sola lista.
     * Equivalente a LINQ SelectMany.
     *
     * @param <T>       Tipo de los elementos de entrada.
     * @param <R>       Tipo de los elementos resultantes.
     * @param source    La colección Iterable<T> a transformar.
     * @param selector  Función que transforma cada elemento T en un Iterable<R>.
     * @return          Un nuevo List<R> con todos los elementos aplanados.
     */
    public static <T, R> List<R> selectMany(Iterable<T> source, Function<T, ? extends Iterable<R>> selector) {
        if (!any(source) || selector == null) {
            return Collections.emptyList();
        }

        return StreamSupport.stream(source.spliterator(), false)
                .map(selector)
                .filter(Objects::nonNull)
                .flatMap(iterable -> StreamSupport.stream(iterable.spliterator(), false))
                .collect(Collectors.toList());
    }

    /**
     * Devuelve el índice del primer elemento que satisface el predicado.
     *
     * @param <T>       Tipo de los elementos de la fuente.
     * @param source    La colección Iterable<T> a evaluar.
     * @param predicate Predicado que define la condición a cumplir.
     * @return          El índice (basado en 0) del primer elemento que cumple la condición; -1 si no se encuentra,
     *                  o si la fuente o el predicado son null.
     */
    public static <T> int findIndex(Iterable<T> source, Predicate<T> predicate) {
        if (!any(source) || predicate == null) {
            return -1;
        }

        int index = 0;
        for (T item : source) {
            if (predicate.test(item)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Devuelve una lista con elementos únicos de la secuencia de entrada, preservando el orden de aparición.
     *
     * @param <T>    Tipo de los elementos de la fuente.
     * @param source La colección Iterable<T> a evaluar.
     * @return       Un nuevo List<T> con elementos distintos; lista vacía si la fuente es null o no tiene elementos.
     */
    public static <T> List<T> distinct(Iterable<T> source) {
        if (!any(source)) {
            return Collections.emptyList();
        }

        return StreamSupport.stream(source.spliterator(), false)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Cuenta el número total de elementos en la secuencia.
     *
     * @param <T>    Tipo de los elementos de la fuente.
     * @param source La colección Iterable<T> a evaluar.
     * @return       La cantidad de elementos; 0 si la fuente es null o está vacía.
     */
    public static <T> int count(Iterable<T> source) {
        if (!any(source)) {
            return 0;
        }
        return (int) StreamSupport.stream(source.spliterator(), false).count();
    }

    /**
     * Cuenta los elementos que cumplen con el predicado.
     *
     * @param <T>       Tipo de los elementos de la fuente.
     * @param source    La colección Iterable<T> a evaluar.
     * @param predicate Predicado que define la condición a cumplir.
     * @return          La cantidad de elementos que cumplen; 0 si la fuente es null/está vacía o el predicado es null.
     */
    public static <T> int count(Iterable<T> source, Predicate<T> predicate) {
        if (!any(source) || predicate == null) {
            return 0;
        }
        return (int) StreamSupport.stream(source.spliterator(), false)
                .filter(predicate)
                .count();
    }

    /**
     * Intenta convertir un objeto a double de forma segura.
     * 
     * @param obj El objeto a convertir.
     * @return El valor numérico como double, o 0.0 si no se puede convertir.
     */
    private static double safeToDouble(Object obj) {
        if (obj == null) {
            return 0.0;
        }
        try {
            if (obj instanceof Number) {
                return ((Number) obj).doubleValue();
            } else if (obj instanceof String) {
                return Double.parseDouble(((String) obj).trim()
                    .replace("$", "")
                    .replace(",", ""));
            } else if (obj instanceof Boolean) {
                return (Boolean) obj ? 1.0 : 0.0;
            } else if (obj instanceof Character) {
                return Character.getNumericValue((Character) obj);
            } else {
                // Intentar convertir el string
                return Double.parseDouble(String.valueOf(obj)
                    .trim()
                    .replace("$", "")
                    .replace(",", ""));
            }
        } catch (NumberFormatException e) {
            return 0.0; // Devuelve 0 si no se puede convertir
        }
    }

    /**
     * Calcula la suma de una secuencia de valores. Intenta convertir cada elemento a número.
     * 
     * @param source La colección de elementos a sumar.
     * @return La suma de los valores convertidos a double; 0 si la fuente es null o está vacía.
     */
    public static <T> double sum(Iterable<T> source) {
        if (!any(source)) {
            return 0.0;
        }
        return StreamSupport.stream(source.spliterator(), false)
                .mapToDouble(Linq::safeToDouble)
                .sum();
    }

    /**
     * Calcula la suma de la secuencia de valores que se obtienen al aplicar
     * una función de transformación a cada elemento de la secuencia de entrada.
     * Intenta convertir cada valor resultante a número.
     *
     * @param <T>     El tipo de los elementos de la fuente.
     * @param source  La colección de elementos.
     * @param selector Función de transformación que se va a aplicar a cada elemento.
     * @return La suma de los valores proyectados; 0 si la fuente es null/está vacía o el selector es null.
     */
    public static <T> double sum(Iterable<T> source, Function<T, ?> selector) {
        if (!any(source) || selector == null) {
            return 0.0;
        }
        return StreamSupport.stream(source.spliterator(), false)
                .map(selector)
                .mapToDouble(Linq::safeToDouble)
                .sum();
    }

    /**
     * Encuentra el valor mínimo en una secuencia de valores.
     * Los valores se convierten a double para la comparación.
     *
     * @param source La secuencia de valores.
     * @return El valor mínimo o 0.0 si la secuencia está vacía o es null.
     */
    public static <T> double min(Iterable<T> source) {
        if (!any(source)) {
            return 0.0;
        }
        return StreamSupport.stream(source.spliterator(), false)
                .mapToDouble(Linq::safeToDouble)
                .min()
                .orElse(0.0);
    }

    /**
     * Encuentra el valor mínimo en una secuencia transformando cada elemento con un selector.
     *
     * @param <T>     El tipo de los elementos de la fuente.
     * @param source  La secuencia de valores.
     * @param selector Función de transformación que se aplica a cada elemento.
     * @return El valor mínimo o 0.0 si la secuencia está vacía o el selector es null.
     */
    public static <T> double min(Iterable<T> source, Function<T, ?> selector) {
        if (!any(source) || selector == null) {
            return 0.0;
        }
        return StreamSupport.stream(source.spliterator(), false)
                .map(selector)
                .mapToDouble(Linq::safeToDouble)
                .min()
                .orElse(0.0);
    }

    /**
     * Encuentra el valor máximo en una secuencia de valores.
     * Los valores se convierten a double para la comparación.
     *
     * @param source La secuencia de valores.
     * @return El valor máximo o 0.0 si la secuencia está vacía o es null.
     */
    public static <T> double max(Iterable<T> source) {
        if (!any(source)) {
            return 0.0;
        }
        return StreamSupport.stream(source.spliterator(), false)
                .mapToDouble(Linq::safeToDouble)
                .max()
                .orElse(0.0);
    }

    /**
     * Encuentra el valor máximo en una secuencia transformando cada elemento con un selector.
     *
     * @param <T>     El tipo de los elementos de la fuente.
     * @param source  La secuencia de valores.
     * @param selector Función de transformación que se aplica a cada elemento.
     * @return El valor máximo o 0.0 si la secuencia está vacía o el selector es null.
     */
    public static <T> double max(Iterable<T> source, Function<T, ?> selector) {
        if (!any(source) || selector == null) {
            return 0.0;
        }
        return StreamSupport.stream(source.spliterator(), false)
                .map(selector)
                .mapToDouble(Linq::safeToDouble)
                .max()
                .orElse(0.0);
    }

    /**
     * Calcula el promedio de una secuencia de valores.
     * Los valores se convierten a double para el cálculo.
     *
     * @param source La secuencia de valores.
     * @return El promedio de los valores o 0.0 si la secuencia está vacía o es null.
     */
    public static <T> double average(Iterable<T> source) {
        if (!any(source)) {
            return 0.0;
        }
        DoubleSummaryStatistics stats = StreamSupport.stream(source.spliterator(), false)
                .mapToDouble(Linq::safeToDouble)
                .summaryStatistics();
        return stats.getAverage();
    }

    /**
     * Calcula el promedio de una secuencia de valores transformados.
     *
     * @param <T>     El tipo de los elementos de la fuente.
     * @param source  La secuencia de valores.
     * @param selector Función de transformación que se aplica a cada elemento.
     * @return El promedio de los valores transformados o 0.0 si la secuencia está vacía o el selector es null.
     */
    public static <T> double average(Iterable<T> source, Function<T, ?> selector) {
        if (!any(source) || selector == null) {
            return 0.0;
        }
        DoubleSummaryStatistics stats = StreamSupport.stream(source.spliterator(), false)
                .map(selector)
                .mapToDouble(Linq::safeToDouble)
                .summaryStatistics();
        return stats.getAverage();
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

    /**
     * Ordena los elementos de una secuencia en orden ascendente según una clave.
     *
     * @param <T>         El tipo de los elementos de la fuente.
     * @param <U>         El tipo de la clave de ordenación.
     * @param source      La secuencia de valores que se va a ordenar.
     * @param keySelector Función para extraer la clave de un elemento.
     * @return Una nueva lista ordenada en orden ascendente según la clave especificada.
     */
    public static <T, U extends Comparable<? super U>> List<T> orderBy(
            Iterable<T> source, 
            Function<T, U> keySelector) {
        return orderBy(source, keySelector, true);
    }

    /**
     * Ordena los elementos de una secuencia en orden ascendente o descendente según una clave.
     *
     * @param <T>         El tipo de los elementos de la fuente.
     * @param <U>         El tipo de la clave de ordenación.
     * @param source      La secuencia de valores que se va a ordenar.
     * @param keySelector Función para extraer la clave de un elemento.
     * @param ascending   true para orden ascendente, false para orden descendente.
     * @return Una nueva lista ordenada según la clave especificada.
     */
    public static <T, U extends Comparable<? super U>> List<T> orderBy(
            Iterable<T> source, 
            Function<T, U> keySelector, 
            boolean ascending) {
        
        if (!any(source) || keySelector == null) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>();
        source.forEach(result::add);

        if (ascending) {
            result.sort(Comparator.comparing(keySelector));
        } else {
            result.sort(Comparator.comparing(keySelector).reversed());
        }

        return result;
    }

    /**
     * Ordena los elementos de una secuencia en orden descendente según una clave.
     *
     * @param <T>         El tipo de los elementos de la fuente.
     * @param <U>         El tipo de la clave de ordenación.
     * @param source      La secuencia de valores que se va a ordenar.
     * @param keySelector Función para extraer la clave de un elemento.
     * @return Una nueva lista ordenada en orden descendente según la clave especificada.
     */
    public static <T, U extends Comparable<? super U>> List<T> orderByDescending(
            Iterable<T> source, 
            Function<T, U> keySelector) {
        return orderBy(source, keySelector, false);
    }

    /**
     * Ordena los elementos de una secuencia en orden ascendente según su orden natural.
     *
     * @param <T>    El tipo de los elementos de la fuente (debe implementar Comparable).
     * @param source La secuencia de valores que se va a ordenar.
     * @return Una nueva lista ordenada en orden ascendente.
     */
    public static <T extends Comparable<? super T>> List<T> orderBy(Iterable<T> source) {
        return orderBy(source, Function.identity(), true);
    }

    /**
     * Ordena los elementos de una secuencia en orden descendente según su orden natural.
     *
     * @param <T>    El tipo de los elementos de la fuente (debe implementar Comparable).
     * @param source La secuencia de valores que se va a ordenar.
     * @return Una nueva lista ordenada en orden descendente.
     */
    public static <T extends Comparable<? super T>> List<T> orderByDescending(Iterable<T> source) {
        return orderBy(source, Function.identity(), false);
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

        // --- Ejemplos de select ---
        List<Integer> numbers = List.of(1,2,3);
        List<String> texts = Linq.select(numbers, n -> "N:" + n); // ["N:1","N:2","N:3"]

        List<String> fruits = List.of("apple","banana");
        List<Integer> lengths = Linq.select(fruits, String::length); // [5,6]

        // --- Ejemplos de selectMany ---
        List<String> words = List.of("hi", "abc");
        List<Character> chars = Linq.selectMany(words, s -> {
            List<Character> list = new ArrayList<>();
            for (char c : s.toCharArray()) list.add(c);
            return list;
        }); // [h, i, a, b, c]

        List<List<Integer>> nested = List.of(List.of(1,2), List.of(3), List.of());
        List<Integer> flat = Linq.selectMany(nested, x -> x); // [1, 2, 3]

        // --- Ejemplos de findIndex ---
        List<String> fruits = List.of("apple","banana","cherry");
        int idx = Linq.findIndex(fruits, s -> s.startsWith("b")); // 1
        int none = Linq.findIndex(fruits, s -> s.length() > 10);  // -1

        // --- Ejemplos de findIndex ---
        List<Integer> nums = List.of(1,2,2,3,1);
        List<Integer> uniq = Linq.distinct(nums); // [1, 2, 3]

        List<String> fruits = List.of("apple","apple","banana");
        List<String> uniqFruits = Linq.distinct(fruits); // [apple, banana]

        // --- Ejemplos de count ---
        List<Integer> nums = List.of(1,2,2,3,1);
        int total = Linq.count(nums);                        // 5
        int evens = Linq.count(nums, n -> n % 2 == 0);       // 2

        List<String> empty = List.of();
        int zero = Linq.count(empty);                        // 0
        int zeroPred = Linq.count(empty, s -> s.isEmpty());  // 0

        // --- Ejemplos de sum ---
        List<Integer> numeros = List.of(1, 2, 3, 4, 5);
        double total = Linq.sum(numeros); // 15.0

        List<Object> varios = List.of("1", 2, "3.5", "$4.99", "5,000", "no-numérico");
        double total = Linq.sum(varios); // 5011.49

        class Producto {
            String nombre;
            double precio;
            // getters, setters, constructor
        }

        List<Producto> productos = List.of(
            new Producto("Laptop", 1200.0),
            new Producto("Mouse", 25.5),
            new Producto("Teclado", 45.0)
        );

        double totalPrecios = Linq.sum(productos, p -> p.getPrecio()); // 1270.5


        class Transaccion {
            String monto; // podría ser "$1,000.50"
            // getters, setters
        }

        List<Transaccion> transacciones = // ...
        double total = Linq.sum(transacciones, t -> t.getMonto());

        // Con números
        List<Integer> nums = List.of(1, 2, 3);
        double suma1 = Linq.sum(nums); // 6.0

        // Con strings que representan números
        List<String> strNums = List.of("1", "2.5", "3");
        double suma2 = Linq.sum(strNums); // 6.5

        // Con formato de moneda
        List<String> precios = List.of("$1.99", "2,500.50", "3.75");
        double total = Linq.sum(precios); // 2506.24

        // Con selector
        List<Map<String, Object>> datos = List.of(
            Map.of("valor", "100"),
            Map.of("valor", 200.5),
            Map.of("valor", "$300,50")
        );
        double suma3 = Linq.sum(datos, m -> m.get("valor")); // 601.0

        // --- Ejemplos de min & max ---
        List<Object> numeros = List.of("10", 5, "3.5", "$7.99", "1,000", "no-numérico");

        // Mínimo
        double min = Linq.min(numeros); // 3.5

        // Máximo
        double max = Linq.max(numeros); // 1000.0

        // Con selector
        class Producto {
            String nombre;
            String precio; // Formato: "$1,000.50"
            // getters, setters
        }

        List<Producto> productos = // ...
        double precioMin = Linq.min(productos, p -> p.getPrecio());
        double precioMax = Linq.max(productos, p -> p.getPrecio());

        // --- Ejemplos de average ---
        List<Object> numeros = List.of("10", 5, "3.5", "$7.99", "1,000", "no-numérico");

        // Promedio directo
        double promedio = Linq.average(numeros); // Calcula el promedio

        // Con selector
        class Producto {
            String nombre;
            String precio; // Formato: "$1,000.50"
            // getters, setters
        }

        List<Producto> productos = // ...
        double precioPromedio = Linq.average(productos, p -> p.getPrecio());


        // --- Ejemplos de orderBy ---
        class Persona {
            String nombre;
            int edad;
            // getters, constructor
        }

        List<Persona> personas = List.of(
            new Persona("Ana", 25),
            new Persona("Carlos", 30),
            new Persona("Beatriz", 20)
        );

        // Ordenar por nombre (ascendente por defecto)
        List<Persona> porNombre = Linq.orderBy(personas, p -> p.getNombre());

        // Ordenar por edad (descendente)
        List<Persona> porEdadDesc = Linq.orderBy(personas, p -> p.getEdad(), false);
        // O alternativamente:
        List<Persona> porEdadDesc2 = Linq.orderByDescending(personas, p -> p.getEdad());

        // Ordenar números en orden natural
        List<Integer> numeros = List.of(3, 1, 4, 1, 5, 9);
        List<Integer> ordenados = Linq.orderBy(numeros);  // [1, 1, 3, 4, 5, 9]

        // --- Ejemplos de all ---

        // Verificar si todos los números son positivos
        List<Integer> numeros = List.of(1, 2, 3, 4, 5);
        boolean todosPositivos = Linq.all(numeros, n -> n > 0);  // true

        // Verificar si todos los strings tienen longitud mayor a 3
        List<String> palabras = List.of("casa", "perro", "gato");
        boolean todosLargos = Linq.all(palabras, s -> s.length() > 3);  // true

        // Con una lista vacía
        List<String> vacia = List.of();
        boolean resultado = Linq.all(vacia, s -> s.length() > 10);  // true

        // Con nulos
        boolean conNulo = Linq.all(null, x -> true);  // true
        boolean conPredicadoNulo = Linq.all(numeros, null);  // false
    }*/
}
