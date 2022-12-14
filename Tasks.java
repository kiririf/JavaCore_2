import org.w3c.dom.ls.LSOutput;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Tasks {

    static class Person {
        final int id;

        final String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;
            return getId() == person.getId() && getName().equals(person.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName());
        }
    }

    private static final Person[] RAW_DATA = new Person[]{
            new Person(0, "Harry"),
            new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(7, "Amelia"),
            new Person(8, "Amelia"),
    };
        /*  Raw data:

        0 - Harry
        0 - Harry
        1 - Harry
        2 - Harry
        3 - Emily
        4 - Jack
        4 - Jack
        5 - Amelia
        5 - Amelia
        6 - Amelia
        7 - Amelia
        8 - Amelia

        **************************************************

        Duplicate filtered, grouped by name, sorted by name and id:

        Amelia:
        1 - Amelia (5)
        2 - Amelia (6)
        3 - Amelia (7)
        4 - Amelia (8)
        Emily:
        1 - Emily (3)
        Harry:
        1 - Harry (0)
        2 - Harry (1)
        3 - Harry (2)
        Jack:
        1 - Jack (4)
     */

    public static void main(String[] args) {
        System.out.println("Raw data:");
        System.out.println();

        for (Person person : RAW_DATA) {
            System.out.println(person.id + " - " + person.name);
        }

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();
        System.out.println("Duplicate filtered, grouped by name, sorted by name and id:");
        System.out.println();

        /*
        Task1
            Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени

            Что должно получиться Key: Amelia
                Value:4
                Key: Emily
                Value:1
                Key: Harry
                Value:3
                Key: Jack
                Value:1
         */
        Predicate<Person> isNotNull = (person) -> Objects.nonNull(person) && Objects.nonNull(person.getName()); // условие для отбраковки null

        Map<String, Long> sortedData = Arrays.stream(RAW_DATA)
                .filter(isNotNull)
                .distinct()
                .sorted(Comparator.comparing(Person::getId))// сортировка по ИД
                .collect(groupingBy(Person::getName, Collectors.counting()));
        sortedData.forEach(
                (k, v) -> System.out.println("key: " + k + ", value: " + v));

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();
        System.out.println("First pair of numbers that gives 10:");
        System.out.println();

        /*
        Task2

            [3, 4, 2, 7], 10 -> [3, 7] - вывести пару именно в скобках, которые дают сумму - 10
         */

        BiFunction<Integer, List<Integer>, Boolean> finder = (num, list) -> {
            for (Integer integer : list) {
                if (integer + num == 10) {
                    System.out.println("[" + num + ", " + integer + "]");
                    return true;
                }
            }
            return false;
        };

        List<Integer> rawList = Arrays.asList(3, 4, 2, 7);
        Integer neededNumbers = Stream.of(3, 4, 2, 7)
                .filter(num -> finder.apply(num, rawList))
                .findFirst()
                .orElse(null);

        if(neededNumbers == null) System.out.println("Can't find needed pair of numbers");

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();
        System.out.println("Compare of given strings:");
        System.out.println();

        /*
        Task3
            Реализовать функцию нечеткого поиска
        */

        System.out.println(fuzzySearch("car", "ca6$$#_rtwheel")); // true
        System.out.println(fuzzySearch("cwhl", "cartwheel")); // true
        System.out.println(fuzzySearch("cwhee", "cartwheel")); // true
        System.out.println(fuzzySearch("cartwheel", "cartwheel")); // true
        System.out.println(fuzzySearch("cwheeel", "cartwheel")); // false
        System.out.println(fuzzySearch("lw", "cartwheel")); // false

    }

    private static boolean fuzzySearch(String str1, String str2) {
        List<String> str1AsList = Arrays.stream(str1.split("")).toList();
        List<String> str2AsList = Arrays.stream(str2.split("")).toList();
        int result = 0;
        int ind = 0;
        for (String sym1 : str1AsList) {
            for(; ind < str2AsList.size(); ind++) {
                if(sym1.equals(str2AsList.get(ind))) {
                    result += 1;
                    ind++;
                    break;
                }
            }
        }
        return result == str1.length();
        /*
        Function<String, Optional<String>> finder = (sym) -> {
            return Arrays.stream(str2.split("")).filter(sym::equals).findFirst();
        };
        List<Character> indexes = Arrays.stream(str1.split(""))
                .map(finder)
                .filter(Optional::isPresent)
                .map(s -> String.valueOf(s).charAt(9))
                .toList();
        StringBuilder resultStr = new StringBuilder();
        for(Character c : indexes) {
            resultStr.append(c);}
        indexes.forEach(System.out::println);
        System.out.println(resultStr);
        return str1.equals(resultStr.toString());

         */
    }
}
