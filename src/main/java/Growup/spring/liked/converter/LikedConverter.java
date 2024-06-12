package Growup.spring.liked.converter;

import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.liked.dto.LikedDtoRes;
import Growup.spring.liked.model.Liked;
import Growup.spring.participate.model.Participate;
import Growup.spring.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LikedConverter {

    //생성 날짜 반환
//    public static LikedDtoRes.LikedRes tolikedRes(Liked liked){
//        return LikedDtoRes.LikedRes.builder()
//                .createdAt(liked.getCreatedAt())
//                .build();
//    }
//
//    public static LikedDtoRes.unLikedRes tounlikedRes(Long growRoom){
//        return LikedDtoRes.unLikedRes.builder()
//                .growRoomId(growRoom)
//                .build();
//    }
//
//
    public static Liked convertToLiked(User user, GrowRoom growRoom){
        return Liked.builder()
                .user(user)
                .growRoom(growRoom)
                .build();
    }
    //
    public static LikedDtoRes.LiveRoomlikeRes toliveRoomlikeRes(Participate liked){
        return LikedDtoRes.LiveRoomlikeRes.builder()
                .createdAt(liked.getCreatedAt())
                .build();
    }
}
