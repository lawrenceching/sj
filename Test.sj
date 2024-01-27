#!/usr/bin/env sj
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static sj.Shell.*;

public class Test {
    public static void main(String[] args) {
        var files = sh("ls examples").split("\n");
        List<String> sjFiles = Arrays.stream(files).filter(f -> f.endsWith(".sj")).filter(f -> !f.equals("Test.sj")).collect(Collectors.toList());

        for (String sj : sjFiles) {
            println("--- %s".formatted(sj));
            println(sh("time ./examples/%s".formatted(sj)));
        }
    }
}