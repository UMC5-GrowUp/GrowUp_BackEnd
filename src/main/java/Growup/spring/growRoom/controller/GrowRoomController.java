package Growup.spring.growRoom.controller;

import Growup.spring.constant.ApiResponse;
import Growup.spring.constant.status.SuccessStatus;
import Growup.spring.growRoom.dto.GrowRoomDtoReq;
import Growup.spring.growRoom.dto.GrowRoomDtoRes;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.service.GrowRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor    //생성자 자동 생성
@RestController             //http response body에 객체 데이터를 json 반환
@RequestMapping("/growup/growroom")
public class GrowRoomController {

    private final GrowRoomService growRoomService;


    /**
     * 24.02.02 작성자 : 류기현
     * 그로우룸 아래 목록 조회 - 에러핸들러 해야함
     * 그로우룸 Dto res 수정
     */
    @GetMapping("/home")
    public ApiResponse<List<GrowRoomDtoRes.GrowRoomAllDtoRes>> findAllGrowRooms(){
        List<GrowRoomDtoRes.GrowRoomAllDtoRes> growRooms = growRoomService.findAll()
                .stream()
                .map(GrowRoomDtoRes.GrowRoomAllDtoRes::new)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(growRooms);
    }

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 생성
     */
    @PostMapping("/create")    // 생성
    public ApiResponse<GrowRoomDtoRes.GrowRoomViewDtoRes> addGrowRoom(@RequestBody GrowRoomDtoReq.AddGrowRoomDtoReq request){
        GrowRoom growRoom = growRoomService.save(request);

        return ApiResponse.onSuccess(new GrowRoomDtoRes.GrowRoomViewDtoRes(growRoom));
    }

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 {id} 조회
     */
    @GetMapping("/view/{id}")
    public ApiResponse<GrowRoomDtoRes.GrowRoomViewDtoRes> findGrowRoom(@PathVariable Long id){
        GrowRoom growRoom = growRoomService.findById(id);

        return ApiResponse.onSuccess(new GrowRoomDtoRes.GrowRoomViewDtoRes(growRoom));
    }

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 {id} 삭제
     */
    @DeleteMapping("/delete/{id}")
    public ApiResponse<SuccessStatus> deleteGrowRoom(@PathVariable Long id){
        growRoomService.delete(id);

        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    /**
     * 24.01.31 작성자 : 류기현
     * 그로우룸 {id} 수정
     */
    @PutMapping("/put/{id}")
    public ApiResponse<GrowRoomDtoRes.GrowRoomViewDtoRes> updateGrowRoom(@PathVariable Long id, @RequestBody GrowRoomDtoReq.UpdateGrowRoomDtoReq request) {
        GrowRoom updatedGrowRoom = growRoomService.update(id, request);

        return ApiResponse.onSuccess(new GrowRoomDtoRes.GrowRoomViewDtoRes(updatedGrowRoom));
    }
}
