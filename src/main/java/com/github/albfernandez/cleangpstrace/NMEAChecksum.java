package com.github.albfernandez.cleangpstrace;

/*
 (C) Copyright 2014-2015 Alberto Fernández <infjaf@gmail.com>

 This file is part of cleangpstrace.

 cleangpstrace is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 cleangpstrace is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with cleangpstrace.  If not, see <http://www.gnu.org/licenses/>.
 */
public final class NMEAChecksum {

    private NMEAChecksum() {
        throw new AssertionError("No instances of this class are allowed");
    }

    public static boolean isValidCheckSum(final String line) {
        if ('*' != line.charAt(line.length() - 3)) {
            return false;
        }
        String checksum = line.substring(line.length() - 2);
        String checkString = line.substring(1, line.length() - 3);
        String calculated = calculateCheckSum(checkString);
        return calculated.equalsIgnoreCase(checksum);
    }

    public static String calculateCheckSum(final String checkString) {
        char value = 0;
        for (char c : checkString.toCharArray()) {
            value ^= c;
        }
        return toHexString(value);
    }

    public static String toHexString(final char c) {
        String tmp = Integer.toHexString(c);
        if (tmp.length() < 2) {
            tmp = "0" + tmp;
        }
        return tmp.toUpperCase();
    }
}
