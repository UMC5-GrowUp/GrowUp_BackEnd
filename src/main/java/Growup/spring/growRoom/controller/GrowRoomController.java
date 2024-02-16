package Growup.spring.growRoom.controller;

import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.growRoom.converter.GrowRoomConverter;
import Growup.spring.growRoom.dto.GrowRoomDtoReq;
import Growup.spring.growRoom.dto.GrowRoomDtoRes;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import Growup.spring.growRoom.service.GrowRoomService;
import Growup.spring.liked.service.LikedService;
import Growup.spring.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor    //생성자 자동 생성
@RestController             //http response body에 객체 데이터를 json 반환
@RequestMapping("/growup/growroom")
public class GrowRoomController {
    // 그로우룸 GrowRoom
    private final GrowRoomService growRoomService;
    private final JwtProvider jwtProvider;
    private final LikedService likedService;

    /**
     * 24.02.02 작성자 : 류기현
     * 그로우룸 목록 조회
     * 그로우룸 Dto res 수정
     */
    @GetMapping("/under")
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
     * 24.02.02 작성자 : 류기현
     * 그로우룸 목록 조회
     * 그로우룸 Dto res 수정
     */
    @GetMapping("/upper")
    public ApiResponse<List<GrowRoomDtoRes.GrowRoomAllDtoRes>> findHotGrowRooms(){
        Long userID = jwtProvider.getUserID();
        List<GrowRoomDtoRes.GrowRoomAllDtoRes> growRooms = growRoomService.findHot()
                .stream()
                .map(growRoom -> new GrowRoomDtoRes.GrowRoomAllDtoRes(growRoom, likedService.isGrowRoomLikedByUser(userID, growRoom.getId())))
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(growRooms);
    }



    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 생성
     */
    @PostMapping("")
    public ApiResponse<GrowRoomDtoRes.GrowRoomViewDtoRes> addGrowRoom(@RequestBody GrowRoomDtoReq.AddGrowRoomDtoReq request){
        GrowRoom growRoom = growRoomService.save(request);

        return ApiResponse.onSuccess(new GrowRoomDtoRes.GrowRoomViewDtoRes(growRoom));
    }

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 {id} 조회
     */
    @GetMapping("/{id}")
    public ApiResponse<GrowRoomDtoRes.GrowRoomViewDtoRes> findGrowRoom(@PathVariable Long id){
        GrowRoom growRoom = growRoomService.findById(id);
        growRoomService.updateView(id); // 그로우룸 조회시 조회수 증가

        return ApiResponse.onSuccess(new GrowRoomDtoRes.GrowRoomViewDtoRes(growRoom));
    }

    /**
     * 24.02.04 작성자 : 류기현
     * 그로우룸 {id} 삭제 - 수정을 통해 상태변경 후 일정 시간 이후 삭제
     */
    @DeleteMapping("/{id}")
    public ApiResponse<SuccessStatus> deleteGrowRoom(@PathVariable Long id){
        growRoomService.deleteTemp(id);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    /**
     * 24.02.04 작성자 : 류기현
     * 그로우룸 {id} 수정
     */
    @PutMapping("/{id}")
    public ApiResponse<GrowRoomDtoRes.GrowRoomViewDtoRes> updateGrowRoom(@PathVariable Long id, @RequestBody GrowRoomDtoReq.UpdateGrowRoomDtoReq request) {
        GrowRoom updatedGrowRoom = growRoomService.update(id, request);

        return ApiResponse.onSuccess(new GrowRoomDtoRes.GrowRoomViewDtoRes(updatedGrowRoom));
    }
}
