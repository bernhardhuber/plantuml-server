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
 * Plantuml example as uml-text representation.
 *
 * @author berni3
 */
public class Example {

    /**
     * Encapulate a puml example-entry, having decoded puml, and date of
     * createing this entry
     */
    public static class ExampleEntry implements Serializable {

        private static final long serialVersionUID = 20201212L;

        private final LocalDateTime createdWhen;
        private final String decoded;
        private final String description;

        public ExampleEntry(String aDecoded) {
            this(aDecoded, "", LocalDateTime.now());
        }

        public ExampleEntry(String aDecoded, String aDescription, LocalDateTime aCreatedWhen) {
            this.decoded = aDecoded;
            this.description = aDescription;
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

        public String getDescription() {
            return description;
        }

        public LocalDateTime getCreatedWhen() {
            return createdWhen;
        }

        @Override
        public String toString() {
            return "ExampleEntry{"
                    + "decoded=" + decoded
                    + "description=" + description
                    + ", createdWhen=" + createdWhen + '}';
        }

        /**
         * Factory creator for {@link HistroyEntry} comparator. Used also as
         * flexible is-equal method.
         *
         * @return
         */
        static Comparator<ExampleEntry> createComparator() {
            return new Comparator<ExampleEntry>() {
                @Override
                public int compare(ExampleEntry he1, ExampleEntry he2) {
                    // compare only encoded, decoded, ignore createdWhen
                    if (he1 == null) {
                        he1 = new ExampleEntry("");
                    }
                    if (he2 == null) {
                        he2 = new ExampleEntry("");
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
     * Repository of {@link  ExampleEntry]
     */
    public static class ExampleEntryRepository {

        private final List<ExampleEntry> exampleEntryList;

        public ExampleEntryRepository() {
            this.exampleEntryList = new ArrayList<>();

        }

        public ExampleEntry add(ExampleEntry newExampleEntry) {
            if (!exampleEntryListContains(newExampleEntry)) {
                exampleEntryList.add(0, newExampleEntry);
            }
            return newExampleEntry;
        }

        public List<ExampleEntry> fetchAllExampleEntry() {
            final List<ExampleEntry> result
                    = Collections.unmodifiableList(exampleEntryList);
            return result;
        }

        private boolean exampleEntryListContains(ExampleEntry he) {
            final Comparator<ExampleEntry> comparatorExampleEntry = ExampleEntry.createComparator();
            boolean isDup = false;
            for (int i = 0; !isDup && i < this.exampleEntryList.size(); i++) {
                final ExampleEntry heFromList = this.exampleEntryList.get(i);
                isDup = (comparatorExampleEntry.compare(he, heFromList) == 0);
            }
            return isDup;
        }
    }

    public static class ExampleEntryRepositoryFactory {

        public void setupFromExampleEntryPeristantStore(ExampleEntryRepository eer) {
            final LocalDateTime now = LocalDateTime.now();
            final ExampleEntryPeristantStore exampleEntryPeristantStore
                    = new ExampleEntryPeristantStore();
            for (String[] example : exampleEntryPeristantStore.allSnippets()) {
                final String aDecoded = example[0];
                final String aDescription = example[1];
                final ExampleEntry newExampleEntry = new ExampleEntry(aDecoded, aDescription, now);
                eer.add(newExampleEntry);
            }
        }
    }

    public static class ExampleEntryPeristantStore {

        private final List<String[]> examples;

        public ExampleEntryPeristantStore() {
            this.examples = Arrays.asList(
                    new String[]{
                        "@startuml\nBob -> Alice\n@enduml\n",
                        "simple sequence diagram"
                    },
                    new String[]{
                        "@startuml\nclass Foo\nclass Bar\nFoo -> Bar\n@enduml\n",
                        "simple class digram"
                    }
            );
        }

        public List<String[]> allSnippets() {
            final List<String[]> result = Collections.unmodifiableList(examples);
            return result;
        }

    }
}
