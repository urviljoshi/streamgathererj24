package org.example;


import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;

public class Main {

    public static void main(String[] args) {
        var books = createSampleBooks();
        mapConcurrentDemo(books);
    }

    private static void latestBooksforAllCatagoriesGatherer(List<Books> books){
        Map<String,List<Books>> copies = books.stream()
        .gather(BooksGatherer.createSampleBooksGatherer(Books::category, Comparator.comparing(Books::publishedDate).reversed(), 2))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        printbooksMap(copies);
    }

    // prior to java 24 group by using category and give me latest 2 published books
    private static void latestBooksforAllCatagoriesWay1(List<Books> books) {
        Map<String,List<Books>> copies = books.stream()
                // group by category
                .collect(Collectors.groupingBy(Books::category,
                        // collect books to list
                        Collectors.collectingAndThen(Collectors.toList(),
                                // sorting by published Date
                                bs -> bs.stream().sorted(Comparator.comparing(Books::publishedDate).reversed())
                                        // limit to 2
                                        .limit(2)
                                        .toList())));
        printbooksMap(copies);
    }

    private static void latestBooksforAllCatagoriesWay2(List<Books> books) {
        Map<String,List<Books>> copies =  books.stream()
                .collect(Collectors.groupingBy(Books::category))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .sorted(Comparator.comparing(Books::publishedDate).reversed())
                                .limit(3)
                                .toList()));

        printbooksMap(copies);
    }


    private static void fixWindowSized(List<Books> books) {
        books.stream().limit(9).
                gather(Gatherers.windowFixed(3))
                .forEach(batch -> {
                    System.out.println("Batch");
                    batch.forEach(System.out::println);
                });

    }

    private static void fixWindowSliding(List<Books> books) {
        books.stream().limit(9).
                gather(Gatherers.windowSliding(2))
                .forEach(window -> {
                    System.out.println("Window");
                    window.forEach(System.out::println);
                });

    }

    private static void foldDemo(List<Books> books) {
        books.stream().limit(9).
                gather(Gatherers.fold( () -> "All titles: ", (res,po) -> res.concat(po.title()).concat(",")))
                .forEach(System.out::println);
    }

    private static void scanDemo(List<Books> books) {
        books.stream().limit(9).
                gather(Gatherers.scan( () -> "Titles so far ", (res,po) -> res.concat(po.title()).concat(",")))
                .forEach(System.out::println);

    }

    //run gatherer in parallel even if your stream is sequential
    private static void mapConcurrentDemo(List<Books> books){
        books.stream().
                gather(Gatherers.mapConcurrent(
                        // these are virtual threads
                        4,
                        b ->
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return Map.entry(b.title(), b.title().length());

                })).forEach(entry -> System.out.println("Key : "+entry.getKey() +"   value:: "+entry.getValue() ) );
    }


    private static void printbooksMap(Map<String, List<Books>> copies) {
    copies.forEach((category, blogBooks) -> {
        System.out.println("catagory :: "+category);
        blogBooks.forEach(System.out::println);
    });
    }



    private static List<Books> createSampleBooks() {
        return List.of(
                new Books(
                        1L,
                        "Getting Started with JDK 24 Stream Gatherers",
                        "Joe Alison",
                        "Learn how to use the exciting new Stream Gatherers feature in JDK 24 .",
                        "Java",
                        LocalDateTime.of(2025, 3, 18, 10, 15)),
                new Books(
                        2L,
                        "Mastering Spring Boot 3",
                        "Jane Smith",
                        "A comprehensive guide to building robust applications with Spring Boot 3.",
                        "Java",
                        LocalDateTime.of(2025, 2, 10, 14, 30)),
                new Books(
                        3L,
                        "Python for Data Science",
                        "Alice Johnson",
                        "Explore Python's powerful libraries for data analysis and machine learning.",
                        "Python",
                        LocalDateTime.of(2024, 12, 5, 9, 0)),
                new Books(
                        4L,
                        "React in Action",
                        "Mark Brown",
                        "Build modern web applications with React and its ecosystem.",
                        "JavaScript",
                        LocalDateTime.of(2025, 1, 22, 11, 45)),
                new Books(
                        5L,
                        "Deep Learning Fundamentals",
                        "Sarah Davis",
                        "An introduction to neural networks and deep learning techniques.",
                        "AI",
                        LocalDateTime.of(2025, 4, 1, 16, 20)),
                new Books(
                        6L,
                        "Kubernetes for Beginners",
                        "David Wilson",
                        "Learn container orchestration with Kubernetes from scratch.",
                        "DevOps",
                        LocalDateTime.of(2024, 11, 15, 13, 10)),
                new Books(
                        7L,
                        "Functional Programming in Scala",
                        "Emily Clark",
                        "Discover functional programming concepts using Scala.",
                        "Scala",
                        LocalDateTime.of(2025, 3, 5, 10, 0)),
                new Books(
                        8L,
                        "Rust Programming Essentials",
                        "Michael Lee",
                        "Get started with Rust for safe and performant systems programming.",
                        "Rust",
                        LocalDateTime.of(2025, 2, 28, 15, 50)),
                new Books(
                        9L,
                        "GraphQL API Design",
                        "Laura Adams",
                        "Learn how to design and implement GraphQL APIs effectively.",
                        "APIs",
                        LocalDateTime.of(2024, 10, 20, 12, 30)),
                new Books(
                        10L,
                        "Advanced TypeScript",
                        "Chris Evans",
                        "Master TypeScript's advanced features for large-scale applications.",
                        "TypeScript",
                        LocalDateTime.of(2025, 1, 10, 9, 15)),
                new Books(
                        11L,
                        "Cloud Native Development",
                        "Anna Taylor",
                        "Build scalable applications for the cloud with modern tools.",
                        "Cloud",
                        LocalDateTime.of(2025, 4, 15, 14, 0)),
                new Books(
                        12L,
                        "Go Programming in Practice",
                        "Robert Harris",
                        "Practical guide to building efficient applications with Go.",
                        "Go",
                        LocalDateTime.of(2024, 12, 12, 11, 20)),
                new Books(
                        13L,
                        "Microservices with Spring Cloud",
                        "Lisa Walker",
                        "Learn to build and deploy microservices using Spring Cloud.",
                        "Java",
                        LocalDateTime.of(2025, 3, 25, 10, 40)),
                new Books(
                        14L,
                        "Introduction to Kotlin",
                        "James Green",
                        "A beginner's guide to Kotlin for Android and backend development.",
                        "Kotlin",
                        LocalDateTime.of(2025, 2, 15, 13, 25)),
                new Books(
                        15L,
                        "Data Structures in C++",
                        "Sophie King",
                        "Master data structures and algorithms using C++.",
                        "C++",
                        LocalDateTime.of(2024, 11, 30, 9, 50)),
                new Books(
                        16L,
                        "Machine Learning with TensorFlow",
                        "Daniel Wright",
                        "Build machine learning models using TensorFlow and Python.",
                        "AI",
                        LocalDateTime.of(2025, 1, 5, 15, 10)),
                new Books(
                        17L,
                        "Web Development with Django",
                        "Olivia Scott",
                        "Create robust web applications with Python and Django.",
                        "Python",
                        LocalDateTime.of(2025, 3, 12, 12, 15)),
                new Books(
                        18L,
                        "Blockchain Basics",
                        "Thomas Baker",
                        "Understand blockchain technology and its applications.",
                        "Blockchain",
                        LocalDateTime.of(2024, 10, 25, 14, 50)),
                new Books(
                        19L,
                        "Modern C# Programming",
                        "Emma Carter",
                        "Explore the latest features of C# for building applications.",
                        "C#",
                        LocalDateTime.of(2025, 2, 20, 10, 30)),
                new Books(
                        20L,
                        "DevOps with AWS",
                        "William Turner",
                        "Learn DevOps practices using AWS tools and services.",
                        "DevOps",
                        LocalDateTime.of(2025, 4, 8, 11, 0))
        );
    }

}
