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

4 DAY

1.@Validated,데이터 모델의 @NotBlank,...
->@Validated된 @ModelAttribute(Item 모델들의 Fields) 모두 Validator 의 support,validate을 자동으로 검증해줌

(build.gradle 에 implementation 'org.springframework.boot:spring-boot-starter-validation' 추가하면)


검증 방법은 애노테이션을 이용: 

        @NotBlank : 빈값 + 공백만 있는 경우를 허용하지 않는다.
        @NotNull : null 을 허용하지 않는다.
        @Range(min = 1000, max = 1000000) : 범위 안의 값이어야 한다.
        @Max(9999) : 최대 9999까지만 허용한다.

BindingResult->addError(new FieldError),addError(new ObjectError) 또는 rejectValue() , reject()를 이용하여
수동으로 validation 가능.

둘의 차이점은 @Validated은 모델의 각각 필드값에 대한 검증이 편하고 BindingResult은 필드값끼리의 혼합 검증이 편하다.

spring.messages.basename=messages,errors 을 추가하여 BindingResult을 검증메시지를 반환한다. 

@Validated은 default 메시지가 있지만 위에 저장하면 그 메시지가 대신 반환된다.

View 렌더링
    
    .field-error {
         border-color: #dc3545;
         color: #dc3545;
     }
오류가 뜨면 빨간 테두리를 채울 css class생성
    
    <div th:if="${#fields.hasGlobalErrors()}">
            <p class="field-error" th:each="err : ${#fields.globalErrors()}" th:text="${err}">전체 오류 메시지</p>
    </div>

글로벌 error은 bindingResult로 만든 혼합 검증. 있으면 위의 p태그 생성
    
    <input type="text" id="itemName" th:field="*{itemName}"
                   th:errorclss="field-error" class="form-control" placeholder="이름을 입력하세요">
    <div class="field-error" th:errors="*{itemName}">

오류가 있으면 th:errorclss을 이용하여 class 추가,th:errors 으로 오류메시지 text에반환

예정:::: 쿠키 이용하여 로그인 기능 생성

5 DAY

세션관리 방법
1.수동으로 Session 생성

                 member 
    1.(클라)Login------->(서버)createSession                          cookie
    2.(서버)sessionStore에 <UUID,member>저장,쿠키 생성<Cookie_Name,UUID>------->클라
    3.(클라)LoginHome------->(서버)getSession
    4.(서버)쿠키들에서 Cookie_Name의 쿠키 찾기 null이면 비로그인 상태(home), null이 아니면             member
      로그인 상태(LoginHoem)이므로 쿠키의 value(UUID)통해 sessionStore에서 UUID로 value(member)전덜 -------> 클라
    정리:쿠키<Cookie_Name,uuid> 세션스토어 <uuid,member>    

2.HttpSession(servlet 제공) 사용

위의 해당과정을 자동으로 해줌
    
    로그인과정
    1.HttpSession session = request.getSession();(default:true 없으면 새로운 세션 반환)
      session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember); 연결키=SessionConst.LOGIN_MEMBER
    홈
    2.HttpSession session = request.getSession(false);(세션 있으면 기존 세션 반환,없으면 null)
      Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

예정:::: 아직 items은 누구나 들어가지되어있다. 로그인한 사람만 자신의 items에 들어갈수 있도록 필터나 인터셉터 구현 예정

