package Growup.spring.participate.dto;




import lombok.*;

import java.util.List;

public class ParticipateDtoRes {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class orderByAsc {
        List<participateRes> participateResList; //participateRes를 가져옴

    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class participateRes {

        private String nickName;
        private Integer liked;
        private String photoUrl;

    }
}
