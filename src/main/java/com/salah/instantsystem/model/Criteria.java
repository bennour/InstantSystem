package com.salah.instantsystem.model;

import lombok.Data;

import javax.swing.*;
import java.util.Optional;

@Data
public class Criteria {

    private SortOrder sortOrder;
    private Optional<String> sortBy = Optional.empty();
    private Optional<Boolean> onlyOpened = Optional.empty();
    private Optional<Integer> minFree = Optional.empty();
    private Optional<Double> maxPrice = Optional.empty();
    private Optional<String> duration = Optional.empty();

}
