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
import java.io.File;
import java.io.IOException;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public final class RemoveUnwantedZonesFilter {

    private FileDataStore store;
    private SimpleFeatureSource featureSource;
    private FilterFactory2 filterFactory;
    private String geometryPropertyName;
    private CoordinateReferenceSystem targetCRS;
    private String cleanZones;

    public RemoveUnwantedZonesFilter(final String excludeZones) {
        this.cleanZones = excludeZones;
        initDataStore();
    }

    private void initDataStore() {
        try {
            this.store = FileDataStoreFinder.getDataStore(new File(
                    this.cleanZones));
            this.featureSource = this.store.getFeatureSource();
            this.filterFactory = CommonFactoryFinder.getFilterFactory2(GeoTools
                    .getDefaultHints());
            this.geometryPropertyName = this.featureSource.getSchema()
                    .getGeometryDescriptor().getLocalName();
            this.targetCRS = this.featureSource.getSchema()
                    .getGeometryDescriptor().getCoordinateReferenceSystem();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Trace cleanTrace(final Trace trace) {
        Trace newTrace = new Trace();
        for (Record record : trace.getRecords()) {
            if (isValidRecord(record)) {
                newTrace.addRecord(record);
            }
        }
        return newTrace;
    }

    private boolean isValidRecord(final Record record) {
        return cuentaIncidencias(record.getLat(), record.getLon()) == 0;
    }

    private long cuentaIncidencias(final double latitud, final double longitud) {
        try {

            ReferencedEnvelope bbox = new ReferencedEnvelope(longitud,
                    longitud + 0.00001, latitud, latitud + 0.00001,
                    this.targetCRS);
            Filter filter = this.filterFactory.bbox(
                    this.filterFactory.property(this.geometryPropertyName),
                    bbox);
            SimpleFeatureCollection lista = this.featureSource
                    .getFeatures(filter);
            return lista.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void dispose() {
        this.store.dispose();
    }
}
