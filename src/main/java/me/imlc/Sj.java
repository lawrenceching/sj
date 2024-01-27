package me.imlc;

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
public class Sj {

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

            Process process = null;
            process = Runtime.getRuntime().exec("bash %s".formatted(tmpFilePath.toAbsolutePath().toString()));

            if (echo) {
                inputStreamToConsole(process.getInputStream());
            }
            inputStreamToConsole(process.getErrorStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());

            }
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

    public static void run(Runnable r) {
        vtExecutor.execute(r);
    }

    public static void inputStreamToConsole(InputStream is) {
        vtExecutor.execute(() -> {
            String line;
            try {
                BufferedReader errReader = new BufferedReader(new InputStreamReader(is));
                while ((line = errReader.readLine()) != null) {
                    println(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static String toGitBashPath(String path) {
        return "/" + path.replace("\\", "/").replace(":", "");
    }


}