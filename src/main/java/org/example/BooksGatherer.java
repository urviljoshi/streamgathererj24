package org.example;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Gatherer;

public class BooksGatherer {
    private BooksGatherer() {} // not to be init

    public static <K> Gatherer<Books, Map<K, List<Books>>, Map.Entry<K,List<Books>>> createSampleBooksGatherer(
            Function<? super Books, ? extends K> keyExtractor,
            Comparator<? super Books> comparator,
            int limit
    ) {
        return Gatherer.of(
                // mutable state
                HashMap::new,
                (map,book, _) -> {
                    // get key
                    K key = keyExtractor.apply(book);
                    // add list if not exist for key
                    map.computeIfAbsent(key, k -> new ArrayList<>()).add(book);
                    // accept elements
                    return true;
                },
                // ignore
                (map1, map2) -> map1,
                (map, downstream) -> {
                    map.forEach((key, books) -> {
                        // do operations on every book of a K
                        books.stream().sorted(comparator)
                                .limit(limit)
                                .toList();
                        downstream.push(Map.entry(key, books));
                    });
                }



        );
    }
}
