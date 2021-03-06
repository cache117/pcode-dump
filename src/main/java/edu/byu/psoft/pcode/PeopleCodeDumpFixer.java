package edu.byu.psoft.pcode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PeopleCodeDumpFixer
{
    public static void main(String[] args)
    {
        long startTime = System.nanoTime();
        System.out.println("Start Time = " + startTime);
        try (Stream<String> lines = Files.lines(Paths.get(args[0]));
             PrintWriter writer = new PrintWriter(args[1], "UTF-8")
        )
        {
            lines
                    .map(PeopleCodeDumpFixer::translateLine)
                    .forEachOrdered(writer::println);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        System.out.println("End Time = " + endTime);
        System.out.println("Elapsed Time = " + Long.toString(endTime - startTime));
    }

    private static String translateLine(String line)
    {
        if (line.matches("\\[[a-zA-Z0-9_]*\\.[a-zA-Z0-9_.]*OnExecute\\]"))
        {
            String newLine = line.substring(0, line.lastIndexOf("."));
            return newLine.replaceAll("\\.", ":") + "]";
        }
        if (line.matches("\\[[a-zA-Z0-9_]*\\.[a-zA-Z0-9_]*\\.FieldFormula\\]"))
        {
            String newLine = line.substring(0, line.lastIndexOf("."));
            return newLine + " FieldFormula]";
        }
        else
        {
            return line;
        }
    }
}
