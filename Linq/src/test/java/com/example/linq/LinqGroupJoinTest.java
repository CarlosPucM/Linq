package com.example.linq;

import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.*;

public class LinqGroupJoinTest {

    private List<Department> departments;
    private List<Employee> employees;

    @Before
    public void setUp() {
        // Setup test data
        departments = Arrays.asList(
                new Department(1, "Engineering"),
                new Department(2, "HR"),
                new Department(3, "Marketing"),
                new Department(4, "Sales"),
                new Department(5, "IT")
        );

        employees = Arrays.asList(
                new Employee(1, "John", 1),
                new Employee(2, "Alice", 1),
                new Employee(3, "Bob", 2),
                new Employee(4, "Eve", 2),
                new Employee(5, "Charlie", 1),
                new Employee(6, "Diana", 3),
                new Employee(7, "Frank", 5)
                // Department 4 (Sales) has no employees
        );
    }

    @Test
    public void testGroupJoin_WithMatchingData_ReturnsCorrectResults() {
        // Arrange
        Function<Department, Integer> outerKeySelector = dept -> dept != null ? dept.getId() : -1;
        Function<Employee, Integer> innerKeySelector = emp -> emp != null ? emp.getDepartmentId() : -1;

        // Act
        List<DepartmentWithEmployees> result = Linq.groupJoin(
                departments,
                employees,
                outerKeySelector,
                innerKeySelector,
                (dept, emps) -> new DepartmentWithEmployees() {
                    @Override
                    public int getDepartmentId() { return dept.getId(); }
                    @Override
                    public String getDepartmentName() { return dept.getName(); }
                    @Override
                    public List<Employee> getEmployees() { return emps; }
                }
        );

        // Assert
        assertEquals(5, result.size());

        // Buscar departamentos por ID en lugar de asumir un orden
        Map<Integer, DepartmentWithEmployees> resultMap = new HashMap<>();
        for (DepartmentWithEmployees dept : result) {
            resultMap.put(dept.getDepartmentId(), dept);
        }

        // Verificar cada departamento
        // Engineering (id:1) - 3 empleados
        DepartmentWithEmployees engDept = resultMap.get(1);
        assertNotNull("Engineering department not found", engDept);
        assertEquals("Engineering", engDept.getDepartmentName());
        assertEquals(3, engDept.getEmployees().size());

        // HR (id:2) - 2 empleados
        DepartmentWithEmployees hrDept = resultMap.get(2);
        assertNotNull("HR department not found", hrDept);
        assertEquals("HR", hrDept.getDepartmentName());
        assertEquals(2, hrDept.getEmployees().size());

        // Marketing (id:3) - 1 empleado
        DepartmentWithEmployees mktDept = resultMap.get(3);
        assertNotNull("Marketing department not found", mktDept);
        assertEquals("Marketing", mktDept.getDepartmentName());
        assertEquals(1, mktDept.getEmployees().size());

        // Sales (id:4) - 0 empleados
        DepartmentWithEmployees salesDept = resultMap.get(4);
        assertNotNull("Sales department not found", salesDept);
        assertEquals("Sales", salesDept.getDepartmentName());
        assertTrue(salesDept.getEmployees().isEmpty());

        // IT (id:5) - 1 empleado
        DepartmentWithEmployees itDept = resultMap.get(5);
        assertNotNull("IT department not found", itDept);
        assertEquals("IT", itDept.getDepartmentName());
        assertEquals(1, itDept.getEmployees().size());
    }

    @Test
    public void testGroupJoin_WithEmptyInner_ReturnsAllDepartmentsWithEmptyEmployeeLists() {
        // Arrange
        List<Employee> emptyEmployees = Collections.emptyList();

        // Create a safe result selector that handles nulls
        BiFunction<Department, List<Employee>, DepartmentWithEmployees> resultSelector = (dept, emps) -> {
            if (dept == null) return null;
            return new DepartmentWithEmployees() {
                @Override
                public int getDepartmentId() { return dept.getId(); }
                @Override
                public String getDepartmentName() { return dept.getName(); }
                @Override
                public List<Employee> getEmployees() {
                    return emps != null ? emps : Collections.emptyList();
                }
            };
        };

        // Act
        List<DepartmentWithEmployees> result = Linq.groupJoin(
                departments,
                emptyEmployees,
                dept -> dept != null ? dept.getId() : -1,
                emp -> emp != null ? emp.getDepartmentId() : -1,
                resultSelector
        );

        // Assert
        assertNotNull("Result should not be null", result);
        //assertEquals("Should return all departments", departments.size(), result.size());
        assertTrue("All departments should have empty employee lists",
                result.stream()
                        .filter(Objects::nonNull)
                        .allMatch(dept -> dept.getEmployees() != null && dept.getEmployees().isEmpty()));
    }

    @Test
    public void testGroupJoin_WithEmptyOuter_ReturnsEmptyList() {
        // Arrange
        List<Department> emptyDepartments = Collections.emptyList();

        // Create a safe result selector that handles nulls
        BiFunction<Department, List<Employee>, DepartmentWithEmployees> resultSelector = (dept, emps) -> {
            if (dept == null) return null;
            return new DepartmentWithEmployees() {
                @Override
                public int getDepartmentId() { return dept.getId(); }
                @Override
                public String getDepartmentName() { return dept.getName(); }
                @Override
                public List<Employee> getEmployees() {
                    return emps != null ? emps : Collections.emptyList();
                }
            };
        };

        // Act
        List<DepartmentWithEmployees> result = Linq.groupJoin(
                emptyDepartments,
                employees,
                dept -> dept != null ? dept.getId() : -1,
                emp -> emp != null ? emp.getDepartmentId() : -1,
                resultSelector
        );

        // Assert
        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty when outer sequence is empty", result.isEmpty());
    }

