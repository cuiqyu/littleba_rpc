package com.limpid.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @auther cuiqiongyu
 * @create 2020/9/1 18:00
 */
@Getter
@Setter
public class Holder<T> implements Serializable {

    private volatile T value;

}
