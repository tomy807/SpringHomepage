package hompage.basic.web;


import hompage.basic.domain.Item;
import hompage.basic.domain.ItemRepository;
import hompage.basic.web.form.ItemSaveForm;
import hompage.basic.web.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@Slf4j
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
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "html/addForm";
    }

    //아이템 저장
    //아이템 저장하고 새로고침 누를시 똑같은 아이템이 중복 저장되는 버그 발생-> redirect 으로 POST가 아니라 GET으로
    //POST-HTML Form으로 얻어온 Item을 저장하고 해당 아이템페이지로 redirect 해준다.
    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        //복합 롤 검증(총 금액은 10000 이상이여야한다.
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            return "html/addForm";
        }

        //성공로직
        //ItemSaveForm->Item
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

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
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {

        //복합 롤 검증(총 금액은 10000 이상이여야한다.
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("error={}", bindingResult);
            return "html/editForm";
        }

        //성공로직
        //ItemUpdateForm->Item
        Item itemParam = new Item();
        itemParam.setItemName(form.getItemName());
        itemParam.setPrice(form.getPrice());
        itemParam.setQuantity(form.getQuantity());


        itemRepository.update(itemId, itemParam);
        return "redirect:/basic/items/{itemId}";
    }


    //Test 하기 위해 미리 데이터 집어넣기
    @PostConstruct
    public void  init(){
        itemRepository.save(new Item("상품1", 10000, 10));
        itemRepository.save(new Item("상품2", 20000, 12));
    }
}
