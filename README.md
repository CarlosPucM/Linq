# Linq para Java

[![Release](https://jitpack.io/v/CarlosPucM/Linq.svg)](https://jitpack.io/#CarlosPucM/Linq)

Una implementación de operadores estilo LINQ para Java, inspirada en .NET LINQ, que proporciona una forma más expresiva de trabajar con colecciones.

## Características

- **Fácil de usar**: Sintaxis simple y familiar para desarrolladores que conocen LINQ de .NET
- **Ligero**: Sin dependencias externas (solo requiere Java 8+)
- **Tipo seguro**: Utiliza genéricos de Java para seguridad de tipos
- **Extensible**: Fácil de extender con nuevos operadores personalizados

## Instalación

### Usando JitPack

1. Agrega el repositorio JitPack a tu `build.gradle`:
   ```gradle
   allprojects {
       repositories {
           maven { url 'https://jitpack.io' }
       }
   }
   ```

2. Agrega la dependencia:
   ```gradle
   dependencies {
       implementation 'com.github.CarlosPucM:Linq:v0.1.2'
   }
   ```

## Uso Básico

```java
import com.example.linq.Linq;
import java.util.List;

public class Ejemplo {
    public static void main(String[] args) {
        List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Filtrar números pares
        List<Integer> pares = Linq.where(numeros, n -> n % 2 == 0);
        
        // Transformar elementos
        List<String> comoTexto = Linq.select(numeros, n -> "Número: " + n);
        
        // Contar elementos que cumplen una condición
        int cantidadImpares = Linq.count(numeros, n -> n % 2 != 0);
    }
}
```

## Métodos Disponibles

### Filtrado
- `where(Iterable<T> source, Predicate<T> predicate)`: Filtra elementos según un predicado
- `distinct(Iterable<T> source)`: Elimina elementos duplicados

### Proyección
- `select(Iterable<T> source, Function<T, R> selector)`: Transforma cada elemento
- `selectMany(Iterable<T> source, Function<T, Iterable<R>> selector)`: Aplana secuencias anidadas

### Particionamiento
- `take(Iterable<T> source, int count)`: Toma los primeros N elementos
- `skip(Iterable<T> source, int count)`: Omite los primeros N elementos

### Búsqueda
- `findIndex(Iterable<T> source, Predicate<T> predicate)`: Encuentra el índice del primer elemento que cumple el predicado
- `firstOrDefault(Iterable<T> source)`: Obtiene el primer elemento o null
- `firstOrDefault(Iterable<T> source, Predicate<T> predicate)`: Obtiene el primer elemento que cumple el predicado o null

### Agregación
- `count(Iterable<T> source)`: Cuenta todos los elementos
- `count(Iterable<T> source, Predicate<T> predicate)`: Cuenta los elementos que cumplen el predicado
- `any(Iterable<T> source)`: Verifica si hay elementos
- `any(Iterable<T> source, Predicate<T> predicate)`: Verifica si algún elemento cumple el predicado
- `all(Iterable<T> source, Predicate<T> predicate)`: Verifica si todos los elementos cumplen el predicado

### Operaciones Numéricas
- `sum(Iterable<T> source)`: Suma los valores numéricos de la secuencia
- `sum(Iterable<T> source, Function<T, ?> selector)`: Suma los valores extraídos por el selector
- `min(Iterable<T> source)`: Encuentra el valor mínimo
- `min(Iterable<T> source, Function<T, ?> selector)`: Encuentra el valor mínimo según el selector
- `max(Iterable<T> source)`: Encuentra el valor máximo
- `max(Iterable<T> source, Function<T, ?> selector)`: Encuentra el valor máximo según el selector
- `average(Iterable<T> source)`: Calcula el promedio de los valores
- `average(Iterable<T> source, Function<T, ?> selector)`: Calcula el promedio de los valores extraídos

### Ordenación
- `orderBy(Iterable<T> source)`: Ordena en orden natural ascendente
- `orderBy(Iterable<T> source, Function<T, ? extends Comparable> keySelector)`: Ordena por clave en orden ascendente
- `orderByDescending(Iterable<T> source)`: Ordena en orden natural descendente
- `orderByDescending(Iterable<T> source, Function<T, ? extends Comparable> keySelector)`: Ordena por clave en orden descendente

## Soporte para Diferentes Tipos de Datos

Los métodos numéricos (`sum`, `min`, `max`, `average`) soportan diferentes tipos de datos:

```java
// Con números
List<Integer> numeros = List.of(1, 2, 3, 4, 5);
double suma = Linq.sum(numeros);  // 15.0

// Con strings que representan números
List<String> strNumeros = List.of("1", "2.5", "3");
double sumaStr = Linq.sum(strNumeros);  // 6.5

// Con formato de moneda
List<String> precios = List.of("$1.99", "2,500.50", "3.75");
double total = Linq.sum(precios);  // 2506.24

// Con objetos personalizados
class Producto {
    String nombre;
    String precio; // Formato: "$1,000.50"
    // getters, setters
}

List<Producto> productos = List.of(
    new Producto("Laptop", "$1,200.00"),
    new Producto("Mouse", "$25.50"),
    new Producto("Teclado", "$45.00")
);

double totalVentas = Linq.sum(productos, p -> p.getPrecio());

## Ejemplos Avanzados

### Encadenamiento de operaciones
```java
List<String> resultado = Linq.where(
    Linq.select(numeros, n -> n * 2),  // Multiplica por 2
    n -> n > 5                         // Filtra mayores a 5
);
```

### Uso con objetos complejos
```java
class Persona {
    String nombre;
    int edad;
    // getters, setters, constructor
}

List<Persona> personas = List.of(
    new Persona("Ana", 25),
    new Persona("Carlos", 30),
    new Persona("Beatriz", 20)
);

// Ordenar por edad descendente
List<Persona> personasOrdenadas = Linq.orderByDescending(personas, p -> p.getEdad());

// Calcular el promedio de edad
double edadPromedio = Linq.average(personas, p -> p.getEdad());

// Verificar si todos son mayores de edad
boolean todosMayores = Linq.all(personas, p -> p.getEdad() >= 18);

// Sumar todas las edades
double sumaEdades = Linq.sum(personas, p -> p.getEdad());

// Encontrar la edad mínima y máxima
double edadMinima = Linq.min(personas, p -> p.getEdad());
double edadMaxima = Linq.max(personas, p -> p.getEdad());

// Personas mayores de 18 años, ordenadas por nombre
List<String> nombres = Linq.select(
    Linq.where(personas, p -> p.getEdad() >= 18),
    p -> p.getNombre()
);
```

## Contribución

¡Las contribuciones son bienvenidas! Por favor, lee la guía de contribución antes de enviar un pull request.

## Licencia

Este proyecto está licenciado bajo la [Licencia MIT](LICENSE).
