package Growup.spring.liked.service;


import Growup.spring.liked.model.Liked;
import Growup.spring.participate.model.Participate;


public interface LikedService {
//        Liked doLike(Long userId , Long growRoomId);
//
//        boolean unLike(Long userId ,Long growRoomId);

        boolean doOrUnLiked(Long userId, Long growRoomId);

        boolean likeCount(Long growRoomId);

        Participate likeToParticipate(Long userId , Long growRoomId , Long participateId);

        boolean isGrowRoomLikedByUser(Long userId, Long growRoomId);
}
