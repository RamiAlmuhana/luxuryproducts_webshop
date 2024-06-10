package com.example.gamewebshop.Repositorys;

import com.example.gamewebshop.models.Product.Enums.Sizes;
import com.example.gamewebshop.models.Product.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

    Optional<Size> findSizeBySize(Sizes size);
}
