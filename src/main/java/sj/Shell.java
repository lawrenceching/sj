package sj;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The Sj class provides utility methods for executing shell commands and running tasks in virtual threads.
 */
public class Shell {

    private static Executor vtExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public static void println(String msg) {
        System.out.println(msg);
    }



    /**
     * Executes a shell command and returns the output as a string.
     *
     * @param command The shell command to execute.
     * @return The output of the shell command as a string.
     * @throws RuntimeException if there is an error executing the command or creating a temporary file.
     */
    public static String sh(String command) {
        return sh(command, false);
    }


    /**
     * Executes a shell command and returns the output as a string.
     *
     * @param command The shell command to execute.
     * @param echo    Flag indicating whether to echo the command's output to the console.
     * @return The output of the shell command as a string.
     * @throws RuntimeException if there is an error executing the command or creating a temporary file.
     */
    public static String sh(String command, boolean echo) {

        try {

            Path tmpFilePath;

            try {
                tmpFilePath = Files.createTempFile("sj", null);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create temp file", e);
            }
            Files.writeString(tmpFilePath, command);

            StringBuffer sb = new StringBuffer();

            final Process process = Runtime.getRuntime().exec("bash %s".formatted(tmpFilePath.toAbsolutePath().toString()));

            CompletableFuture.allOf(
                    run(() -> {
                        inputStreamToStringBuffer(process.getErrorStream(), sb, true);
                    }),
                    run(() -> {
                        inputStreamToStringBuffer(process.getInputStream(), sb, echo);
                    })
            ).join();

            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String[] sh(String ... commands) {
        return sh(false, commands);
    }

    public static String[] sh(boolean echo, String ... commands) {
        final String[] output = new String[commands.length];

        final CompletableFuture[] all = new CompletableFuture[commands.length];


        for (int i = 0; i < commands.length; i++) {
            final int fi = i;
            CompletableFuture<String> f = new CompletableFuture<>();
            f.completeAsync(() -> {
                var o = sh(commands[fi], echo);
                output[fi] = o;
                return o;
            }, vtExecutor);
            all[i] = f;
        }

        CompletableFuture.allOf(all).join();

        return output;
    }

    public static CompletableFuture<Void> run(Runnable r) {
        return CompletableFuture.runAsync(r, vtExecutor);
    }

    public static void inputStreamToStringBuffer(InputStream is, StringBuffer sb, boolean echo) {
        try {
            String line;
            BufferedReader errReader = new BufferedReader(new InputStreamReader(is));
            while ((line = errReader.readLine()) != null) {
                if (echo) {
                    println(line);
                }

                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toGitBashPath(String path) {
        return "/" + path.replace("\\", "/").replace(":", "");
    }


    private static final String[] OPTIONS = {"apple", "banana"};
    private static int selectedOption = 0;

    private static final int KEY_SPACE = 32;
    private static final int KEY_ESCAPE = 27;
    private static final int KEY_ARROW_RIGHT = 67;
    private static final int KEY_ARROW_LEFT = 68;

}