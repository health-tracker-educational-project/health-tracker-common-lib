package com.healthtracker.dto.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@ToString
public class PaginationRequestDto<S> {

    private Integer page;
    private Integer size;
    private List<OrderDto> orders;
    private S searchCriteria;

    public Sort convertToSort() {
        return Sort.by(orders.stream()
                .map(order -> new Sort.Order(
                        Sort.Direction.fromString(order.getDirection()), order.getProperty())
                ).toList());
    }

}
