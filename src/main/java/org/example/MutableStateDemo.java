package org.example;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Gatherer;

public class MutableStateDemo {

    public static void main(String[] args) {

        var nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);


        Predicate<Integer> criteria = i ->  i == 4;
        class Switch {
            boolean enable;
        }
        Gatherer<Integer, Switch, Integer> dropWhileGatherer = Gatherer.ofSequential(
                // initalizer
                Switch::new,
                (sw, element, downstream) -> {
                    if(sw.enable) {
                        return downstream.push(element);
                    }else if(criteria.test(element)) {
                        sw.enable = true;
                        return downstream.push(element);
                    }else {
                        return true;
                    }
                }
        );

        var numsDropWhile = nums.stream()
                .gather(dropWhileGatherer)
                .toList();
        System.out.println(numsDropWhile);
    }


}