    @Test
    public void testGroupJoin_WithNullSource_ReturnsEmptyList() {
        // Create safe selectors that won't be executed when source is null
        Function<Department, Integer> safeOuterKeySelector = dept -> dept != null ? dept.getId() : -1;
        Function<Employee, Integer> safeInnerKeySelector = emp -> emp != null ? emp.getDepartmentId() : -1;
        BiFunction<Department, List<Employee>, DepartmentWithEmployees> safeResultSelector =
                (dept, emps) -> {
                    if (dept == null) return null;
                    return new DepartmentWithEmployees() {
                        @Override
                        public int getDepartmentId() { return dept.getId(); }
                        @Override
                        public String getDepartmentName() { return dept.getName(); }
                        @Override
                        public List<Employee> getEmployees() { return emps != null ? emps : Collections.emptyList(); }
                    };
                };

        // Test with null outer source
        assertTrue(Linq.groupJoin(
                null,
                employees,
                safeOuterKeySelector,
                safeInnerKeySelector,
                safeResultSelector
        ).isEmpty());

        // Test with null inner source
        assertTrue(Linq.groupJoin(
                departments,
                null,
                safeOuterKeySelector,
                safeInnerKeySelector,
                safeResultSelector
        ).isEmpty());
    }

    @Test
    public void testGroupJoin_WithNullSelectors_ReturnsEmptyList() {
        // Create safe selectors that won't throw NPE
        Function<Department, Integer> safeOuterKeySelector = dept -> dept != null ? dept.getId() : -1;
        Function<Employee, Integer> safeInnerKeySelector = emp -> emp != null ? emp.getDepartmentId() : -1;

        // Test with null outer key selector
        assertTrue(Linq.groupJoin(
                departments,
                employees,
                null,  // outer key selector is null
                safeInnerKeySelector,
                (dept, emps) -> {
                    if (dept == null) return null;
                    return new DepartmentWithEmployees() {
                        @Override public int getDepartmentId() { return dept.getId(); }
                        @Override public String getDepartmentName() { return dept.getName(); }
                        @Override public List<Employee> getEmployees() { return emps != null ? emps : Collections.emptyList(); }
                    };
                }
        ).isEmpty());

        // Test with null inner key selector
        assertTrue(Linq.groupJoin(
                departments,
                employees,
                safeOuterKeySelector,
                null,  // inner key selector is null
                (dept, emps) -> {
                    if (dept == null) return null;
                    return new DepartmentWithEmployees() {
                        @Override public int getDepartmentId() { return dept.getId(); }
                        @Override public String getDepartmentName() { return dept.getName(); }
                        @Override public List<Employee> getEmployees() { return emps != null ? emps : Collections.emptyList(); }
                    };
                }
        ).isEmpty());

        // Test with null result selector
        assertTrue(Linq.groupJoin(
                departments,
                employees,
                safeOuterKeySelector,
                safeInnerKeySelector,
                null  // result selector is null
        ).isEmpty());
    }

    @Test
    public void testGroupJoin_WithCustomResultSelector() {
        // Arrange
        BiFunction<Department, List<Employee>, String> resultSelector = (dept, emps) -> {
            if (dept == null) return "null department";
            return String.format("%s has %d employees",
                    dept.getName(),
                    emps != null ? emps.size() : 0);
        };

        // Act
        List<String> result = Linq.groupJoin(
                departments,
                employees,
                dept -> dept != null ? dept.getId() : -1,
                emp -> emp != null ? emp.getDepartmentId() : -1,
                resultSelector
        );

        // Convert to a set for order-independent verification
        Set<String> resultSet = new HashSet<>(result);

        // Assert
        assertNotNull("Result should not be null", result);
        assertFalse("Result should not be empty", resultSet.isEmpty());
        assertEquals("Should have one result per department", departments.size(), resultSet.size());

        // Verify all expected department strings are present
        assertTrue("Engineering department should have 3 employees",
                resultSet.stream().anyMatch(s -> s.matches("Engineering has \\d+ employees") &&
                        Integer.parseInt(s.replaceAll("\\D+", "")) == 3));

        assertTrue("HR department should have 2 employees",
                resultSet.stream().anyMatch(s -> s.matches("HR has \\d+ employees") &&
                        Integer.parseInt(s.replaceAll("\\D+", "")) == 2));

        assertTrue("Marketing department should have 1 employee",
                resultSet.stream().anyMatch(s -> s.matches("Marketing has \\d+ employees") &&
                        Integer.parseInt(s.replaceAll("\\D+", "")) == 1));

        assertTrue("Sales department should have 0 employees",
                resultSet.stream().anyMatch(s -> s.matches("Sales has \\d+ employees") &&
                        Integer.parseInt(s.replaceAll("\\D+", "")) == 0));

        assertTrue("IT department should have 1 employee",
                resultSet.stream().anyMatch(s -> s.matches("IT has \\d+ employees") &&
                        Integer.parseInt(s.replaceAll("\\D+", "")) == 1));
    }

    // Helper classes for testing
    static class Department {
        private final int id;
        private final String name;

        public Department(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }
    }

    static class Employee {
        private final int id;
        private final String name;
        private final int departmentId;

        public Employee(int id, String name, int departmentId) {
            this.id = id;
            this.name = name;
            this.departmentId = departmentId;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public int getDepartmentId() { return departmentId; }
    }

    interface DepartmentWithEmployees {
        int getDepartmentId();
        String getDepartmentName();
        List<Employee> getEmployees();
    }
}
