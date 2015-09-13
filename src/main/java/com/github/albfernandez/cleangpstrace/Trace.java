package com.github.albfernandez.cleangpstrace;

/*
 (C) Copyright 2014-2015 Alberto Fernández <infjaf@gmail.com>

 This file is part of cleangpstrace.

 Foobar is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Foobar is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.StringUtils;

public final class Trace {
    private List<Record> records = new ArrayList<>();

    public Trace() {
        super();
    }

    public static Trace load(final File file) throws IOException {
        if (file.getName().toLowerCase().endsWith(".gz")) {
            try (InputStream is = new GZIPInputStream(new FileInputStream(file))) {
                return load(is);
            }
        }
        try (InputStream is = new FileInputStream(file)) {
            return load(is);
        }
    }

    public static Trace load(final InputStream is) throws IOException {
        try (Reader reader = new InputStreamReader(is,
                StandardCharsets.US_ASCII)) {
            return load(reader);
        }
    }

    public static Trace load(final Reader reader1) throws IOException {
        Trace trace = new Trace();
        try (BufferedReader reader = new BufferedReader(reader1)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!StringUtils.isBlank(line)) {
                    Record r = Record.createRecord(line);
                    trace.addRecord(r);
                }
            }
        }
        trace.joinRecords();
        trace.removeUnorderedPoints();
        return trace;
    }

    private void removeUnorderedPoints() {
        List<Record> newRecords = new ArrayList<>();
        long lastTime = 0;
        for (Record r : this.records) {
            long recordTime = r.getTime();
            if (recordTime > lastTime) {
                lastTime = recordTime;
                newRecords.add(r);
            }
        }

        this.records = newRecords;
    }

    private void joinRecords() {
        List<Record> newRecords = new ArrayList<>();
        for (Record r : this.records) {
            if (r.isGGA() || r.isRMC()) {
                if (!newRecords.isEmpty()) {
                    Record last = newRecords.get(newRecords.size() - 1);
                    if (r.getTimeAsString().equals(last.getTimeAsString())) {
                        last.join(r);
                    } else {
                        newRecords.add(r);
                    }
                } else {
                    newRecords.add(r);
                }
            }
        }
        String lastValidDate = null;
        for (Record r : newRecords) {
            String recordDate = r.getDateAsString();
            if (!StringUtils.isBlank(recordDate)) {
                lastValidDate = recordDate;
            } else if (!StringUtils.isBlank(lastValidDate)) {
                r.setDateAsString(lastValidDate);
            }
        }

        this.records = newRecords;

    }

    public void addRecord(final Record r) {
        if (r != null) {
            this.records.add(r);
        }
    }

    public List<Record> getRecords() {
        return this.records;
    }

    public void setRecords(final List<Record> newRecords) {
        this.records = newRecords;
    }

    public String getTimestampAsString() {
        if (this.records.isEmpty()) {
            return "";
        }
        long time = this.records.get(0).getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date(time));

    }

}
