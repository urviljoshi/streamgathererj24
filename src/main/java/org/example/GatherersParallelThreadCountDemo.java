package org.example;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Gatherer;
import java.util.stream.IntStream;

public class GatherersParallelThreadCountDemo {
    public static void main(String[] args) {

        //squentialGathererThreadCountDemo();
        parallelGathererThreadCountDemo();

    }

    private static void squentialGathererThreadCountDemo() {
        class State {
            Set<String> upstreamThreads = new HashSet<>();
            Set<String> gathererThreads = new HashSet<>();
            Set<String> downstreamThreads = new HashSet<>();
        }

        var res = IntStream.range(0, 10_000_000).
                mapToObj(x -> Thread.currentThread().getName()).
                parallel().
                distinct().
                gather(Gatherer.<String, State, State>ofSequential(State::new,
                        ((state,element,_) ->{
                           state.upstreamThreads.add(element);
                           state.gathererThreads.add(Thread.currentThread().getName());
                           return true;
                        }),
                        ((state, downstream) -> {
                          IntStream.range(0,100).
                            forEach(_ ->downstream.push(state));
                        })
                )).
                peek(state-> state.downstreamThreads.add(Thread.currentThread().getName()))
                .toList();

        System.out.println( "upstreamThreads count ::" + res.getFirst().upstreamThreads.size());
        System.out.println( "gathererThreads count ::" + res.getFirst().gathererThreads.size());
        System.out.println( "downstreamThreads count ::" + res.getFirst().downstreamThreads.size());
    }

    private static void parallelGathererThreadCountDemo() {
        class State {
            Set<String> upstreamThreads = new HashSet<>();
            Set<String> gathererThreads = new HashSet<>();
            Set<String> downstreamThreads = new HashSet<>();
        }

        var res = IntStream.range(0, 10_000_000).
                mapToObj(x -> Thread.currentThread().getName()).
                parallel().
                distinct().
                gather(Gatherer.<String, State, State>of(State::new,
                        ((state,element,_) ->{
                            state.upstreamThreads.add(element);
                            state.gathererThreads.add(Thread.currentThread().getName());
                            return true;
                        }),
                        (s1,s2) -> {
                            s1.upstreamThreads.addAll(s2.upstreamThreads);
                            s1.gathererThreads.addAll(s2.gathererThreads);
                            s1.downstreamThreads.addAll(s2.downstreamThreads);
                            return s1;
                        },
                        ((state, downstream) -> {
                            IntStream.range(0,100).
                                    forEach(_ ->downstream.push(state));
                        })
                )).
                peek(state-> state.downstreamThreads.add(Thread.currentThread().getName()))
                .toList();

        System.out.println( "upstreamThreads count ::" + res.getFirst().upstreamThreads.size());
        System.out.println( "gathererThreads count ::" + res.getFirst().gathererThreads.size());
        System.out.println( "downstreamThreads count ::" + res.getFirst().downstreamThreads.size());
    }
}
