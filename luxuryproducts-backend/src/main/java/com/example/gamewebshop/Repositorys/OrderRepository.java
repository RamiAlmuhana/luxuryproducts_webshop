package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.CustomUser;
import com.example.gamewebshop.models.PlacedOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<PlacedOrder, Long> {
    Optional<List<PlacedOrder>> findByUserId(long id);
    Optional<List<PlacedOrder>> findAllByUser(CustomUser customUser);

}
