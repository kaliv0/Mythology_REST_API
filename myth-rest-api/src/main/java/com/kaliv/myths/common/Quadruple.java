package com.kaliv.myths.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quadruple<T, U, V, W> {
    private T first;
    private U second;

    private V third;
    private W fourth;
}
