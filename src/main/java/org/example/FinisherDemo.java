package org.example;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Gatherer;

public class FinisherDemo {
    public static void main(String[] args) {

        var nums = List.of(10, 31, 23, 44, 15, 63, 71, 82, 19, 11);

        //distinct sorting
        Gatherer<Integer, TreeSet<Integer>, Integer> sortGatherer = Gatherer.ofSequential(
                // initalizer
                TreeSet::new,
                (set, element, _) -> {
                    set.add(element);
                    return true;
                },
                // no more element to push so no element here
                (set, downstream) -> {
                    // you need to check if downstream is rejecting or not if you do not does not matter it will anyway not use them you will waster cpu cycles
                    set.stream()
                            //.takeWhile(_ -> !downstream.isRejecting()).forEach(downstream::push);
                            .allMatch(downstream::push);
                }
        );

        var numsSorted = nums.stream()
                .gather(sortGatherer)
                // add later
                .limit(3)
                .toList();

        System.out.println(numsSorted);

    }
}
