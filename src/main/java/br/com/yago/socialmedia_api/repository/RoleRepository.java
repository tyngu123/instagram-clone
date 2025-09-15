package br.com.yago.socialmedia_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.yago.socialmedia_api.model.Role;
import br.com.yago.socialmedia_api.model.RoleName;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}