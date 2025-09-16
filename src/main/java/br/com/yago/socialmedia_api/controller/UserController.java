package br.com.yago.socialmedia_api.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import br.com.yago.socialmedia_api.dto.PostResponse;
import br.com.yago.socialmedia_api.dto.UserProfileResponse;
import br.com.yago.socialmedia_api.model.Post;
import br.com.yago.socialmedia_api.model.User;
import br.com.yago.socialmedia_api.repository.PostRepository;
import br.com.yago.socialmedia_api.repository.UserRepository;
import br.com.yago.socialmedia_api.service.ImageUploadService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ImageUploadService imageUploadService; // <-- INJETE O SERVIÇO DE UPLOAD

	// --- NOVO ENDPOINT ADICIONADO ABAIXO ---
	@PostMapping("/profile-picture")
	@Transactional
	public ResponseEntity<?> updateProfilePicture(@RequestParam("file") MultipartFile file,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Por favor, selecione um arquivo para upload.");
		}

		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

		try {
			Map uploadResult = imageUploadService.uploadImage(file);
			String fileUrl = (String) uploadResult.get("secure_url");

			currentUser.setProfilePictureUrl(fileUrl);
			userRepository.save(currentUser);

			return ResponseEntity.ok(Map.of("profilePictureUrl", fileUrl));
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Não foi possível fazer o upload da imagem.", e);
		}
	}

	// --- MÉTODO GET USER PROFILE MODIFICADO ---
	@GetMapping("/{username}")
	@Transactional(readOnly = true)
	public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String username,
			@AuthenticationPrincipal UserDetails userDetails) {
		// Encontra o usuário do perfil que está sendo visitado
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		// Encontra o usuário que está logado e fazendo a requisição
		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user not found"));

		// Verifica se o usuário logado está na lista de seguidores do perfil visitado
		boolean isFollowing = user.getFollowers().contains(currentUser);

		// Retorna a resposta incluindo o novo campo 'isFollowing'
		return ResponseEntity.ok(new UserProfileResponse(user, isFollowing));
	}

	// --- O RESTO DA CLASSE PERMANECE IGUAL ---

	@PostMapping("/{username}/follow")
	@Transactional
	public ResponseEntity<?> followUser(@PathVariable String username,
			@AuthenticationPrincipal UserDetails userDetails) {
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
	public ResponseEntity<?> unfollowUser(@PathVariable String username,
			@AuthenticationPrincipal UserDetails userDetails) {
		User currentUser = userRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user not found"));
		User userToUnfollow = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User to unfollow not found"));
		userToUnfollow.getFollowers().remove(currentUser);
		userRepository.save(userToUnfollow);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{username}/posts")
	@Transactional(readOnly = true)
	public ResponseEntity<Page<PostResponse>> getUserPosts(@PathVariable String username, Pageable pageable) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

		Page<Post> posts = postRepository.findByAuthorOrderByCreatedAtDesc(user, pageable);
		Page<PostResponse> response = posts.map(PostResponse::new);

		return ResponseEntity.ok(response);
	}
}