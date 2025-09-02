# Changelog

## [0.2.0] - 2025-09-02
### Added
- Métodos de agregación numérica:
  - `sum`: Para sumar valores numéricos
  - `min`/`max`: Para encontrar valores mínimo y máximo
  - `average`: Para calcular promedios
- Métodos de ordenación:
  - `orderBy`: Para ordenar en orden ascendente
  - `orderByDescending`: Para ordenar en orden descendente
- Método `all`: Para verificar si todos los elementos cumplen una condición
- Soporte para diferentes tipos de datos en operaciones numéricas (números, strings, booleanos, caracteres)
- Manejo de formatos de moneda y separadores de miles en strings

## [0.1.2] - 2025-09-02
### Added
- Método `select` para transformar elementos de una secuencia
- Método `selectMany` para aplanar secuencias anidadas
- Método `findIndex` para encontrar el índice del primer elemento que cumple un predicado
- Método `distinct` para eliminar elementos duplicados
- Método `count` (con y sin predicado) para contar elementos

### Changed
- Actualizadas dependencias de Gradle y plugins

## [0.1.0] - 2025-09-01
### Added
- Versión inicial de la librería
- Métodos implementados:
  - firstOrDefault
  - any
  - where
  - take
  - skip
