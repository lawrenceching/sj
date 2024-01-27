package me.imlc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Sj {

    private static Executor vtExecutor = Executors.newVirtualThreadPerTaskExecutor();

    public static void println(String msg) {
        System.out.println(msg);
    }


    public static String sh(String command) {

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
            stderrToConsole(process);
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

    public static void run(Runnable r) {
        vtExecutor.execute(r);
    }

    public static void stderrToConsole(Process process) {
        vtExecutor.execute(() -> {
            String line;
            try {
                BufferedReader errReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
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