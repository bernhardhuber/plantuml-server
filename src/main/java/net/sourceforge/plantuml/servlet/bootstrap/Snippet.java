/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Plantuml snippet as uml-text representation.
 *
 * @author berni3
 */
public class Snippet {

    /**
     * Encapulate a puml snippet-entry, having decoded puml, and date of
     * createing this entry
     */
    public static class SnippetEntry implements Serializable {

        private static final long serialVersionUID = 20201212L;

        private final LocalDateTime createdWhen;
        private final String decoded;

        public SnippetEntry(String aDecoded) {
            this(aDecoded, LocalDateTime.now());
        }

        public SnippetEntry(String aDecoded, LocalDateTime aCreatedWhen) {
            this.decoded = aDecoded;
            this.createdWhen = aCreatedWhen;
        }

        String[] defaultToEmpty() {
            String[] result = new String[]{
                this.decoded != null ? this.decoded : ""
            };
            return result;
        }

        public String getDecoded() {
            return decoded;
        }

        public LocalDateTime getCreatedWhen() {
            return createdWhen;
        }

        @Override
        public String toString() {
            return "SnippetEntry{" + "decoded=" + decoded + ", createdWhen=" + createdWhen + '}';
        }

        /**
         * Factory creator for {@link HistroyEntry} comparator. Used also as
         * flexible is-equal method.
         *
         * @return
         */
        static Comparator<SnippetEntry> createComparator() {
            return new Comparator<SnippetEntry>() {
                @Override
                public int compare(SnippetEntry he1, SnippetEntry he2) {
                    // compare only encoded, decoded, ignore createdWhen
                    if (he1 == null) {
                        he1 = new SnippetEntry("");
                    }
                    if (he2 == null) {
                        he2 = new SnippetEntry("");
                    }
                    final String[] se1DefaultToEmpty = he1.defaultToEmpty();
                    final String[] se2DefaultToEmpty = he2.defaultToEmpty();
                    final String se1CompareVal = se1DefaultToEmpty[0];
                    final String se2CompareVal = se2DefaultToEmpty[0];
                    int compareResult = se1CompareVal.compareTo(se2CompareVal);
                    return compareResult;
                }
            };
        }
    }

    /**
     * Repository of {@link  SnippetEntry]
     */
    public static class SnippetEntryRepository {

        private final List<SnippetEntry> snippetEntryList = new ArrayList<>();

        public SnippetEntry add(String decoded) {
            final SnippetEntry snippetEntry = new SnippetEntry(decoded);
            if (!snippetEntryListContains(snippetEntry)) {
                snippetEntryList.add(0, snippetEntry);
            }
            return snippetEntry;
        }

        public List<SnippetEntry> fetchAllSnippetEntry() {
            final List<SnippetEntry> result = Collections.unmodifiableList(snippetEntryList);
            return result;
        }

        private boolean snippetEntryListContains(SnippetEntry he) {
            final Comparator<SnippetEntry> comparatorSnippetEntry = SnippetEntry.createComparator();
            boolean isDup = false;
            for (int i = 0; !isDup && i < this.snippetEntryList.size(); i++) {
                final SnippetEntry heFromList = this.snippetEntryList.get(i);
                isDup = (comparatorSnippetEntry.compare(he, heFromList) == 0);
            }
            return isDup;
        }
    }

    public static class SnippetEntryRepositoryFactory {

        public void setupFromSnippetEntryPeristantStore(SnippetEntryRepository eer) {
            SnippetEntryPeristantStore snippetEntryPeristantStore
                    = new SnippetEntryPeristantStore();
            for (String snippet : snippetEntryPeristantStore.allSnippets()) {
                eer.add(snippet);
            }
        }
    }

    public static class SnippetEntryPeristantStore {

        private final List<String> snippets;

        public SnippetEntryPeristantStore() {
            snippets = Arrays.asList(
                    "skinparam monochrome true"
                    + "skinparam handwritten true");
        }

        public List<String> allSnippets() {
            final List<String> result = Collections.unmodifiableList(snippets);
            return result;
        }

    }
}
