package com.healthtracker.dto.pagination;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class PaginationResponseDto<R> {

    private List<R> content;
    private int totalPages;
    private long totalElements;

}
