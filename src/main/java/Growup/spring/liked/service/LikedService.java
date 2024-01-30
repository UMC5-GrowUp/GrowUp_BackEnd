package Growup.spring.liked.service;


import Growup.spring.liked.model.Liked;
import Growup.spring.participate.model.Participate;


public interface LikedService {
        Liked doLike(Long userId , Long growRoomId );

        boolean unLike( Long userId ,Long growRoomId );

        boolean likecount(Long growRoomId);

        Participate likeToParticipate(Long userId , Long growRoomId , Long participateId);
}
