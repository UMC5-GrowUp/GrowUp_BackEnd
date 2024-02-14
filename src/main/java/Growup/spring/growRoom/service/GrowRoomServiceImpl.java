package Growup.spring.growRoom.service;

import Growup.spring.constant.handler.GrowRoomHandler;
import Growup.spring.constant.handler.UserHandler;
import Growup.spring.constant.status.ErrorStatus;
import Growup.spring.growRoom.converter.GrowRoomConverter;
import Growup.spring.growRoom.converter.RecruitmentPeriodConverter;
import Growup.spring.growRoom.dto.GrowRoomDtoReq;
import Growup.spring.growRoom.model.GrowRoom;
import Growup.spring.growRoom.model.Post;
import Growup.spring.growRoom.model.component.CategoryDetail;
import Growup.spring.growRoom.model.component.GrowRoomCategory;
import Growup.spring.growRoom.model.component.RecruitmentPeriod;
import Growup.spring.growRoom.repository.*;
import Growup.spring.liked.model.Liked;
import Growup.spring.liked.repository.LikedRepository;
import Growup.spring.participate.model.Participate;
import Growup.spring.participate.repository.ParticipateRepository;
import Growup.spring.security.JwtProvider;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ParticipateRepository participateRepository;
    private final LikedRepository likedRepository;
    private final RecruitmentPeriodConverter recruitmentPeriodConverter;
    private final RecruitmentPeriodRepository recruitmentPeriodRepository;


    // 그로우룸 글 목록 조회
    public List<GrowRoom> findAll(){
        return growRoomRepository.findAllByStatusNot("삭제");
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

        RecruitmentPeriod recruitmentPeriod = recruitmentPeriodConverter.convertToRecruitmentPeriod(request.getStartDate(), request.getEndDate());
        recruitmentPeriodRepository.save(recruitmentPeriod);
        growRoom.setRecruitmentPeriod(recruitmentPeriod);

        // errorHandler 만들어야함.
        growRoom.setRecruitment(recruitmentRepository.findById(request.getRecruitmentId())
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.RECRUITMENT_NOT_FOUND)));
        growRoom.setNumber(numberRepository.findById(request.getNumberId())
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.NUMBER_NOT_FOUND)));
        growRoom.setPeriod(periodRepository.findById(request.getPeriodId())
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PERIOD_NOT_FOUND)));

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

        // growroom 생성자만 삭제된 growroom에 진입 가능(삭제 and 생성자아님)
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
        growRoom.getRecruitmentPeriod().update(request.getStartDate(), request.getEndDate());

        growRoom.update(
                recruitmentRepository.findById(request.getRecruitmentId())
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.RECRUITMENT_NOT_FOUND)),
                numberRepository.findById(request.getNumberId())
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.NUMBER_NOT_FOUND)),
                periodRepository.findById(request.getPeriodId())
                        .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.PERIOD_NOT_FOUND)),
                growRoomCategories
        );
        return growRoom;
    }

    @Override
    public int updateView(Long id) {
        User user = userRepository.findById(jwtProvider.getUserID())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        GrowRoom growRoom = growRoomRepository.findById(id)
                .orElseThrow(() -> new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND));
        if (user == growRoom.getUser())
            return 0;
        else return growRoomRepository.updateView(id);
    }

    //조회수 증가
//    @Transactional //트랜잭션이란 데이터베이스의 상태를 변경하는 작업 또는 한번에 수행되어야 하는 연산들을 의미한다.
//    @Override
//    public int viewincrease(Long growRoomId) {
//        return growRoomRepository.increaseViews(growRoomId);
//    }


    //라이브룸 선택시 조회 되게 하는것
    @Override
    public Post inquirypost(Long growRoomId) {
        Post post = postRepository.findByGrowRoomId(growRoomId);
        return post;
    }


    //그로우룸 조회
    @Override
    public Page<GrowRoom> GrowRoomList(String filter, Long userId, Integer page) {
        User existuser = userRepository.findById(userId).get();

        List<Participate> participateList = participateRepository.findByUserId(userId); //유저 아이디를 통해 참여자 리스트를 가져옴
        List<Long> growRoomIds1 = participateList.stream() //해당 리스트들을 통해 그로우룸 아이디를 가져옴
                .map(participate -> participate.getGrowRoom().getId())
                .collect(Collectors.toList());

        List<Liked> likedList = likedRepository.findByuserId(userId);
        List<Long> growRoomIds2 = likedList.stream()
                .map(liked -> liked.getGrowRoom().getId())
                .collect(Collectors.toList());

        Page<GrowRoom> list ;

        if (existuser != null) {
            if (filter.equals("내모집글")) {
                list = growRoomRepository.findAllByUserId(userId, PageRequest.of(page, 16));
            }
            else if (filter.equals("참여글")) {
                list = growRoomRepository.findAllByIdIn(growRoomIds1, PageRequest.of(page, 16)); // //그로우룸 아이디를 통해 객체를 가져옴
            }
            else if (filter.equals("관심글")) {
                list = growRoomRepository.findAllByIdIn(growRoomIds2, PageRequest.of(page, 16)); // //그로우룸 아이디를 통해 객체를 가져옴
            }
            else {
                list = growRoomRepository.findAllBy(PageRequest.of(page, 16));
            }
        }
        else {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }

        return list;
    }
}