package ru.practicum.common.model;

import org.springframework.data.domain.Sort;

public class Constants {
    public static final String MIN_SIZE = "0";
    public static final String MAX_SIZE = "10";

    public static final Sort ID_SORTING_DESC = Sort.by(Sort.Direction.DESC, "id");

}
