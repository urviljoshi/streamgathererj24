package org.example;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

public class BlogGatherer {
    private BlogGatherer() {} // not to be init

    private static <K> Gatherer<BlogPost, Map<K, List<BlogPost>>, Map.Entry<K,List<BlogPost>>> createSampleBlogPosts(
            Function<? super BlogPost, ? extends K> keyExtractor,
            int limit,
            Comparator<? super BlogPost> comparator
    ) {
return Gatherer.of(
        // init with empty map
            HashMap<K,List<BlogPost>>::new,

        (map,post,downstream) -> {
                // group post based
                K key = keyExtractor.apply(post);
                map.computeIfAbsent(key, k -> new ArrayList<>()).add(post);
                return true;
        },


        ,

        );
    }
}
