# [REST API 서버] "자기개발 및 공부 시간 측명 및 화면 공유 서비스"

<img width="1164" alt="image" src="https://github.com/Jungjuhyeon/BOJ/assets/131857282/31dcbc2c-7fba-41b9-b34b-96684897fba1">


> 자신의 공부 시간 측정/ 투두리스트 작성/ 화면공유 기능을 통한 팀구성/ 자기개발을 위한 서비스
>
---

### 🧑‍🤝‍🧑 맴버구성
 - 팀원1 : 정주현 - 사설 로그인(회원관리),마이페이지, 라이브룸(CRUD), 투두리스트(CRUD), 캘린더(CRUD), CICD 구축
 - 팀원2 : 류기현 - 그로우룸(CRUD),댓글 및 대댓글
 - 팀원3 : 진명재 - github 초기시작 제작, 초기 응답 코드 통일및 Domain 정의
 - 팀원4 : 김지환 - 화면 공유 및 캠 공유


---

## 🍩 프로젝트 개요

### 서비스 소개
- 자기개발(갓생)을 위한 서비스
- 자신의 공부 시간을 측정할 수 있는 서비스
- 투두리스트 작성 서비스
- 다른 팀원과 그룹을 구성 후 화면공유 하여 같이 공부하는 서비스


### 진행사항
- UMC 5th GroupUP Project [기한 약 40~50일]
---

## 🍠서버 구조도



>- AWS EC2 : FreeTier t2.micro
>- OS : Ubuntu 22.04 LTS
>- Java : Oracle Open JDK "11"
>- SpringBoot : 2.7.17
>- AWS RDS : Freetier t2.micro 버스터블 클래스 & Mysql
---



---
## 🌮기능 정의

#### 1. 사설로그인
- 회원가입, 본인인증(이메일인증), 비밀번호 재설정,  로그인, 로그아웃, 회원탈퇴

#### 2. 마이페이지
- 마이페이지 조회, 프로필 사진/닉네임/이메일 변경, 개인 공부 누적 시간 조회(월별)

#### 3. 투두리스트
- 투두리스트 들록/조회/상태수정/내용수정

#### 3. 캘린더
- 캘린더 내용 들록/조회(선택한 날짜)
- 캘린더 조회(달 기준)
- 캘린더 글(목록) 수정/상태 수정(줄 긋기)
- 캘린더 해당 날짜 색상 수정/ 캘린더 글(목록) 삭제

#### 4. 라이브룸
- 그로우룸의 좋아요 설정
- 라이브룸 참여자 목록 조회
- 총 누적 시간 랭킹 -일/주/월 별
- 타이머 클릭시 멈춤

#### 5. 그로우룸
- 그로우룸 생성,조회,삭제,수정
- 그로우룸 조회수 

- #### 6. 댓글
- 댓글 작성/수정/삭제
- 대댓글 기능


---
## ☕핵심 기능
### (구현 완료)
- Redis
- 화면 공유


---
## 🍚플로우 차트



--- 
## 🧀ERD
![erd](https://github.com/Jungjuhyeon/BOJ/assets/131857282/f23fe968-b8ae-4ca6-84ac-91f3b73f3ed9)
