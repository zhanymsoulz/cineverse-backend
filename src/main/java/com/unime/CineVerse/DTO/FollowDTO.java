package com.unime.CineVerse.DTO;

import lombok.Data;

@Data
public class FollowDTO {
    private Long followedUserId;
    private Long followingUserId;
}
