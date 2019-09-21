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
public final class IgnoreLineException extends Exception {

    private static final long serialVersionUID = 9166222770371847804L;

    public IgnoreLineException() {
        super();
    }

    public IgnoreLineException(final String message) {
        super(message);
    }

    public IgnoreLineException(final Throwable cause) {
        super(cause);
    }

    public IgnoreLineException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
