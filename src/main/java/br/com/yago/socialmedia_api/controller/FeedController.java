package br.com.yago.socialmedia_api.controller;

import br.com.yago.socialmedia_api.dto.PostResponse;
import br.com.yago.socialmedia_api.model.Post;
import br.com.yago.socialmedia_api.model.User;
import br.com.yago.socialmedia_api.repository.PostRepository;
import br.com.yago.socialmedia_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Page<PostResponse>> getFeed(Pageable pageable, @AuthenticationPrincipal UserDetails userDetails) {
        // 1. Encontra o usuário que está fazendo a requisição
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        // 2. Pega a lista de usuários que ele segue
        List<User> following = new ArrayList<>(currentUser.getFollowing());
        if (following.isEmpty()) {
            // Se não segue ninguém, retorna uma página vazia
            return ResponseEntity.ok(Page.empty(pageable));
        }

        // 3. Usa o novo método do repositório para buscar os posts dos usuários seguidos
        Page<Post> feedPosts = postRepository.findByAuthorInOrderByCreatedAtDesc(following, pageable);

        // 4. Mapeia para o DTO de resposta
        Page<PostResponse> response = feedPosts.map(PostResponse::new);

        return ResponseEntity.ok(response);
    }
}