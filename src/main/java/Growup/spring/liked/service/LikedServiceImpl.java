package Growup.spring.liked.service;

import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.handler.LikedHandler;
import Growup.spring.constant.handler.ParticipateHandler;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.repository.GrowRoomRepository;
import Growup.spring.liked.model.Liked;
import Growup.spring.liked.repository.LikedRepository;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.repository.ParticipateRepository;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class LikedServiceImpl implements LikedService{
    private final LikedRepository likedRepository;
    private final UserRepository userRepository;
    private final GrowRoomRepository growRoomRepository;
    private final ParticipateRepository participateRepository;

    //좋아요 기능 메서드
//    @Override
//    public Liked doLike( Long userId ,Long growRoomId){
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
//        GrowRoom growRoom = growRoomRepository.findById(growRoomId)
//                .orElseThrow(()-> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));
//

        // liked가 생성되었는지 확인 후 비어있다면 liked 생성, 이미 존재한다면 null 리턴
//        if (likedRepository.findByUserAndGrowRoom(user, growRoom).isEmpty())
//            return Liked.builder()
//                    .user(user)
//                    .growRoom(growRoom)
//                    .build();
//
//        else return null;
//        Optional<Liked> existLiked = likedRepository.findByUserAndGrowRoom(user, growRoom);
//        Liked liked = Liked.builder() // liked 안에 user growroom 저장
//                .user(user)
//                .growRoom(growRoom)
//                .build();
//
//        if(existLiked.isEmpty()) { //비어있다면, liked 저장
//            likedRepository.save(liked);
//        }
//        return liked;
//    }

    //좋아요 취소 기능
//    @Override
//    public boolean unLike (Long userId ,Long growRoomId){
//        User user = userRepository.findById(userId)
//                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
//        GrowRoom growRoom = growRoomRepository.findById(growRoomId)
//                .orElseThrow(()-> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));
//
//        Optional<Liked> existLiked = likedRepository.findByUserAndGrowRoom(user ,growRoom);
//
//        if(existLiked.isPresent()){
//            likedRepository.delete(existLiked.get()); // 여기서 삭제를 할때 삭제가 되지 않는경우가 발생하여 블로그 사용하여 해결
//            return true;
//        }
//        else {
//            return false;
//        }
//        Liked existLiked = likedRepository.findByUserAndGrowRoom(user, growRoom)
//                .orElse;
//        if (existLiked){
//            likedRepository.delete(existLiked);
//        }
//    }

    // 좋아요 생성 or 취소
    @Override
    public boolean doOrUnLiked(Long userId, Long growRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        GrowRoom growRoom = growRoomRepository.findById(growRoomId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));

        Optional<Liked> liked = likedRepository.findByUserAndGrowRoom(user, growRoom);

        if (liked.isPresent()) {
            Liked.builder()
                    .user(user)
                    .growRoom(growRoom)
                    .build();
            return true;
        }
        else return false;
    }

    //인기글 확인 api
    @Override
    public boolean likecount(Long growRoomId){
        growRoomRepository.findById(growRoomId)
                .orElseThrow(()-> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND)); //growroomid로 찾아서 저장

        List<Liked> likeCount = likedRepository.findByGrowRoomId(growRoomId); // growroom를 기준으로 likelist찾아서 likecount에 저장
        if (likeCount.size()>=2){
            return true; //100개 이상이면 true 반환, 테스트를 위해 좋아요 2개 이상으로 임시 수정
        }
        else {
            return false;
        }
    }

    //라이브룸 내에 (방장만) 좋아요하는 기능
    @Override
    public Participate likeToParticipate (Long userId ,Long growRoomId , Long participateId){
        User user = userRepository.findById(userId).orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND)); //user 찾아옴

        GrowRoom growRoom = growRoomRepository.findById(growRoomId).orElseThrow(()-> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND)); // growroom 찾아옴

        Participate participate = participateRepository.findById(participateId).orElseThrow(()-> new ParticipateHandler(ErrorStatus.PARTICIPATE_NOT_FOUND)); //참가자 찾아옴

        Integer liked = participate.getLiked(); //liked를 가져옴


        //방장이라면 좋아요 및 취소 기능
        if(user.getId() == growRoom.getId()){
            if(liked ==0){
                liked = 1;
            }
            else {
                liked = 0;
            }

        }
        //방장이아니라면 error
        else {
             throw new LikedHandler(ErrorStatus.PARTICIPATE_NO_AUTHORIZATION);
        }
        participate.setLiked(liked);
        return participate;
    }

    // growroom 좋아요 조회
    @Override
    public boolean isGrowRoomLikedByUser(Long userId, Long growRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        GrowRoom growRoom = growRoomRepository.findById(growRoomId)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));
        return likedRepository.existsLikedByUserAndGrowRoom(user, growRoom);
    }
}
