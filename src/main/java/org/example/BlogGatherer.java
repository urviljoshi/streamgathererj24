package org.example;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

public class BlogGatherer {
    private BlogGatherer() {} // not to be init

    public static <K> Gatherer<BlogPost, Map<K, List<BlogPost>>, Map.Entry<K,List<BlogPost>>> createSampleBlogPosts(
            Function<? super BlogPost, ? extends K> keyExtractor,
            Comparator<? super BlogPost> comparator,
            int limit
    ) {
return Gatherer.of(
        //  Initialize with an empty map to store our grouped items
            HashMap<K,List<BlogPost>>::new,
        // Process each blog post
        (map,post,downstream) -> {
                // group post based
                K key = keyExtractor.apply(post);
                map.computeIfAbsent(key, k -> new ArrayList<>()).add(post);
                return true;
        },
        // Combiner for parallel streams - just use the first map in this simple case
        (map1, map2) -> map1,
        // When all posts have been processed, emit the results
        (map,downstream)->
                // Sort the posts and limit to the specified number
                map.forEach((key, post) -> {
                    List<BlogPost> limitedPost = post.stream().sorted(comparator)
                            .limit(limit)
                            .collect(Collectors.toList());
                    // Emit a Map.Entry with the key and limited posts
                    downstream.push(Map.entry(key, limitedPost));
                })

        );
    }
}
