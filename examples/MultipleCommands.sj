#!/usr/bin/env sj
import static me.imlc.Sj.*;

public class Hello {
    public static void main(String[] args) {
        sh(
                true,
                "sleep 5; echo Command 1;",
                "sleep 2; echo Command 2;",
                "sleep 3; echo Command 3;",
                "sleep 4; echo Command 4;"
        );
    }
}