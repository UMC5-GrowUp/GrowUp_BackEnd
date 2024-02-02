package Growup.spring.growRoom.service;

import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.converter.GrowRoomConverter;
import Growup.spring.growRoom.dto.GrowRoomDtoReq;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import Growup.spring.growRoom.model.component.CategoryDetail;
import Growup.spring.growRoom.repository.*;
import Growup.spring.security.JwtProvider;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GrowRoomServiceImpl implements GrowRoomService {
    private final GrowRoomRepository growRoomRepository;
    private final JwtProvider jwtProvider;  // 토큰 발급을 위함
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public final GrowRoomCategoryRepository growRoomCategoryRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final NumberRepository numberRepository;
    private final PeriodRepository periodRepository;
    public final GrowRoomCategoryServiceImpl growRoomCategoryServiceImpl;
    public final GrowRoomConverter growRoomConverter;


    // 그로우룸 글 목록 조회
    public List<GrowRoom> findAll(){
        return growRoomRepository.findAll();
    }

    // 그로우룸 글 생성
    @Transactional
    public GrowRoom save(GrowRoomDtoReq.AddGrowRoomDtoReq request) {

        // 그로우룸에 user 설정 - 토큰
        User user = userRepository.findById(jwtProvider.getUserID())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        GrowRoom growRoom = request.toEntity();
        growRoom.setUser(user);

        // post 저장
        Post post = growRoomConverter.convertToPost(request.getTitle(), request.getContent());
        postRepository.save(post);
        growRoom.setPost(post);

        // errorHandler 만들어야함.
        growRoom.setRecruitment(recruitmentRepository.findById(request.getRecruitmentId())
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.RECRUITMENT_NOT_FOUND)));
        growRoom.setNumber(numberRepository.findById(request.getNumberId())
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.NUMBER_NOT_FOUND)));
        growRoom.setPeriod(periodRepository.findById(request.getPeriodId())
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PERIOD_NOT_FOUNT)));

        List<CategoryDetail> categoryDetails = new ArrayList<>();
        categoryDetails = growRoomConverter.convertToCategoryDetails(request.getCategoryDetailIds());

        // 카테고리 리스트 저장, growRoom에 지정
        growRoom.setGrowRoomCategoryList(growRoomCategoryServiceImpl.save(growRoom, categoryDetails));

        return growRoom;
    }

    // 그로우룸{id} 조회
    public GrowRoom findById(Long id) {
        return growRoomRepository.findById(id)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));
    }

    // 그로우룸{id} 삭제
    public void delete(Long id) {
        growRoomRepository.deleteById(id);
    }

    // 그로우룸{id} 수정
    @Transactional
    public GrowRoom update(Long id, GrowRoomDtoReq.UpdateGrowRoomDtoReq request) {
        GrowRoom growRoom = growRoomRepository.findById(id)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));

        growRoom.update(
                recruitmentRepository.findById(request.getRecruitmentId())
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.RECRUITMENT_NOT_FOUND)),
                numberRepository.findById(request.getNumberId())
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.NUMBER_NOT_FOUND)),
                periodRepository.findById(request.getPeriodId())
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PERIOD_NOT_FOUNT)),
                growRoomConverter.convertToPost(request.getTitle(), request.getContent())
        );


        return growRoom;
    }
}