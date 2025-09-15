package br.com.yago.socialmedia_api.controller;

import br.com.yago.socialmedia_api.model.User;
import br.com.yago.socialmedia_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{username}/follow")
    @Transactional
    public ResponseEntity<?> followUser(@PathVariable String username, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user not found"));

        User userToFollow = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User to follow not found"));

        if (currentUser.getId().equals(userToFollow.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot follow yourself");
        }

        userToFollow.getFollowers().add(currentUser);
        userRepository.save(userToFollow);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}/follow")
    @Transactional
    public ResponseEntity<?> unfollowUser(@PathVariable String username, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user not found"));

        User userToUnfollow = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User to unfollow not found"));

        userToUnfollow.getFollowers().remove(currentUser);
        userRepository.save(userToUnfollow);

        return ResponseEntity.ok().build();
    }
}