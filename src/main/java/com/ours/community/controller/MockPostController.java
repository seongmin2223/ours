package com.ours.community.controller;

import com.ours.community.domain.post.Post;
import com.ours.community.domain.post.PostRepository;
import com.ours.community.domain.user.User;
import com.ours.community.domain.user.UserRepository;
import com.ours.community.dto.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mock/posts")
@RequiredArgsConstructor
public class MockPostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> createMockPost(@RequestBody PostCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. ID: " + request.getUserId()));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        postRepository.save(post);
        return ResponseEntity.ok("가짜 게시글 등록 성공! 게시글 ID: " + post.getId());
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다. ID: " + userId));

        List<Post> posts = postRepository.findByUser(user);
        return ResponseEntity.ok(posts);
    }
}