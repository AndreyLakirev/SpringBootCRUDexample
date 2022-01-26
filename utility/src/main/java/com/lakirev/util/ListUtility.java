package com.lakirev.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtility {
    public static <T> List<T> getDistinctElements (List<T> list) {
        List<T> distinctList = new ArrayList<>();
        for (T element : list) {
            if (!distinctList.contains(element)) {
                distinctList.add(element);
            }
        }
        return distinctList;
    }
}
