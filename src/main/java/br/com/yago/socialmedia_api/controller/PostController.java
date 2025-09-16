package br.com.yago.socialmedia_api.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import br.com.yago.socialmedia_api.model.Media;
import br.com.yago.socialmedia_api.model.Post;
import br.com.yago.socialmedia_api.model.User;
import br.com.yago.socialmedia_api.repository.PostRepository;
import br.com.yago.socialmedia_api.repository.UserRepository;
import br.com.yago.socialmedia_api.service.ImageUploadService;

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
    public ResponseEntity<Post> createPost(@RequestParam("mediaFiles") List<MultipartFile> mediaFiles,
                                           @RequestParam(value = "caption", required = false) String caption,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        // ... (código de validação e busca do autor) ...
        User author = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        try {
            Post newPost = new Post();
            newPost.setCaption(caption);
            newPost.setAuthor(author);

            for (MultipartFile file : mediaFiles) {
                Map uploadResult = imageUploadService.uploadImage(file);
                String mediaUrl = (String) uploadResult.get("secure_url");
                String resourceType = (String) uploadResult.get("resource_type");

                Media media = new Media();
                media.setMediaUrl(mediaUrl);
                media.setPost(newPost);

                if ("video".equals(resourceType)) {
                    media.setMediaType(Media.MediaType.VIDEO);
                    // Pega a URL do thumbnail gerado
                    List<Map> eager = (List<Map>) uploadResult.get("eager");
                    if (eager != null && !eager.isEmpty()) {
                        media.setThumbnailUrl((String) eager.get(0).get("secure_url"));
                    }
                } else {
                    media.setMediaType(Media.MediaType.IMAGE);
                }
                newPost.getMedia().add(media);
            }

            Post savedPost = postRepository.save(newPost);
            return new ResponseEntity<>(savedPost, HttpStatus.CREATED);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload media.", e);
        }
    }
}