package se.lexicon.vxo.service;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import org.junit.jupiter.api.Test;
import se.lexicon.vxo.model.Gender;
import se.lexicon.vxo.model.Person;
import se.lexicon.vxo.model.PersonDto;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Your task is to make all tests pass (except task1 because its non-testable).
 * However, you have to solve each task by using a java.util.Stream or any of its variance.
 * You also need to use lambda expressions as implementation to functional interfaces.
 * (No Anonymous Inner Classes or Class implementation of functional interfaces)
 */
public class StreamExercise {

    private static List<Person> people = People.INSTANCE.getPeople();

    /**
     * Turn integers into a stream then use forEach as a terminal operation to print out the numbers
     */
    @Test
    public void task1() {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        //integers.stream().forEach(System.out::println);  //1. using method reference

        integers.stream().forEach(integer -> System.out.println(integer));  //2. using lambda

    }

    /**
     * Turning people into a Stream count all members
     */
    @Test
    public void task2() {
        long amount = 0;


        amount = people.stream().count();
        assertEquals(10000, amount);

        System.out.println("Count of people: " + amount);
    }

    /**
     * Count all people that has Andersson as lastName.
     */
    @Test
    public void task3() {
        long amount = 0;
        int expected = 90;


        amount = people.stream().filter(person -> person.getLastName().equals("Andersson")).count();

        assertEquals(expected, amount);

        System.out.println("Number of people with last name \"Andersson\": " + amount);
    }

    /**
     * Extract a list of all female
     */
    @Test
    public void task4() {

        int expectedSize = 4988;
        List<Person> females = people.stream().filter(person -> person.getGender() == Gender.FEMALE).toList();
        assertNotNull(females);
        assertEquals(expectedSize, females.size());

        System.out.println("List of female persons:");
        for (Person person : females) {
            System.out.println(person.toString());
        }
    }

    /**
     * Extract a TreeSet with all birthDates
     */
    @Test
    public void task5() {
        int expectedSize = 8882;
        Set<LocalDate> dates = null;

        dates = people.stream().map(Person::getDateOfBirth).collect(Collectors.toCollection(TreeSet::new));

        assertNotNull(dates);
        assertTrue(dates instanceof TreeSet);
        assertEquals(expectedSize, dates.size());

        System.out.println("TreeSet with all birthDates:");
        for (LocalDate date : dates) {
            System.out.println(date);
        }
    }

    /**
     * Extract an array of all people named "Erik"
     */
    @Test
    public void task6() {
        int expectedLength = 3;

        Person[] result = people.stream().filter(person -> person.getFirstName().equals("Erik")).toArray(Person[]::new);

        assertNotNull(result);
        assertEquals(expectedLength, result.length);

        System.out.println("People named \"Erik\":");
        for (Person person : result) {
            System.out.println (person.toString ());
        }
    }

    /**
     * Find a person that has id of 5436
     */
    @Test
    public void task7() {
        Person expected = new Person(5436, "Tea", "Håkansson", LocalDate.parse("1968-01-25"), Gender.FEMALE);

        Optional<Person> optional = people.stream().filter(p -> p.getPersonId() == 5436).findFirst();

        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());

