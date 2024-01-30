package Growup.spring.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class RefreshTokenRes {
    private String newAccessToken;
}
