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
 
예정::::상품 ID,상품명,가격,수량 text 메시지 변수화 예정-> 국제화(message.properties에 영어,한글 따로 저장),Input에 잘못된 서식으로 입력하였을때 오류메시지 추가 예정
     
3 DAY
     
th:object으로 Model model의 아이템 Object 사용하여 th:field 적용->input의 id,name,value 자동으로 매핑(※주의 /basic/items/add(GET)에는 Model이 없으므로 생성)

하드코딩 되어 있는 상품 ID,상품명,가격,수량,페이지,버튼 label의 text를 변경용이하도록 변수화->스프링 부트가 제공하는 messages.properties 사용->국제화 도움

메시지 표현식: #{...} 사용 th:text="#{label.item.itemName}으로 적용 
     
     label.item.id=상품 ID                     
     label.item.itemName=상품명
     label.item.price=가격
     label.item.quantity=수량

     page.items=상품 목록
     page.item=상품 상세
     page.addItem=상품 등록
     page.updateItem=상품 수정

     button.save=저장
     button.cancel=취소

국제화는 messages아래에messages_en.properties 파일 만들어 똑같이 만들어준다.locale을 en으로 바꾸면 자동 매핑
     
     label.item.id=Item ID
     label.item.itemName=Item Name
     label.item.price=price
     label.item.quantity=quantity

     page.items=Item List
     page.item=Item Detail
     page.addItem=Item Add
     page.updateItem=Item Update

     button.save=Save
     button.cancel=Cancel

예정:::: 상품등록 수정할떄, Input에 잘못된 값을 입력하였을시 오류 메시지 출력 
     
     1.상품명은 NotNull
     2.가격은 NotNull,1000원 이상 1000000원이하
     3.수량은 NotNull,10000개 이하
     4.총금액은 10000이상
    
위와 같은 조건으로 Validation 적용할것.     
