#!/usr/bin/env sj
import static me.imlc.Sj.*;

public class Hello {
    public static void main(String[] args) {
        var dir = sh("pwd");
        println("Current location: %s".formatted(dir));
        println(sh("ls"));
    }
}