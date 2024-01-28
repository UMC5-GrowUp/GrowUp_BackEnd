package Growup.spring.growRoom.controller;

import Growup.spring.growRoom.domain.GrowRoom;
import Growup.spring.growRoom.dto.AddGrowRoomRequest;
import Growup.spring.growRoom.dto.GrowRoomResponse;
import Growup.spring.growRoom.dto.UpdateGrowRoomRequest;
import Growup.spring.growRoom.service.GrowRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor    //생성자 자동 생성
@RestController             //http response body에 객체 데이터를 json 반환
public class GrowRoomApiController {

    private final GrowRoomService growRoomService;


    // 그로우룸 목록 조회
    @GetMapping("/growup/growrooms")
    public ResponseEntity<List<GrowRoomResponse>> findAllGrowRooms(){
        List<GrowRoomResponse> growRooms = growRoomService.findAll()
                .stream()
                .map(GrowRoomResponse::new)
                .collect(Collectors.toList());  //음..?

        return ResponseEntity.ok()
                .body(growRooms);
    }

    // 그로우룸 생성
    @PostMapping("/growup/growrooms")
    public ResponseEntity<GrowRoom> addGrowRoom(@RequestBody AddGrowRoomRequest request){
        GrowRoom savedGrowRoom = growRoomService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedGrowRoom);
    }

    // 그로우룸{id} 조회
    @GetMapping("/growup/growrooms/{id}")
    public ResponseEntity<GrowRoomResponse> findGrowRoom(@PathVariable long id){
        GrowRoom growRoom = growRoomService.findById(id);

        return ResponseEntity.ok()
                .body(new GrowRoomResponse(growRoom));
    }

    // 그로우룸{id} 삭제
    @DeleteMapping("/growup/growrooms/{id}")
    public ResponseEntity<Void> deleteGrowRoom(@PathVariable long id){
        growRoomService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    // 그로우룸{id} 수정
    @PutMapping("/growup/growrooms/{id}")
    public ResponseEntity<GrowRoom> updateGrowRoom(@PathVariable long id, @RequestBody UpdateGrowRoomRequest request) {
        GrowRoom updatedGrowRoom = growRoomService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedGrowRoom);
    }
}
