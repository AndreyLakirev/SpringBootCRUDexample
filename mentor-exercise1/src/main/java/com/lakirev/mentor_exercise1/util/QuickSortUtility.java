package com.lakirev.mentor_exercise1.util;

import java.util.ArrayList;
import java.util.List;

public class QuickSortUtility {
    public static <T extends Comparable<? super T>> List<T> quickSort(List<T> targetList) {
        return quickSortStep(targetList, targetList.size() / 2);
    }

    private static <T extends Comparable<? super T>> List<T> quickSortStep(List<T> targetList, int pointElementIndex) {
        List<T> resultList = new ArrayList<>();
        List<T> leftList = new ArrayList<>();
        List<T> rightList = new ArrayList<>();
        for (int i = 0; i < targetList.size(); i++) {
            if (i == pointElementIndex) continue;
            if (targetList.get(i).compareTo(targetList.get(pointElementIndex)) < 0) {
                leftList.add(targetList.get(i));
            } else {
                rightList.add(targetList.get(i));
            }
        }
        if (leftList.size() < 2) {
            resultList.addAll(leftList);
        } else {
            resultList.addAll(quickSortStep(leftList, leftList.size() / 2));
        }
        resultList.add(targetList.get(pointElementIndex));
        if (rightList.size() < 2) {
            resultList.addAll(rightList);
        } else {
            resultList.addAll(quickSortStep(rightList, rightList.size() / 2));
        }
        return resultList;
    }
}
