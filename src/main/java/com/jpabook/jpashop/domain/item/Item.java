package com.jpabook.jpashop.domain.item;

import com.jpabook.jpashop.domain.Category;
import com.jpabook.jpashop.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 한 테이블의 모든 상속된 변수들 컬럼 생성
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // == 비즈니스 로직 ==/
    /**
     *  stock 증가
     * @param quentity
     */
    public void addStock(int quentity){
        this.stockQuantity += quentity;
    }
    /*
     * stock 감소
     */
    public void removeStock(int quentity){
        int restStock = this.stockQuantity - quentity;
        if(restStock < 0){
            throw new NotEnoughStockException("read more Stock");
        }
        this.stockQuantity = restStock;
    }
}
