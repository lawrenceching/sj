package me.imlc;

import org.junit.jupiter.api.Test;
import static me.imlc.Sj.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SjTest {

    @Test
    void testExec() {
        String output = sh("""
                echo "ABC"
                """);

        assertEquals("ABC\n", output);
    }

    @Test
    void runCommandAndFail() {
        String output = sh("""
                commandnotfound
                """);

        assertEquals("ABC\n", output);
    }

    @Test
    void executeMultipleCommand() {
        String[] output = sh(
                true,
                "echo A",
                "echo B",
                "echo C",
                "bash /tmp/noexist.sh"
                );

        assertEquals("A\n", output[0]);
        assertEquals("B\n", output[1]);
        assertEquals("C\n", output[2]);
        assertThat(output[3]).contains("bash: /tmp/noexist.sh: No such file or directory");
    }
}