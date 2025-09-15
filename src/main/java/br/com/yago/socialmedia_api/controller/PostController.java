package br.com.yago.socialmedia_api.controller;

import br.com.yago.socialmedia_api.model.Media;
import br.com.yago.socialmedia_api.model.Post;
import br.com.yago.socialmedia_api.model.User;
import br.com.yago.socialmedia_api.repository.PostRepository;
import br.com.yago.socialmedia_api.repository.UserRepository;
import br.com.yago.socialmedia_api.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(@RequestParam("images") List<MultipartFile> images,
                                           @RequestParam(value = "caption", required = false) String caption,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        if (images.isEmpty() || images.size() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must upload between 1 and 5 images.");
        }

        User author = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        try {
            Post newPost = new Post();
            newPost.setCaption(caption);
            newPost.setAuthor(author);

            for (MultipartFile file : images) {
                String imageUrl = imageUploadService.uploadImage(file);

                Media media = new Media();
                media.setImageUrl(imageUrl);
                media.setPost(newPost);
                newPost.getMedia().add(media);
            }

            Post savedPost = postRepository.save(newPost);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image.", e);
        }
    }
}