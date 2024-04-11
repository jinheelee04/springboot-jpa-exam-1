package com.jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

/**
 * 값 타입은 변경 불가능하게 설계해야 한다.
 * 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들자.
 * JPA스텍상 엔티티나 임베디드 타입은 자바 기본 생성자를 public 또는 protected로 설정해야 한다.
 * public으로 두는 것 보다는 protected로 설정하는 것이 그나마 더 안전하다.
 * JPA가 이런 제약을 두는 이유는 JPA구현 라이브러리가 객체를 생성할 때 리플렉션 같은 기술을 사용할 수 있도록
 * 지원해야 하기 때문이다.
 */
@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;
    protected Address(){
    }
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
