package br.com.yago.socialmedia_api.repository;

import br.com.yago.socialmedia_api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {}