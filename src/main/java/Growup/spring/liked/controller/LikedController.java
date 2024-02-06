package Growup.spring.liked.controller;


import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.liked.converter.LikedConverter;
import Growup.spring.liked.dto.LikedDtoRes;
import Growup.spring.liked.model.Liked;
import Growup.spring.liked.repository.LikedRepository;
import Growup.spring.liked.service.LikedService;

import Growup.spring.participate.model.Participate;
import Growup.spring.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;




@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/growup/users")
public class LikedController {
        private final LikedService likedService;
        private final LikedRepository likedRepository;
        private final JwtProvider jwtProvider;



        //그로우룸 & 라이브룸 좋아요 설정
        @PostMapping("/liked")
        public ApiResponse<LikedDtoRes.LikedRes> doLike(@RequestParam (name = "growRoomId")Long growRoom ) {
                Long user = jwtProvider.getUserID();
                Liked liked = likedService.doLike(user , growRoom );
                return ApiResponse.onSuccess(LikedConverter.tolikedRes(liked));
        }

        //그로우룸 & 라이브룸 좋아요 설정 해제
        @PostMapping("/unliked")
        public ApiResponse<LikedDtoRes.unLikedRes> unLike(@RequestParam (name = "growRoomId") Long growRoom ) {
                Long user = jwtProvider.getUserID();
                boolean result = likedService.unLike(user,growRoom );

                if (result){
                        return ApiResponse.onSuccess(LikedConverter.tounlikedRes(growRoom));
                }
                else {
                        return ApiResponse.onFailure(ErrorStatus.LIKED_NOT_FOUND.getCode(), ErrorStatus.LIKED_NOT_FOUND.getMessage(), null);
                }
        }

        //좋아요 갯수를 기반으로 100개이상은 인기글로 나타냄
        @GetMapping("/likecount")
        public ApiResponse<String> likecount(@RequestParam (name = "growRoomId")Long growRoom ){
                boolean hot = likedService.likecount(growRoom);
                if(hot == true){
                        return ApiResponse.onSuccess("인기글 입니다.");
                }
                else {
                        return ApiResponse.onFailure(ErrorStatus.GROWROOM_NOT_HOT.getCode(), ErrorStatus.GROWROOM_NOT_HOT.getMessage(),null);
                }
        }

        //liveRoom내에 (방장만)좋아요 설정
        @PostMapping("/liveRoomliked")
        public ApiResponse<LikedDtoRes.LiveRoomlikeRes> liveRoomLike(@RequestParam (name = "growRoomId") Long growRoomId ,
                                                                     @RequestParam (name = "participateId")Long participateId ){
                Long userId = jwtProvider.getUserID();
                Participate participate =likedService.likeToParticipate(userId,growRoomId,participateId);
                return ApiResponse.onSuccess(LikedConverter.toliveRoomlikeRes(participate));
        }
}

