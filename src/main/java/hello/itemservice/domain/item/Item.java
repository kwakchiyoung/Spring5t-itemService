package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Item {
    private long id;//아이디
    private String itemName;//상품명
    private Integer price; // 가격 null일수도있어서 Integer사용
    private Integer quantity; //수량
    public Item(){ //기본생성자
    }

    public Item(String itemName, Integer price, Integer quantity) { //아이디를 제외한 생성자
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
