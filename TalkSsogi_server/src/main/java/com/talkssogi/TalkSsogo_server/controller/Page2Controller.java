package com.talkssogi.TalkSsogo_server.controller;


//페이지2의 채팅방 목록에 번호 매겨서 스프링부트 유저 객체에 저장된 채팅방 배열의 번호를 갖게 해야됨
// api에 같이 전달해서 해당 채팅방에 대한 결과를 전달 받음
/*
page2 서버에서 가져올 데이터
- 사용자 이름(id) - user_id
- 사용자의 기존 카톡 채팅방 파일 목록 (리스트 형식) - user_chat
=>컨트롤러에서 id랑 목록을 안드로이드로 전달할 api를 만듦
=>서비스에서 전달할 목록을 만들거임
목록 = Map형식으로 key=채팅방 이름, value=채팅방 번호(user.chatList에 저장된 인덱스)
위의 과정에서 필요한게 있으면 도메인이나 레포지토리에 추가해도 됨
=>안드로이드 스튜디오에서 만든 코드 다시 보고 어떤 형태로 데이터를 넘겨줘야하는지 고려해서 api만들기
 */
public class Page2Controller {
}
