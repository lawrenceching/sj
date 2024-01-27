package me.imlc;

import org.junit.jupiter.api.Test;
import static me.imlc.Sj.*;
import static org.junit.jupiter.api.Assertions.*;

class SjTest {

    @Test
    void testExec() {
        String output = sh("""
                echo "ABC"
                """);

        assertEquals("ABC\n", output);
    }
}