package com.bank.model;

import java.util.UUID;

public class User {
    final private UUID uuid;
    final private String nickname;

    @Override
    public String toString() {
        return "User{" +
                "UUID=" + uuid +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public UUID getUUID() {
        return this.uuid;
    }


    public String getNickname() {
        return this.nickname;
    }

    public User(String nickname) {
        this.uuid = UUID.randomUUID();
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname cannot be null or empty");
        }
        this.nickname = nickname;
    }


}
