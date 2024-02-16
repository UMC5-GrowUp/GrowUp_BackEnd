package Growup.spring.growRoom.service;

import Growup.spring.constant.exeption.GeneralException;
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
import Growup.spring.security.JwtProvider;
import Growup.spring.user.model.User;
import Growup.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private final LikedRepository likedRepository;
    private final RecruitmentPeriodConverter recruitmentPeriodConverter;
    private final RecruitmentPeriodRepository recruitmentPeriodRepository;


    // 그로우룸 글 목록 조회 - 조건
    @Override
    public List<GrowRoom> findByFilter(String filter, String categoryDetail, String period, String status, Long userId, String search) {

        List<GrowRoom> growRooms1;
//        List<GrowRoom> growRooms2 = null;
        List<GrowRoom> growRooms3 = null;
        List<GrowRoom> growRooms4 = null;
        List<GrowRoom> growRooms5 = null;

        int a1 = 0;
        int a2 = 0;
        int a3 = 0;
        int a4 = 0;
        int a5 = 0;


        // 조건 1 : filter
        // 필수 선택
        switch (filter){
            case "내 모집글" :
                growRooms1 = growRoomRepository.findAllByUserId(userId);
                break;
            case "관심글" :
                List<Liked> likedList = likedRepository.findByUserId(userId);
                List<Long> growRoomIds = likedList.stream()
                        .map(liked -> liked.getGrowRoom().getId())
                        .collect(Collectors.toList());
                growRooms1 = growRoomRepository.findAllByIdIn(growRoomIds);
                break;
            case "프로젝트" :
                growRooms1 = growRoomRepository.findAllByRecruitmentId(Long.valueOf("2"));
                break;
            case "스터디" :
                growRooms1 = growRoomRepository.findAllByRecruitmentId(Long.valueOf("1"));
                break;
            case "챌린지" :
                growRooms1 = growRoomRepository.findAllByRecruitmentId(Long.valueOf("3"));
                break;
            default :
                growRooms1 = growRoomRepository.findAll();
                break;
////                throw new GrowRoomHandler(ErrorStatus._BAD_REQUEST);
        }


        // 조건 2 : categoryDetail
        // 내림차순이 아닌 오름차순으로 정렬되는 문제
        if (!categoryDetail.equals("전체")) {
            Set<GrowRoom> growRoomSet = new HashSet<>();

            for (GrowRoom growRoom : growRooms1) {
                List<GrowRoomCategory> growRoomCategories = growRoom.getGrowRoomCategoryList();
                for (GrowRoomCategory growRoomCategory : growRoomCategories) {
                    String categoryName = growRoomCategory.getCategoryDetail().getName();
                    if (categoryName.equals(categoryDetail)) {
                        growRoomSet.add(growRoom);
                        break;
                    }
                }
            }

            growRooms1 = new ArrayList<>(growRoomSet);
        }
        else{
            a2 = 1;
        }


//        // 조건 3 : period
//        if (!period.equals("전체")) {
//            // 조건
//        }
//        else a3 = 1;
//
//
        // 조건 4 : 모집 중만 보기
        if (!status.equals("전체")) {
            growRooms4 = growRoomRepository.findAllByStatus(status);
        }
        else a4 = 1;
//
//        // 조건 5 : 검색창
//        if (search != null) {
//            growRooms5 = new ArrayList<>();
//            List<Post> posts = postRepository.findAllByTitleContaining(search);
//            for (Post post : posts){
//                growRooms5.add(growRoomRepository.findByPost(post));
//            }
//            if (growRooms5.isEmpty()){
//                throw new GrowRoomHandler(ErrorStatus.GROWROOM_NOT_FOUND);
//            }
//        }
//        else a5 = 1;

        List<GrowRoom> growRooms = new ArrayList<>(growRooms1);
//        if (growRooms3 != null)
//            growRooms.retainAll(growRooms3);
        if (growRooms4 != null)
            growRooms.retainAll(growRooms4);
//        if (growRooms5 != null)
//            growRooms.retainAll(growRooms5);
//
        return growRooms;
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
//        else throw new UserHandler(ErrorStatus.USER_NOT_PERMITTED);

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

    // 조회수 증가
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


    // 라이브룸 선택시 조회 되게 하는것
    @Override
    public Post inquirypost(Long growRoomId) {
        return postRepository.findByGrowRoomId(growRoomId);
    }
}