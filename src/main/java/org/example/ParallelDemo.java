package org.example;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Gatherer;

public class ParallelDemo {

    public static void main(String[] args) {

        var nums = List.of(10, 30, 23, 23, 15, 18, 71, 11, 19, 11);

        //distinct
       /* Gatherer<Integer, HashSet<Integer>, Integer> distinctGatherer = Gatherer.ofSequential(
                // initalizer
                HashSet::new,
                (set, element, downstream) -> {
                    if(set.add(element)) {
                       return downstream.push(element);
                    }
                    return true;
                }
        );*/

        /*Gatherer<Integer, HashSet<Integer>, Integer> distinctGatherer =
                //Real parallel
                Gatherer.of(
                // initalizer every thread has its own copy of mutable state
                HashSet::new,
                // integrator
                (set, element, downstream) -> {
                    if(set.add(element)) {
                       return downstream.push(element);
                    }
                    return true;
                },
                //combiner to merge mutable state we need a combiner
                (s1,s2) -> {
                    s1.addAll(s2);
                return s1;
                },null

        );*/


        Gatherer<Integer, HashSet<Integer>, Integer> distinctGatherer =
                Gatherer.of(
                        HashSet::new,
                        // integrator change as every thread has its own copy so distinct will not work as told
                        (set, element, _) -> {
                          set.add(element);
                          return true;
                        },

                        (s1,s2) -> {
                            s1.addAll(s2);
                            return s1;
                        },
                        //finisher add this
                (set, downstream) -> {
                    set.stream()
                           .allMatch(downstream::push);
                }
                );

        var numsSorted = nums.stream()
                .parallel()
                .gather(distinctGatherer)
                .toList();

        System.out.println(numsSorted);
    }
}
