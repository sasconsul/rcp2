package com.sasconsul.rcp.service;

    import org.jsoup.Jsoup;
    import org.jsoup.helper.Validate;
    import org.jsoup.nodes.Document;
    import org.jsoup.nodes.Entities;
    import org.jsoup.safety.Cleaner;
    import org.jsoup.safety.Whitelist;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import java.io.IOException;
    import java.util.*;

/**
 * program to count the frequency of words from a URL (page).
 */
public class PageWordCount {
    private static final Logger log = LoggerFactory.getLogger(PageWordCount.class);

    private static HashMap<String, Long> wordCount = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String url = args[0];
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();

        Document.OutputSettings os = new Document.OutputSettings();
        os.prettyPrint(false);
        os.escapeMode(Entities.EscapeMode.extended);
        os.charset("ascii");

        Cleaner cleaner = new Cleaner(Whitelist.none());
        Document cleanDoc = cleaner.clean(doc).outputSettings(os);
        String[] wordList = cleanDoc.text().split("\\W");
        for (String key : wordList) {
            key = key.toLowerCase();
            if (key.isEmpty()) {
                continue;
            }
            if (wordCount.containsKey(key)) {
                wordCount.put(key, wordCount.get(key) + 1);
            } else {
                wordCount.put(key, 1L);
            }
        }

        print("=== frequency ===");
        print("%s\n",wordCount.entrySet());

        // A comparator that compares two map entries based on their value
        Comparator<Map.Entry<String, Long>> comparator =
            (e0, e1) -> Long.compare(e0.getValue(), e1.getValue());

        TreeSet<Map.Entry<String, Long>> entryList = new TreeSet<>(comparator);
        wordCount.entrySet().forEach(entry -> entryList.add(entry));

        entryList.forEach(entry -> print(" w: |%s| f: %d ", entry.getKey(), entry.getValue()));
    }

    private static void print(String msg, Object... args) {
        log.info(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }
}
