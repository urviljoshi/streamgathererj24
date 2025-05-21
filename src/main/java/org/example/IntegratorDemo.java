package org.example;

import java.util.List;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

public class IntegratorDemo {

    public static void main(String[] args) {
        var count = List.of("abc","def","ghi","jkl","mno","pqr","tuv","wxyz");

        Gatherer<String, ?, String> filterGatherer =Gatherer.of( (state,  element, downstream) ->{
            if(element.length()>3){
                downstream.push(element);
            }
            return true;
        });

        var countFiltered = count.stream()
                .gather(filterGatherer)
                .toList();
        System.out.println(countFiltered);

    }

}
