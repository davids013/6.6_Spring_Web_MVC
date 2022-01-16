package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Stub
@Repository
public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    public List<Post> all() {
        if (posts.isEmpty())
            return new ArrayList<>();
        return posts
                .values()
                .stream()
                .filter(x -> !x.getRemoved())
                .collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        final Post post = posts.get(id);
        if (post != null && !post.getRemoved()) return Optional.of(post);
        throw new NotFoundException("Can't read. There is no post with id " + id);
    }

    public Post save(Post post) {
        final long id = post.getId();
        if (id == 0) {
            final long nextId = counter.incrementAndGet();
            final Post newPost = new Post(nextId, post.getContent());
            posts.put(nextId, newPost);
            return newPost;
        } else {
            if (posts.containsKey(id) && !posts.get(id).getRemoved()) {
                posts.put(id, post);
                return post;
            }
        }
        throw new NotFoundException("Can't override. There is no post with id " + id);
    }

    public void removeById(long id) {
        final Post post = posts.get(id);
        if (post != null && !post.getRemoved()) {
            post.setRemoved(true);
        } else throw new NotFoundException("Can't remove. There is no post with id " + id);
    }
}
