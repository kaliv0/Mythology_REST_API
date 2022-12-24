package com.kaliv.myths.common;

import com.kaliv.myths.constant.CriteriaConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor //?
public class SortCriteria {
    private String sortOrder = CriteriaConstants.DEFAULT_SORT_ORDER;
    private String sortAttribute = CriteriaConstants.DEFAULT_SORT_ATTRIBUTE;
}
