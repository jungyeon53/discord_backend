## Discord Clone 백엔드 (24.01.29 ~ 현재 진행중)
프론트 담당 : 이길호 
백엔드 담당 : 김정연
<br />
<a href="https://docs.google.com/spreadsheets/d/1hxbx__Gz9DKd2sEl_v-7-JrF99VRnm534RZEoAIE6Ng/edit#gid=0">[API 명세서]</a>
<br />
2/13 
1. 회원가입
2. 로그인
   - jwt
   - refresh Token 재발급

2/14 
1. 이메일 인증
   - 메일 본문에 token값을 포함하여 전송 
   - GetMapping("/email/tokenLink}) 로 비밀번호 변경 화면 
2. 비밀번호 변경하기

   
