package ca.concordia.soen7481.assignment;

import ca.concordia.soen7481.assignment.bugpatterns.BugPattern;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toMap;

/**
 * A Util class to handle formatting and writing the results in a text file
 */
public class FileUtil {
    /**
     * Append String in a file
     * @param content content to add
     * @throws IOException if we cannot access the file
     */
    static void writeInFile(String content) throws IOException {
        try (FileWriter fw = new FileWriter("results/report.html", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(content);
        }
    }

    /**
     * Delete files from the results folder
     * Keep the .gitkeep
     */
    static void deleteFiles() {
        File files = new File("results/");
        for (File file: Objects.requireNonNull(files.listFiles())) {
            if(!file.getName().equals(".gitkeep")) {
                file.delete();
            }
        }
    }

    /**
     * Delete a specific file from the results folder
     * @param filename filename
     */
    static void deleteFileName(String filename) {
        File files = new File("results/");
        for (File file: Objects.requireNonNull(files.listFiles())) {
            if(file.getName().equals(filename)) {
                file.delete();
            }
        }
    }

    /**
     * Replace the variables for the report
     */
    static void generateReport(List<BugPattern> bugPatterns) {
        try {
            String htmlString = new String(Files.readAllBytes(Paths.get("resources/report-template.html")),
                    StandardCharsets.UTF_8);

            Collection<BugPattern> bugPatternsUniqueInstances = bugPatterns.stream().collect(toMap(BugPattern::getIdentifier, p -> p, (p, q) -> p)).values();

            StringBuilder bugSummaryTable = new StringBuilder();
            AtomicInteger counter = new AtomicInteger(0);
            bugPatternsUniqueInstances.forEach(bug -> {
                String evenOdd = (counter.get() % 2 == 0) ? "1" : "0";
                bugSummaryTable.append("<tr class=\"tablerow"+evenOdd+"\"><td><a href=\"#"+bug.getIdentifier()+"\">"
                        +bug.getIdentifier()+": "+bug.getDescription()+"</a></td><td align=\"right\">" +
                        bugPatterns.stream().filter(bug.getClass()::isInstance).count() +
                        "</td></tr>");
                counter.getAndIncrement();
            });

            StringBuilder bugsPatternsTable = new StringBuilder();
            AtomicInteger counter2 = new AtomicInteger(0);
            bugPatterns.forEach(bug -> {
                String evenOdd = (counter2.get() % 2 == 0) ? "1" : "0";
                bugsPatternsTable.append("<tr onclick=\"toggleRow('row"+counter2.get()+"');\" class=\"tablerow"+evenOdd+"\">\n" +
                        "                    <td>\n" +
                        "                    <span>"+bug.getIdentifier()+"</span>\n" +
                        "                    </td>\n" +
                        "                    <td>"+bug.getPath()+": "+bug.getDescription()+"</td>\n" +
                        "                </tr>\n" +
                        "                <tr class=\"detailrow"+evenOdd+"\">\n" +
                        "                    <td>\n" +
                        "                        </td><td>\n" +
                        "                        <p style=\"display: none;\" id=\"row"+counter2.get()+"\">\n" +
                        "                            <a href=\"#"+bug.getIdentifier()+"\">Bug type "+bug.getName()+" (click for details)</a>\n" +
                        "                            <br>In file "+bug.getFile()+"<br>In method \""+bug.getFunctionName()+"\"<br>At line "+bug.getLine()+"\n" +
                        "                        </p>\n" +
                        "                    </td>\n" +
                        "                </tr>");
                counter2.getAndIncrement();
            });

            htmlString = htmlString.replace("$bugsSummmaryTable", bugSummaryTable);
            htmlString = htmlString.replace("$totalNumberOfBugs", String.valueOf(bugPatterns.size()));
            htmlString = htmlString.replace("$bugPatternsTable", bugsPatternsTable);

            writeInFile(htmlString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Credits: https://stackoverflow.com/a/27872852
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
