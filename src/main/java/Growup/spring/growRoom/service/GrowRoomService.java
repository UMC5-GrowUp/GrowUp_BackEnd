package Growup.spring.growRoom.service;

import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.converter.CategoryDetailConverter;
import Growup.spring.growRoom.dto.AddGrowRoomDtoReq;
import Growup.spring.growRoom.dto.UpdateGrowRoomDtoReq;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import Growup.spring.growRoom.model.component.CategoryDetail;
import Growup.spring.growRoom.model.component.Number;
import Growup.spring.growRoom.model.component.Period;
import Growup.spring.growRoom.model.component.Recruitment;
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
public class GrowRoomService {
    private final GrowRoomRepository growRoomRepository;
    private final JwtProvider jwtProvider;  // 토큰 발급을 위함
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public final GrowRoomCategoryRepository growRoomCategoryRepository;
    private final CategoryDetailRepository categoryDetailRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final NumberRepository numberRepository;
    private final PeriodRepository periodRepository;
    public final GrowRoomCategoryService growRoomCategoryService;
    private final CategoryDetailConverter categoryDetailConverter;


    // 그로우룸 글 목록 조회
    public List<GrowRoom> findAll(){
        return growRoomRepository.findAll();
    }

    // 그로우룸 글 생성
    @Transactional
    public GrowRoom save(AddGrowRoomDtoReq request) {

        // 그로우룸에 user 설정 - 토큰
        User user = userRepository.findById(jwtProvider.getUserID())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        GrowRoom growRoom = request.toEntity();
        growRoom.setUser(user);

        // builder는 컨버터에서
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .status(0)
                .build();

        postRepository.save(post);

        growRoom.setPost(post);

        // errorHandler 만들어야함.
        growRoom.setRecruitment(recruitmentRepository.findById(request.getRecruitmentId())
                .orElseThrow());
        growRoom.setNumber(numberRepository.findById(request.getNumberId())
                .orElseThrow());
        growRoom.setPeriod(periodRepository.findById(request.getPeriodId())
                .orElseThrow());

        List<CategoryDetail> categoryDetails = new ArrayList<>();
        categoryDetails = categoryDetailConverter.convertToCategoryDetails(request.getCategoryDetailIds());

        // 카테고리 리스트 저장, growRoom에 지정
        growRoom.setGrowRoomCategoryList(growRoomCategoryService.save(growRoom, categoryDetails));

        return growRoom;
    }

    // 그로우룸{id} 조회
    public GrowRoom findById(Long id) {
        return growRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    // 그로우룸{id} 삭제
    public void delete(Long id) {
        growRoomRepository.deleteById(id);
    }

    // 그로우룸{id} 수정
    @Transactional
    public GrowRoom update(Long id, UpdateGrowRoomDtoReq request) {
        GrowRoom growRoom = growRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        // builder는 컨버터에서
        growRoom.update(Recruitment.builder().id(request.getRecruitmentId()).build(),
                Number.builder().id(request.getNumberId()).build(),
                Period.builder().id(request.getPeriodId()).build(),
                Post.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .build());

        return growRoom;
    }
}
