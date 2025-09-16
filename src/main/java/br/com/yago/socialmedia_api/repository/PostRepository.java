package br.com.yago.socialmedia_api.repository;

import br.com.yago.socialmedia_api.model.Post;
import br.com.yago.socialmedia_api.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importe a List

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);

    // --- NOVO MÉTODO ADICIONADO ABAIXO ---
    Page<Post> findByAuthorInOrderByCreatedAtDesc(List<User> authors, Pageable pageable);
}