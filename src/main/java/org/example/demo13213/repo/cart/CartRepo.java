package org.example.demo13213.repo.cart;

import org.example.demo13213.model.dao.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Carts, Long> {

    Optional<Carts> findByUserId(Long userId);
}

