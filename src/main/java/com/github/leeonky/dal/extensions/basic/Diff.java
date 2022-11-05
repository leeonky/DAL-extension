package com.github.leeonky.dal.extensions.basic;

import com.github.leeonky.dal.util.TextUtil;
import com.github.leeonky.interpreter.StringWithPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

public class Diff {
    private static final String LEFT_EXPECT = "Expect:";
    private static final String RIGHT_ACTUAL = "Actual:";
    private final String detail;

    public Diff(String expected, String actual) {
        detail = needDiff(expected, actual) ? makeDiffDetail(expected, actual) : "";
    }

    private boolean needDiff(String expected, String actual) {
        return actual != null && ((actual.contains("\n") || actual.contains("\r"))
                || expected.contains("\n") || expected.contains("\r"));
    }

    private String makeDiffDetail(String expected, String actual) {
        int position = TextUtil.differentPosition(expected, Objects.requireNonNull(actual));
        List<String> expectedInfo = new ArrayList<>(TextUtil.lines(new StringWithPosition(expected).position(position).result()));
        List<String> actualInfo = new ArrayList<>(TextUtil.lines(new StringWithPosition(actual).position(position).result()));
        int leftWidth = Math.max(expectedInfo.stream().mapToInt(String::length).max().orElse(0), LEFT_EXPECT.length());
        int rightWidth = Math.max(actualInfo.stream().mapToInt(String::length).max().orElse(0), RIGHT_ACTUAL.length());
        int maxLine = Math.max(actualInfo.size(), expectedInfo.size());
        for (int i = expectedInfo.size(); i < maxLine; i++)
            expectedInfo.add("");
        for (int i = actualInfo.size(); i < maxLine; i++)
            actualInfo.add("");
        String leftFormat = "\n%-" + leftWidth + "s";
        StringBuilder builder = new StringBuilder().append("Diff:");
        builder.append(format(leftFormat, LEFT_EXPECT)).append(" | ").append(RIGHT_ACTUAL).append('\n');
        for (int i = 0; i < leftWidth; i++)
            builder.append('-');
        builder.append("-|-");
        for (int i = 0; i < rightWidth; i++)
            builder.append('-');
        for (int i = 0; i < maxLine; i++)
            builder.append(format(leftFormat, expectedInfo.get(i))).append(" | ").append(actualInfo.get(i));
        return builder.toString().trim();
    }

    public String detail() {
        return detail;
    }
}
