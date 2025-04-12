package org.example;

import java.util.List;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

public class IntegratorDemo {

    public static void main(String[] args) {
        var count = List.of("abc","def","ghi","jkl","mno","pqr","tuv","wxyz");


        // most useless gatherer
        //Gatherer<String, ?, ?> dummyGatherer = () -> (state, element, downstream) -> true;

        Gatherer<String, ?, String> filterGatherer = Gatherer.of((_, element, downstream) -> {
                downstream.push(element.toUpperCase());
                //we can push multiple elements
                //return downstream.push(element.toUpperCase());
                // stream will not more elements
                return true;
                    }
                );

        var countFiltered = count.stream()
                .gather(filterGatherer)
                .toList();
        System.out.println(countFiltered);

    }

}
