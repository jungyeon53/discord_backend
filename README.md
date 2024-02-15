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


   
