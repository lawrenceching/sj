#!/usr/bin/env sj
import static sj.Shell.*;

public class Hello {
    public static void main(String[] args) {
        sh("echo 'Hello, world!'", true);
    }
}