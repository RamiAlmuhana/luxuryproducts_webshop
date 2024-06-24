package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.Product.Enums.Fit;
import com.example.gamewebshop.models.Product.SizeAndFit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeAndFitRepository extends JpaRepository<SizeAndFit, Long> {
    Optional<SizeAndFit> findSizeAndFitByFit(Fit fit);
}
