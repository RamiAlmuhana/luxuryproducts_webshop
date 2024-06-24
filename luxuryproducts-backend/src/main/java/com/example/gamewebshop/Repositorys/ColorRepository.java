package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.Product.Color;
import com.example.gamewebshop.models.Product.Enums.Colors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {

    Optional<Color> findColorByName(Colors name);
}
