package Growup.spring.growRoom.service;

import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.converter.GrowRoomConverter;
import Growup.spring.growRoom.dto.GrowRoomDtoReq;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import Growup.spring.growRoom.model.component.CategoryDetail;
import Growup.spring.growRoom.model.component.GrowRoomCategory;
import Growup.spring.growRoom.repository.*;
import Growup.spring.security.JwtProvider;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

        List<CategoryDetail> categoryDetails;
        categoryDetails = growRoomConverter.convertToCategoryDetails(request.getCategoryDetailIds());

        // 카테고리 리스트 저장, growRoom에 지정
        growRoom.setGrowRoomCategoryList(growRoomCategoryServiceImpl.save(growRoom, categoryDetails));

        return growRoom;
    }

    // 그로우룸{id} 조회
    public GrowRoom findById(Long id) {
        GrowRoom growRoom = growRoomRepository.findById(id)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));

        // growroom 생성자만 삭제된 growroom에 진입 가능
        if (growRoom.getStatus().equals("삭제") && !(growRoom.getUser().equals(userRepository.findById(jwtProvider.getUserID())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND)))))
            throw new GrowRoomHandler(ErrorStatus.GROWROOM_IS_DELETED);

        return growRoom;
    }

    // 그로우룸{id} 삭제

    @Override
    @Transactional
    public void deleteTemp(Long id) {
        GrowRoom growRoom = growRoomRepository.findById(id)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));

        // growroom.user, request를 보낸 user 동일한지 확인
        User reqUser = userRepository.findById(jwtProvider.getUserID())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_PERMITTED));

        if (!growRoom.getUser().equals(reqUser))
            throw new UserHandler(ErrorStatus.USER_NOT_PERMITTED);

        growRoom.updateStatus("삭제");
    }

    // 그로우룸{id} 수정
    @Transactional
    public GrowRoom update(Long id, GrowRoomDtoReq.UpdateGrowRoomDtoReq request) {

        GrowRoom growRoom = growRoomRepository.findById(id)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));

        // growroom을 생성한 userId와 수정요청한 userId가 다르다면 error
        if(!jwtProvider.getUserID().equals(growRoom.getUser().getId()))
            throw new UserHandler(ErrorStatus.USER_NOT_PERMITTED);


        // 이후에 growroomCategory의 growroomId와 categroyDetailId가 같다면 수정하지 않는 코드로 fix
        growRoomCategoryRepository.deleteGrowRoomCategoriesByGrowRoomId(growRoom.getId());

        List<CategoryDetail> categoryDetails = growRoomConverter.convertToCategoryDetails(request.getCategoryDetailIds());
        List<GrowRoomCategory> growRoomCategories = growRoomCategoryServiceImpl.save(growRoom, categoryDetails);

        growRoom.getPost().update(request.getTitle(), request.getContent());

        growRoom.update(
                recruitmentRepository.findById(request.getRecruitmentId())
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.RECRUITMENT_NOT_FOUND)),
                numberRepository.findById(request.getNumberId())
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.NUMBER_NOT_FOUND)),
                periodRepository.findById(request.getPeriodId())
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PERIOD_NOT_FOUNT)),
                growRoomCategories
        );


        return growRoom;
    }

    @Override
    public int updateView(Long id) {
        return growRoomRepository.updateView(id);
    }
}