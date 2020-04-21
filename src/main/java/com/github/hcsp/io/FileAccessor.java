package com.github.hcsp.io;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileAccessor {
    public static List<String> readFile1(File file) throws IOException {
        return FileUtils.readLines(file, Charset.defaultCharset());
    }

    public static List<String> readFile2(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        StringBuilder result = new StringBuilder();
        int byteInt;
        while ((byteInt = in.read()) != -1) {
            result.append((char) byteInt);
        }
        in.close();
        return Arrays.asList(result.toString().split("\\r\\n"));
    }

    public static List<String> readFile3(File file) throws IOException {
        List<String> rl = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            rl.add(str);
        }
        bufferedReader.close();
        return rl;
    }

    public static List<String> readFile4(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    public static void writeLinesToFile1(List<String> lines, File file) throws IOException {
        FileUtils.writeLines(file, lines);
    }

    public static void writeLinesToFile2(List<String> lines, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        out.write(String.join("\r\n", lines).getBytes());
        out.close();
    }

    public static void writeLinesToFile3(List<String> lines, File file) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(String.join("\r\n", lines));
        bufferedWriter.close();
    }

    public static void writeLinesByFiles(List<String> lines, File file) throws IOException {
        Files.write(file.toPath(), lines, Charset.defaultCharset());
    }

    public static void main(String[] args) throws IOException {
        File projectDir = new File(System.getProperty("basedir", System.getProperty("user.dir")));
        File testFile = new File(projectDir, "target/test.txt");
        List<String> lines = Arrays.asList("AAA", "BBB", "CCC");
        writeLinesToFile1(lines, testFile);
        writeLinesToFile2(lines, testFile);
        writeLinesToFile3(lines, testFile);
        writeLinesByFiles(lines, testFile);

        System.out.println(readFile1(testFile));
        System.out.println(readFile2(testFile));
        System.out.println(readFile3(testFile));
        System.out.println(readFile4(testFile));
    }
}

