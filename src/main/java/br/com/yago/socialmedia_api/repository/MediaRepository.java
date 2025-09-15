package br.com.yago.socialmedia_api.repository;

import br.com.yago.socialmedia_api.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {}