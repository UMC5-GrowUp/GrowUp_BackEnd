package Growup.spring.growRoom.service;


import Growup.spring.growRoom.dto.AddGrowRoomRequest;
import Growup.spring.growRoom.dto.UpdateGrowRoomRequest;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.repository.GrowRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@RequiredArgsConstructor
@Service
public class GrowRoomService {
    private final GrowRoomRepository growRoomRepository;

    // 그로우룸 글 목록 조회
    public List<GrowRoom> findAll(){
        return growRoomRepository.findAll();
    }

    // 그로우룸 글 생성
    public GrowRoom save(AddGrowRoomRequest request) {
        return growRoomRepository.save(request.toEntity());
    }

    // 그로우룸{id} 조회
    public GrowRoom findById(long id) {
        return growRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    // 그로우룸{id} 삭제
    public void delete(long id) {
        growRoomRepository.deleteById(id);
    }

    // 그로우룸{id} 수정
    @Transactional
    public GrowRoom update(long id, UpdateGrowRoomRequest request) {
        GrowRoom growRoom = growRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        growRoom.update(request.getRecruitment(), request.getNumber(), request.getPeriod(), request.getTitle(), request.getContent());

        return growRoom;
    }
}
