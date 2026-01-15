package com.weslley.ssi_api.repository;

import com.weslley.ssi_api.model.RefreshTokenModel;
import com.weslley.ssi_api.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel,Long> {
    Optional<RefreshTokenModel> findByRefreshToken(String refreshToken);

    @Transactional
    void deleteByUser(Optional<UserModel> user);
}
