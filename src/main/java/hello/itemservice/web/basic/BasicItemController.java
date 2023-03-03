package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("basic/items")
@RequiredArgsConstructor //롬복 - final이 붙은 객체의 생성자를 만들어준다. 생성자가 하나일경우 @Autowired가 생략 가능.
public class BasicItemController {
    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model){ //1.아이템조회
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items",items);
        return "basic/items";
    }
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId,Model model){ //2.아이템상세
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/item";
    }
    @GetMapping("/add")
    public String addItemV1(){
        return "basic/addForm";
    } //3 아이템등록 템플릿 호출

    //@PostMapping("/add")
    public String save(@RequestParam String itemName,  //3.1 아이템등록
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model){
        Item item = new Item(itemName,price,quantity);
        itemRepository.save(item);
        model.addAttribute("item",item);
        return "basic/item";
    }
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model){//3.2 아이템등록
        itemRepository.save(item);
        //model.addAttribute("item",item); //자동 추가되어 생략 가능.    //@ModelAttribute("item")이부분이 알아서 이 기능까지 처리해준다.
        //  아니면 @ModelAttribute("item") Item item이렇게 사용하고 위에 주석을 제거,
        //또는  @ModelAttribute("item") Item이렇게 @ModelAttribute 의 이름을 생략하면 모델에 저장될 때 클래스명을 사용한다. 이때 클래스의 첫글자만 소문자로 변경해서 자동등록된다.

        return "basic/item";
    }
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, Model model){ //3.3 아이템등록
        itemRepository.save(item);
        //또는  @ModelAttribute("item") Item이렇게 @ModelAttribute 의 이름을 생략하면 모델에 저장될 때 클래스명을 사용한다. 이때 클래스의 첫글자만 소문자로 변경해서 자동등록된다.
        return "basic/item";
    }
    //@PostMapping("/add") //3.4아이템등록
    public String addItemV4(Item item, Model model){ //@ModelAttribute 자체도 생략가능하다. 대상 객체는 모델에 자동 등록된다.
        itemRepository.save(item);
        return "basic/item";
    }

    //@PostMapping("/add") //3.5아이템등록 redirect
    public String addItemV5(Item item, Model model){
        itemRepository.save(item);
        return "redirect:/basic/items/"+ item.getId();
    }
    @PostMapping("/add") //3.6아이템등록
    public String addItemV6(Item item, Model model , RedirectAttributes redirectAttributes){ //@ModelAttribute 자체도 생략가능하다. 대상 객체는 모델에 자동 등록된다.
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId()); //리턴에서 {itemId}로 치환된다.
        redirectAttributes.addAttribute("status", true); //나머지는 쿼리 파라미터로 넘어간다.
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit") //4.수정
    public String editForm(@PathVariable Long itemId , Model model){ //@PathVariable("itemId") <-변수명이 같으면 생략가능.
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item",item);
        return "basic/editForm";
    }
    @PostMapping("/{itemId}/edit") //4.수정
    public String edit(@PathVariable Long itemId , @ModelAttribute Item item){
        itemRepository.update(itemId,item);
        return "redirect:/basic/items/{itemId}";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA",10000,10));
        itemRepository.save(new Item("itemB",20000,20));
        itemRepository.save(new Item("itemC",30000,30));
    }
}
