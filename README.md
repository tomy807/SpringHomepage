# SpringHomepage
1 DAY

Domian: Item(id,itemName,price,quantitiy) ItemRepository(DB 연결전에 임시 ConcurrentHashMap에 데이터 저장)  

Web: ItemController
     구조-->/basic/items(GET) 모든 아이템 
     
            /basic/items/{itemId}(GET) 각각의 아이템 
            
            /basic/items/add(GET) 아이템 생성 폼       
            
            /basic/items/{itemId}/edit(GET) 각각의 아이템 마다 수정 화면 보여주기 
            
            --------------------------------------------------------------------(GET)
            
            /basic/items/add(POST) 받아온 아이템 ItemRepository에 저장후, redirect /basic/items/{itemId}
            
            /basic/items/{itemId}/edit(POST) 해당 아이템 ItemRepository에 update,redirect /basic/items/{itemId}

오류::::/basic/items/add(POST)한 후에 새로고침하면 아이템 저장 중복 오류 발생-->/basic/items/{itemId}(GET)으로 GET방식으로Redirect

저장한후,정상적으로 저장됨을 알리기 위해 RedirectAttributes에 {status: true}을 넣고 redirect 할때 함께 이동

(status가 true일떄만 html으로 "저장완료"을 생성할 예정)

예정::::Thymeleaf로 html 수정예정

2 DAY

Thymeleaf로 Html 수정 완료
          <html xmlns:th="http://www.thymeleaf.org">
               
          th:href="@{/css/bootstrap.min.css}"
          
          th:onclick="|location.href='@{/basic/items/add}'|" URL표현식:@{...}
          
          th:href="@{|/basic/items/${item.id}|}" URL표현식:@{...}, ||리터럴로 string,변수표현식 간단 수정
     
          th:each="item: ${items}" each문 
     
          th:if="${param.status}" RedirectAttributes으로 true일 경우에만 item에 "저장완료" 끼어넣기
