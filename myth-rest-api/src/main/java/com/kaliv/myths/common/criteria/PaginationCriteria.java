package com.kaliv.myths.common.criteria;

import com.kaliv.myths.constant.CriteriaConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginationCriteria {
    //TODO:check if overriding works for default values
    private int page = CriteriaConstants.DEFAULT_PAGE;
    private int size = CriteriaConstants.DEFAULT_SIZE;
}
