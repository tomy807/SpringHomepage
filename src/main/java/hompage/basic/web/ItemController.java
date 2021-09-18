package hompage.basic.web;


import hompage.basic.domain.Item;
import hompage.basic.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;

    //첫화면에 모든 아이템들 뿌려주기
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "html/items";
    }


    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "html/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "html/addForm";
    }

    //아이템 저장
    //아이템 저장하고 새로고침 누를시 똑같은 아이템이 중복 저장되는 버그 발생-> redirect 으로 POST가 아니라 GET으로
    //POST-HTML Form으로 얻어온 Item을 저장하고 해당 아이템페이지로 redirect 해준다.
    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        Item saveItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", saveItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }

    //아이템 수정화면 보여주기
    @GetMapping("/{itemId}/edit")
    public String editFrom(@PathVariable Long itemId, Model model) {
        //수정하고자 하는 해당 아이템정보 먼저 보여주기
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "html/editForm";
    }

    //아이템 수정저장
    //수정할 정도 ModelAttribute 로 받아와서 업데이트
    //수정 완료후, 해당 아이템으로 redirect
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }


    //Test 하기 위해 미리 데이터 집어넣기
    @PostConstruct
    public void  init(){
        itemRepository.save(new Item("상품1", 10000, 10));
        itemRepository.save(new Item("상품2", 20000, 12));
    }
}
