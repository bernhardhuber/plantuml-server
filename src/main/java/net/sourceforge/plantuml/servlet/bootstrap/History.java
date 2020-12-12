/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.plantuml.servlet.bootstrap;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author berni3
 */
public class History {

    /**
     * Encapulate a puml history-entry, having decoded, and encoded puml, and
     * date of createing this entry
     */
    public static class HistoryEntry implements Serializable {

        private static final long serialVersionUID = 20201212L;

        private final LocalDateTime createdWhen;
        private final String encoded;
        private final String decoded;

        public HistoryEntry(String aEncoded, String aDecoded) {
            this(aEncoded, aDecoded, LocalDateTime.now());
        }

        public HistoryEntry(String aEncoded, String aDecoded, LocalDateTime aCreatedWhen) {
            this.encoded = aEncoded;
            this.decoded = aDecoded;
            this.createdWhen = aCreatedWhen;
        }

        String[] defaultToEmpty() {
            String[] result = new String[]{
                this.encoded != null ? this.encoded : "",
                this.decoded != null ? this.decoded : ""
            };
            return result;
        }

        public String getEncoded() {
            return encoded;
        }

        public String getDecoded() {
            return decoded;
        }

        public LocalDateTime getCreatedWhen() {
            return createdWhen;
        }

        @Override
        public String toString() {
            return "HistoryEntry{"
                    + "encoded=" + encoded
                    + ", decoded=" + decoded
                    + ", createdWhen=" + createdWhen + '}';
        }

        /**
         * Factory creator for {@link HistroyEntry} comparator. Used also as
         * flexible is-equal method.
         *
         * @return
         */
        static Comparator<HistoryEntry> createComparator() {
            return new Comparator<HistoryEntry>() {
                @Override
                public int compare(HistoryEntry he1, HistoryEntry he2) {
                    // compare only encoded, decoded, ignore createdWhen
                    if (he1 == null) {
                        he1 = new HistoryEntry("", "");
                    }
                    if (he2 == null) {
                        he2 = new HistoryEntry("", "");
                    }
                    final String[] he1DefaultToEmpty = he1.defaultToEmpty();
                    final String[] he2DefaultToEmpty = he2.defaultToEmpty();
                    final String he1CompareVal = he1DefaultToEmpty[0] + he1DefaultToEmpty[1];
                    final String he2CompareVal = he2DefaultToEmpty[0] + he2DefaultToEmpty[1];
                    int compareResult = he1CompareVal.compareTo(he2CompareVal);
                    return compareResult;
                }
            };
        }
    }

    /**
     * Repository of {@link  HistoryEntry]
     */
    static class HistoryEntryRepository {

        private final List<HistoryEntry> historyEntryList = new ArrayList<>();

        public HistoryEntry add(String encoded, String decoded) {
            final HistoryEntry he = new HistoryEntry(encoded, decoded);
            if (!historyEntryListContains(he)) {
                historyEntryList.add(0, he);
            }
            return he;
        }

        public List<HistoryEntry> fetchAllHistoryEntry() {
            final List<HistoryEntry> result = Collections.unmodifiableList(historyEntryList);
            return result;
        }

        private boolean historyEntryListContains(HistoryEntry he) {
            final Comparator<HistoryEntry> comparatorHistoryEntry = HistoryEntry.createComparator();
            boolean isDup = false;
            for (int i = 0; !isDup && i < this.historyEntryList.size(); i++) {
                final HistoryEntry heFromList = this.historyEntryList.get(i);
                isDup = (comparatorHistoryEntry.compare(he, heFromList) == 0);
            }
            return isDup;
        }

    }
}
