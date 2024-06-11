package com.divit.springboot.application.controller;

import java.util.ArrayList;
import java.util.List;

public class DomainNameGenerator {

    private static final String[] PREFIXES = {
        "get", "go", "my", "try", "best", "pro", "new", "super", "high", "ultra",
        "mega", "micro", "hyper", "max", "prime","hotel" , "apartment" , "city" , "life", "gold"
    };

    private static final String[] WORDS = {
        "tech", "web", "app", "cloud", "data", "smart", "fast", "secure", "code",
        "future", "digital", "global", "dynamic", "innova", "tech", "net", "cyber",
        "power", "core", "system", "enterprise", "connect", "link", "wave", "stream",
        "matrix" ,"top" ,"buy" , "sell" , "book"  ,
    };

    private static final String[] SUFFIXES = {
        "hub", "base", "space", "zone", "world", "net", "solutions", "work",
        "lab", "place", "center", "link", "core", "point", "studio", "line",
        "network", "tech", "way", "path", "group", "media", "market", "domain",
        "express", "force", "focus", "sphere", "spot", "portal", "forge", "yard"
    };


    private static final String[] TLDs = {
        ".com"
    };

    public static void main(String[] args) {
        List<String> domainNames = generateDomainNames();
        for (String domain : domainNames) {
            System.out.println(domain);
        }
    }

    private static List<String> generateDomainNames() {
        List<String> domainNames = new ArrayList<>();

        // SUFFIXES + WORDS
        for (String suffix : SUFFIXES) {
            for (String word : WORDS) {
                for (String tld : TLDs) {
                    domainNames.add(suffix + word + tld);
                }
            }
        }

        // SUFFIXES + PREFIXES
        for (String suffix : SUFFIXES) {
            for (String prefix : PREFIXES) {
                for (String tld : TLDs) {
                    domainNames.add(suffix + prefix + tld);
                }
            }
        }

        // SUFFIXES + SUFFIXES
        for (String suffix1 : SUFFIXES) {
            for (String suffix2 : SUFFIXES) {
                for (String tld : TLDs) {
                    domainNames.add(suffix1 + suffix2 + tld);
                }
            }
        }

        // WORDS + PREFIXES
        for (String word : WORDS) {
            for (String prefix : PREFIXES) {
                for (String tld : TLDs) {
                    domainNames.add(word + prefix + tld);
                }
            }
        }

        // WORDS + SUFFIXES
        for (String word : WORDS) {
            for (String suffix : SUFFIXES) {
                for (String tld : TLDs) {
                    domainNames.add(word + suffix + tld);
                }
            }
        }

        // WORDS + WORDS
        for (String word1 : WORDS) {
            for (String word2 : WORDS) {
                if (!word1.equals(word2)) {
                    for (String tld : TLDs) {
                        domainNames.add(word1 + word2 + tld);
                    }
                }
            }
        }

        // PREFIXES + WORDS
        for (String prefix : PREFIXES) {
            for (String word : WORDS) {
                for (String tld : TLDs) {
                    domainNames.add(prefix + word + tld);
                }
            }
        }

        // PREFIXES + SUFFIXES
        for (String prefix : PREFIXES) {
            for (String suffix : SUFFIXES) {
                for (String tld : TLDs) {
                    domainNames.add(prefix + suffix + tld);
                }
            }
        }

        // PREFIXES + PREFIXES
        for (String prefix1 : PREFIXES) {
            for (String prefix2 : PREFIXES) {
                if (!prefix1.equals(prefix2)) {
                    for (String tld : TLDs) {
                        domainNames.add(prefix1 + prefix2 + tld);
                    }
                }
            }
        }

        return domainNames;
    }
}
