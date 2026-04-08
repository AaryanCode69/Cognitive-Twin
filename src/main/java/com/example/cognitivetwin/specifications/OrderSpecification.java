package com.example.cognitivetwin.specifications;

import com.example.cognitivetwin.order.dto.OrderFilterDTO;
import com.example.cognitivetwin.order.entity.OrderEntity;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;



public class OrderSpecification {

    public static Specification<OrderEntity> getSpecification(OrderFilterDTO orderFilterDTO){
        return (root,query,criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(orderFilterDTO.orderStatus() != null){
                predicates.add(criteriaBuilder.equal(root.get("orderStatus"),orderFilterDTO.orderStatus()));
            }
            if(orderFilterDTO.userId() != null){
                predicates.add( criteriaBuilder.equal(root.get("user").get("id"),orderFilterDTO.userId()));
            }
            if(orderFilterDTO.minTotalAmount() != null){
                predicates.add( criteriaBuilder.greaterThanOrEqualTo(root.get("totalAmount"),orderFilterDTO.minTotalAmount()));
            }
            if(orderFilterDTO.maxTotalAmount()!=null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("totalAmount"),orderFilterDTO.maxTotalAmount()));
            }
            if(orderFilterDTO.paymentStatus()!=null){
                predicates.add(criteriaBuilder.equal(root.join("payment", JoinType.LEFT).get("paymentStatus"),orderFilterDTO.paymentStatus()));
            }
            if(orderFilterDTO.createdBefore()!=null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"),orderFilterDTO.createdBefore()));
            }
            if(orderFilterDTO.createdAfter()!=null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"),orderFilterDTO.createdAfter()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
