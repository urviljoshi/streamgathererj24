package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Gatherer;

public class ParallelDemo {

    public static void main(String[] args) {

        var nums = List.of(10, 30, 23, 23, 15, 18, 71, 11, 19, 11);



        Gatherer<Integer, HashSet<Integer>, Integer> distinctGatherer = Gatherer.of(
                HashSet::new,
                (set, element, downstream) -> {
                    set.add(element);
                    return true;
                },(s1,s2) -> {
                    s1.addAll(s2);
                    return s1;
                },
                (set, downstream) -> {
                    set.stream().allMatch(downstream::push);
                }
        );

        var numsSorted = nums.stream()
                .parallel()
                .gather(distinctGatherer)
                .toList();

        System.out.println(numsSorted);
    }
}
