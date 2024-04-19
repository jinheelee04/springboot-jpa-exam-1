package com.jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;
    
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore // 엔티티 안에 화면 조건 있으면 상황에 따라 다르기 때문에 문제가 발생함
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
