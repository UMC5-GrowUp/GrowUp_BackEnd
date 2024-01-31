package Growup.spring.growRoom.controller;

import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.growRoom.dto.AddGrowRoomDtoReq;
import Growup.spring.growRoom.dto.GrowRoomDtoRes;
import Growup.spring.growRoom.dto.UpdateGrowRoomDtoReq;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.service.GrowRoomService;
import Growup.spring.security.JwtProvider;
import Growup.spring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor    //생성자 자동 생성
@RestController             //http response body에 객체 데이터를 json 반환
@RequestMapping("/growup")
public class GrowRoomApiController {

    private final GrowRoomService growRoomService;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 목록 조회 - api 응답통일 - 에러핸들러 해야함
     */
    @GetMapping("/growrooms")
    public ApiResponse<List<GrowRoomDtoRes>> findAllGrowRooms(){
        List<GrowRoomDtoRes> growRooms = growRoomService.findAll()
                .stream()
                .map(GrowRoomDtoRes::new)
                .collect(Collectors.toList());  //음..?

        return ApiResponse.onSuccess(growRooms);
    }

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 생성
     */
    @PostMapping("/growrooms")
    public ApiResponse<GrowRoomDtoRes> addGrowRoom(@RequestBody AddGrowRoomDtoReq request){
        GrowRoom growRoom = growRoomService.save(request);

        return ApiResponse.onSuccess(new GrowRoomDtoRes(growRoom));
    }

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 {id} 조회
     */
    @GetMapping("/growrooms/{id}")
    public ApiResponse<GrowRoomDtoRes> findGrowRoom(@PathVariable Long id){
        GrowRoom growRoom = growRoomService.findById(id);

        return ApiResponse.onSuccess(new GrowRoomDtoRes(growRoom));
    }

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 {id} 삭제
     */
    @DeleteMapping("/growrooms/{id}")
    public ApiResponse<SuccessStatus> deleteGrowRoom(@PathVariable Long id){
        growRoomService.delete(id);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 {id} 수정
     */
    @PutMapping("/growrooms/{id}")
    public ApiResponse<GrowRoomDtoRes> updateGrowRoom(@PathVariable Long id, @RequestBody UpdateGrowRoomDtoReq request) {
        GrowRoom updatedGrowRoom = growRoomService.update(id, request);

        return ApiResponse.onSuccess(new GrowRoomDtoRes(updatedGrowRoom));
    }
}
