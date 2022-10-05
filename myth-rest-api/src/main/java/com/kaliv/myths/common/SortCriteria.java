package com.kaliv.myths.common;

import com.kaliv.myths.constants.CriteriaConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;

@Getter
@Setter
@NoArgsConstructor //?
public class SortCriteria {
    private SortOrder sortOrder = CriteriaConstants.DEFAULT_SORT_ORDER;
    private String sortAttribute = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE;
}
