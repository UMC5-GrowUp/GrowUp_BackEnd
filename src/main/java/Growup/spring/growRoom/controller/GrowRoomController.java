package Growup.spring.growRoom.controller;

import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.growRoom.dto.GrowRoomDtoReq;
import Growup.spring.growRoom.dto.GrowRoomDtoRes;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.service.GrowRoomService;
import Growup.spring.liked.service.LikedService;
import Growup.spring.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor    //생성자 자동 생성
@RestController             //http response body에 객체 데이터를 json 반환
@RequestMapping("/growup")
public class GrowRoomController {
    // 그로우룸 GrowRoom
    private final GrowRoomService growRoomService;
    private final JwtProvider jwtProvider;
    private final LikedService likedService;

    /**
     * 24.02.02 작성자 : 류기현
     * 그로우룸 목록 조회
     */
    @GetMapping("/growroom/under")
    public ApiResponse<List<GrowRoomDtoRes.GrowRoomAllDtoRes>> findAllGrowRooms(@RequestParam(name = "filter", defaultValue = "전체") String filter,
                                                                                @RequestParam(name = "categoryDetail", defaultValue = "전체") String categoryDetail,
                                                                                @RequestParam(name = "period", defaultValue = "전체") String period,
                                                                                @RequestParam(name = "status", defaultValue = "전체") String status,
                                                                                @RequestParam(name = "search", defaultValue = "") String search){
        Long userID = jwtProvider.getUserID();
        List<GrowRoomDtoRes.GrowRoomAllDtoRes> growRooms = growRoomService.findByFilter(filter, categoryDetail, period, status, userID, search)
                .stream()
                .map(growRoom -> new GrowRoomDtoRes.GrowRoomAllDtoRes(growRoom, likedService.isGrowRoomLikedByUser(userID, growRoom.getId())))
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(growRooms);
    }

    /**
     * 24.02.16 작성자 : 류기현
     * 그로우룸 인기글 조회
     */
    @GetMapping("/growroom/upper")
    public ApiResponse<List<GrowRoomDtoRes.GrowRoomAllDtoRes>> findHotGrowRooms(){
        Long userID = jwtProvider.getUserID();
        List<GrowRoomDtoRes.GrowRoomAllDtoRes> growRooms = growRoomService.findHot()
                .stream()
                .map(growRoom -> new GrowRoomDtoRes.GrowRoomAllDtoRes(growRoom, likedService.isGrowRoomLikedByUser(userID, growRoom.getId())))
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(growRooms);
    }

    /**
     * 24.02.17 작성자 : 류기현
     * 토큰 X 그로우룸 인기글 조회
     */
    @GetMapping("/upperAbleNoToken")
    public ApiResponse<List<GrowRoomDtoRes.GrowRoomAllDtoRes>> findHotGrowRoomsNoToken(){
        List<GrowRoomDtoRes.GrowRoomAllDtoRes> growRooms = growRoomService.findHot()
                .stream()
                .map(growRoom -> new GrowRoomDtoRes.GrowRoomAllDtoRes(growRoom, false))
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(growRooms);
    }

    /**
     * 24.02.17 작성자 : 류기현
     * 라이브룸 목록 조회
     */
    @GetMapping("/growroom/liveRoom/under")
    public ApiResponse<List<GrowRoomDtoRes.GrowRoomAllDtoRes>> findAllGrowRooms(@RequestParam(name = "filter", defaultValue = "전체") String filter){
        Long userID = jwtProvider.getUserID();
        List<GrowRoomDtoRes.GrowRoomAllDtoRes> growRooms = growRoomService.findByFilter(filter, "전체", "전체", "전체", userID, "")
                .stream()
                .map(growRoom -> new GrowRoomDtoRes.GrowRoomAllDtoRes(growRoom, likedService.isGrowRoomLikedByUser(userID, growRoom.getId())))
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(growRooms);
    }


    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 생성
     */
    @PostMapping("/growroom")
    public ApiResponse<GrowRoomDtoRes.GrowRoomViewDtoRes> addGrowRoom(@RequestBody GrowRoomDtoReq.AddGrowRoomDtoReq request){
        Long userId = jwtProvider.getUserID();
        GrowRoom growRoom = growRoomService.save(userId, request);

        return ApiResponse.onSuccess(new GrowRoomDtoRes.GrowRoomViewDtoRes(growRoom));
    }

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 {id} 조회
     */
    @GetMapping("/growroom/{id}")
    public ApiResponse<GrowRoomDtoRes.GrowRoomViewDtoRes> findGrowRoom(@PathVariable Long id){
        Long userID = jwtProvider.getUserID();
        GrowRoom growRoom = growRoomService.findById(id);
        growRoomService.updateView(userID, id); // 그로우룸 조회시 조회수 증가

        return ApiResponse.onSuccess(new GrowRoomDtoRes.GrowRoomViewDtoRes(growRoom));
    }

    /**
     * 24.02.04 작성자 : 류기현
     * 그로우룸 {id} 수정
     */
    @PutMapping("/growroom/{id}")
    public ApiResponse<GrowRoomDtoRes.GrowRoomViewDtoRes> updateGrowRoom(@PathVariable Long id, @RequestBody GrowRoomDtoReq.UpdateGrowRoomDtoReq request) {
        Long userID = jwtProvider.getUserID();
        GrowRoom updatedGrowRoom = growRoomService.update(userID, id, request);

        return ApiResponse.onSuccess(new GrowRoomDtoRes.GrowRoomViewDtoRes(updatedGrowRoom));
    }

    /**
     * 24.02.04 작성자 : 류기현
     * 그로우룸 {id} 삭제 - 수정을 통해 상태변경 후 일정 시간 이후 삭제
     */
    @DeleteMapping("/growroom/{id}")
    public ApiResponse<SuccessStatus> deleteGrowRoom(@PathVariable Long id){
        Long userID = jwtProvider.getUserID();
        growRoomService.deleteTemp(userID, id);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
}