        System.out.println("Person with id 5436:");
        System.out.println(optional.get().toString());


    }

    /**
     * Using min() define a comparator that extracts the oldest person i the list as an Optional
     */
    @Test
    public void task8() {
        LocalDate expectedBirthDate = LocalDate.parse("1910-01-02");

        Optional<Person> optional = people.stream().min(Comparator.comparing(Person::getDateOfBirth));



        assertNotNull(optional);
        assertEquals(expectedBirthDate, optional.get().getDateOfBirth());

        System.out.println("Oldest person in the list:");
        optional.ifPresent(System.out::println);
    }


    /**
     * Map each person born before 1920-01-01 into a PersonDto object then extract to a List
     */
    @Test
    public void task9() {
        int expectedSize = 892;
        LocalDate date = LocalDate.parse("1920-01-01");
        Function<Person, PersonDto> mapper = person -> new PersonDto(person.getPersonId(), person.getFirstName(), person.getLastName(), person.getDateOfBirth(), person.getGender());

        List<PersonDto> dtoList = people.stream()
                .filter(person -> person.getDateOfBirth().isBefore(date))
                .map(mapper)
                .collect(Collectors.toList());

        assertNotNull(dtoList);
        assertEquals(expectedSize, dtoList.size());

        System.out.println("List of PersonDto objects born before 1920-01-01:");
        dtoList.forEach(System.out::println);
    }

    /**
     * In a Stream Filter out one person with id 5914 from people and take the birthdate and build a string from data that the date contains then
     * return the string.
     */
    @Test
    public void task10() {
        String expected = "WEDNESDAY 19 DECEMBER 2012";
        int personId = 5914;

        Function<Person, String> birthdateString = person -> person.getDateOfBirth().getDayOfWeek().toString().toUpperCase() + " " + person.getDateOfBirth().getDayOfMonth() + " " + person.getDateOfBirth().getMonth().toString().toUpperCase() + " " + person.getDateOfBirth().getYear();
        Optional<String> optional = people.stream()
                .filter(person -> person.getPersonId() == personId)
                .map(birthdateString)
                .findFirst();

        assertNotNull(optional);
        assertTrue(optional.isPresent());
        assertEquals(expected, optional.get());

        System.out.println("Birthdate String: " + optional.get().toUpperCase());
    }


    /**
     * Get average age of all People by turning people into a stream and use defined ToIntFunction personToAge
     * changing type of stream to an IntStream.
     */
    @Test
    public void task11() {
                    ToIntFunction<Person> personToAge =
                            person -> Period.between(person.getDateOfBirth(), LocalDate.parse("2019-12-20")).getYears();
                    double expected = 54.42;
                    double averageAge = people.stream ()

                            .mapToInt(personToAge)
                            .average()
                            .orElse(0);

                    System.out.println("Average Age: " + averageAge);

                    assertTrue(averageAge > 0);
                    assertEquals(expected, averageAge, .01);
    }

    /**
     * Extract from people a sorted string array of all firstNames that are palindromes. No duplicates
     */
    @Test
    public void task12() {

        String[] expected = {"Ada", "Ana", "Anna", "Ava", "Aya", "Bob", "Ebbe", "Efe", "Eje", "Elle", "Hannah", "Maram", "Natan", "Otto"};

        String[] result = people.stream()

                .map(Person::getFirstName)
                .filter(s -> {
                    String str = s.toLowerCase(); // Convert to lowercase for case-insensitive comparison
                    return new StringBuilder(str).reverse().toString().equals(str);
                })
                .sorted()
                .distinct()
                .toArray(String[]::new);

        for (String name : result) {
            System.out.println(name);
        }
        assertNotNull(result);
        assertArrayEquals(expected, result);
    }

    /**
     * Extract from people a map where each key is a last name with a value containing a list of all that has that lastName
     */
    @Test
    public void task13() {
        int expectedSize = 107;
        Map<String, List<Person>> personMap =

                people.stream()
                        .collect(Collectors.groupingBy (Person::getLastName, Collectors.toList()));


        assertNotNull(personMap);
        assertEquals(expectedSize, personMap.size());

        personMap.forEach((lastName, peopleList) -> {
            System.out.println("Last Name: " + lastName);
            System.out.println("People with this last name:");
            peopleList.forEach(person -> System.out.println(person.getFirstName() + " " + person.getLastName()));
        });
    }

    /**
     * Create a calendar using Stream.iterate of year 2020. Extract to a LocalDate array
     */
    @Test
    public void task14() {
        LocalDate[] _2020_dates =

                Stream.iterate (LocalDate.of (2020, 1, 1), date -> date.plusDays (1))
                        .limit (366)
                        .toArray (LocalDate[]::new);




        assertNotNull(_2020_dates);
        assertEquals(366, _2020_dates.length);
        assertEquals(LocalDate.parse("2020-01-01"), _2020_dates[0]);
        assertEquals(LocalDate.parse("2020-12-31"), _2020_dates[_2020_dates.length - 1]);

        System.out.println ("2020 calendar");
        for(LocalDate date : _2020_dates) {
            System.out.println (date);
        }
    }
}
