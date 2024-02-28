## Discord Clone 백엔드 (2024.01.29 ~ 진행중)

백엔드 담당: 김정연 <br>
프론트 담당: 이길호 

[API 명세서](https://docs.google.com/spreadsheets/d/1hxbx__Gz9DKd2sEl_v-7-JrF99VRnm534RZEoAIE6Ng/edit#gid=0)

**기술 스택** <br>
SPRINGBOOT, JPA, REACT, SCSS 

### 진행 내역

- **1/29**
  1. 주제선정
      - 채팅 기능 , 1:1 채팅 기능이 있는 단톡방  
  3. 대상 사용자
      - 20~30대 , 직업 심리검사 추천
      - 결론은 비슷한 기능인 디스코드 클론 

- **1/31**
  1. 컨셉 및 기획
      - 와이어프레임 작성
      - 기능 명세서 작성
      - ERD 설계

- **2/5**
  1. API 명세서 작성
  2. URL 명세서 추가

- **2/13**
  1. 회원가입
  2. 로그인
      - jwt
      - refresh Token 재발급

- **2/14**
  1. 이메일 인증
      - 메일 본문에 token값을 포함하여 전송 
      - GetMapping("/email/tokenLink") 로 비밀번호 변경 화면 
  2. 비밀번호 변경하기

- **2/15**
  1. 마이페이지
      - 비밀번호 변경
      - 닉네임 변경
  2. 프로필 사진 첨부
     - 이미지 정보 디비에 추가 > 첨부파일 완료 
     - 백엔드에 이미지 저장 > static 폴더가 resources 에 인식이 안되는 문제 해결 완료 (24.2.16)

- **2/16**
  1. 첨부파일 경로 수정
     - 실제 저장되는 경로 정확히 명시
  2. 프로필 이미지 삭제
     - 삭제시 데이터베이스 & 저장된 static 폴더 삭제
     - 프로필 이미지만 삭제시 기본 이미지로 추가 예정

- **2/17**
  1. 프로필 이미지 등록시 기본이미지 여부 체크
     - 기본이미지일 경우 새로운 이미지 등록
     - 기존에 등록된 이미지가 있을 경우 기존 이미지 삭제 후 등록 
  2. 기본 프로필 이미지로 변경 
     - 기존에 등록된 이미지 삭제 후 기본 이미지로 변경 

- **2/18**
  1. state(상태) 등록하기
     - 온라인, 오프라인, 방해금지, 자리비움 상태를 데이터 베이스에 추가
  2. 회원가입시 오프라인으로 상태 표시하기
 
- **2/19**
  1. 로그인&로그아웃 상태변경 
     - 온라인, 오프라인 표시
     - 방해금지와 자리비움상태로 로그아웃 후 다시 로그인 하면 전의 상태로 표시 
  2. 상태변경
     - 온라인, 오프라인, 방해금지, 자리비움 상태 변경 가능

- **2/22**
  1. 친구 요청 
     - 친구 요청을 보낼 때 상대방이 나에게 이미 보낸 요청이라면 친구로 추가, 보내지 않았다면 요청 추가 
  2. 친구 요청 삭제 
     - 친구 요청 취소
  3. 친구 삭제
     - 친구 취소
  4. 카운트
     - 친구 목록 카운트
     - 친구 요청 목록 카운트
  5. 리스트
     - 친구 리스트
     - 친구 요청 리스트
    
- **2/26**
  1. 친구 목록보기
  2. 온라인 친구 목록보기

- **2/27 ~ 코드 리팩토링 중!**
