package com.weslley.demo_crud.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class RefreshTokenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String refreshToken;
    @Column(nullable = false)
    private Instant expiration;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

    public RefreshTokenModel() {
        super();
    }

    public RefreshTokenModel(String refreshToken, Instant expiration, UserModel user) {
        this.refreshToken = refreshToken;
        this.expiration = expiration;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
