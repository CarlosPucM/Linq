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

List<Persona> personas = /* ... */;

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
