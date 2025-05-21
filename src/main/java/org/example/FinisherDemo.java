package org.example;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Gatherer;

public class FinisherDemo {
    public static void main(String[] args) {

        var nums = List.of(10, 31, 23, 44, 15, 63, 71, 82, 19, 11);

        //distinct sorting
        Gatherer<Integer, TreeSet<Integer>, Integer> sortGatherer = Gatherer.ofSequential(
                TreeSet::new,
                (set, element, downstream) -> {
                    set.add(element);
                    return true;
                },
                (set,  downstream) -> {
                    set.stream().allMatch(downstream::push);
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
