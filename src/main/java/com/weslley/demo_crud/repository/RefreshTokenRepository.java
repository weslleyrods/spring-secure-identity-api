package com.weslley.demo_crud.repository;

import com.weslley.demo_crud.model.RefreshTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel,Long> {
    Optional<RefreshTokenModel> findByRefreshToken(String refreshToken);
}
