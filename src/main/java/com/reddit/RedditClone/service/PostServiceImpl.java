package com.reddit.RedditClone.service;

import com.reddit.RedditClone.model.Comment;
import com.reddit.RedditClone.model.Image;
import com.reddit.RedditClone.model.Post;
import com.reddit.RedditClone.model.Subreddit;
import com.reddit.RedditClone.repository.PostRepository;
import com.reddit.RedditClone.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class PostServiceImpl implements  PostService{

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubredditRepository subredditRepository;

    @Autowired
    private AWSService awsService;

    @Override
    public Post savePost(Post post) {
        Timestamp timestamp = Timestamp.from(Instant.now());
//        Optional<Subreddit> subreddit = subredditRepository.findById(post.getSubredditId());
        String url = awsService.uploadFile(post.getImage());
        List<Image> images = new ArrayList<>();
        Image image = new Image();
        image.setUrls(url);
        images.add(image);
        post.setImages(images);
        post.setCreatedAt(timestamp);
        post.setUpdatedAt(timestamp);
//        post.setSubredditId(subreddit.get().getId());
        return postRepository.save(post);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getBySubRedditId(Long id) {
        return postRepository.findAllBySubredditId(id);
    }

    @Override
    public Post getPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.orElse(null);
    }

    @Override
    public void updatePostById(Post post) {
        Optional<Post> optional = postRepository.findById(post.getId());
        if(!optional.isPresent()){
            return;
        }
        Post existingPost = optional.get();
        Timestamp timestamp = Timestamp.from(Instant.now());
        Optional<Subreddit> subreddit = subredditRepository.findById(post.getSubredditId());
        String url = awsService.uploadFile(post.getImage());
        List<Image> images = new ArrayList<>();
        Image image = new Image();
        image.setUrls(url);
        images.add(image);
        existingPost.setImages(images);
        existingPost.setUpdatedAt(timestamp);
        existingPost.setSubredditId(subreddit.get().getId());
        postRepository.save(existingPost);
    }

    @Override
    public Long deleteById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()){
            postRepository.deleteById(postId);
            if(!post.get().getImages().isEmpty()) {
                String file = post.get().getImages().get(0).getUrls();
                String[] fileName = file.split("com/");
                awsService.deleteFile(fileName[1]);
            }
        }
        return post.get().getSubredditId();
    }

    @Override
    public long getKarma(Long subredditId){
        List<Post> posts = postRepository.findAllBySubredditId(subredditId);
        long karma = 0L;
        for(Post post : posts){
            karma = karma +  post.getVoteCount();
        }
        return karma;
    }

    public SortedSet<Comment> getCommentsWithoutDuplicates(int page, Set<Long> visitedComments, SortedSet<Comment> comments) {
        page++;
        Iterator<Comment> itr = comments.iterator();
        while (itr.hasNext()) {
            Comment comment = itr.next();
            boolean addedToVisitedComments = visitedComments.add(comment.getId());
            if (!addedToVisitedComments) {
                itr.remove();
                if (page != 1)
                    return comments;
            }
            if (addedToVisitedComments && !comment.getComments().isEmpty())
                getCommentsWithoutDuplicates(page, visitedComments, comment.getComments());
        }

        return comments;
    }
}
