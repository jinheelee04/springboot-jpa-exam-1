package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderSearch;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 *
 * 쿼리 방식 선택 권장 순서
 * 1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
 * 2. 필요하면 패치 조인으로 성능 최적화 한다. -> 대부분의 성능 이슈가 해결된다.
 * 3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
 * 4. 최후의 방법은 JPA가 제공하는 네이티브SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for(Order order : all){
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
;       return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleDto> ordersV2(){
        // N+1 문제 발생함
        return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(OrderSimpleDto::new)
                .collect(toList());
    }

    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderSimpleDto> result = orders.stream().map(o -> new OrderSimpleDto(o))
                .collect(toList());
        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }
    @Data
    private class OrderSimpleDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화 (영속성 컨텍스트를 조회 후 없으면 쿼리 실행)
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}
