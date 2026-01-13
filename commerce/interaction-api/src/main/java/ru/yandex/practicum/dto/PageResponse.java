package ru.yandex.practicum.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private PageableResponse pageable;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private List<SortOrderResponse> sort;
    private boolean first;
    private int numberOfElements;
    private boolean empty;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageableResponse {
        private int pageNumber;
        private int pageSize;
        private List<SortOrderResponse> sort;
        private int offset;
        private boolean paged;
        private boolean unpaged;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SortOrderResponse {
        @JsonProperty("direction")
        private String direction;

        @JsonProperty("property")
        private String property;

        @JsonProperty("ignoreCase")
        private boolean ignoreCase;

        @JsonProperty("nullHandling")
        private String nullHandling;

        @JsonProperty("ascending")
        private boolean ascending;

        @JsonProperty("descending")
        private boolean descending;

        public static SortOrderResponse fromOrder(Sort.Order order) {
            return new SortOrderResponse(
                    order.getDirection().name(),
                    order.getProperty(),
                    order.isIgnoreCase(),
                    order.getNullHandling().name(),
                    order.isAscending(),
                    order.isDescending()
            );
        }
    }

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        List<SortOrderResponse> sortOrders = convertSort(page.getSort());

        PageableResponse pageableResponse = new PageableResponse(
                page.getNumber(),
                page.getSize(),
                sortOrders,
                (int) page.getPageable().getOffset(),
                page.getPageable().isPaged(),
                page.getPageable().isUnpaged()
        );

        return new PageResponse<>(
                page.getContent(),
                pageableResponse,
                page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                sortOrders,
                page.isFirst(),
                page.getNumberOfElements(),
                page.isEmpty()
        );
    }

    private static List<SortOrderResponse> convertSort(Sort sort) {
        List<SortOrderResponse> orders = new ArrayList<>();
        if (sort.isSorted()) {
            for (Sort.Order order : sort) {
                orders.add(SortOrderResponse.fromOrder(order));
            }
        }
        return orders;
    }
}