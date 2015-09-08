package com.github.albfernandez.cleangpstrace;

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

public class RemoveUnwantedZonesFilter {
        
    private FileDataStore store;
    private SimpleFeatureSource featureSource;
    private FilterFactory2 filterFactory;
    String geometryPropertyName;
    CoordinateReferenceSystem targetCRS;
    //"/home/alberto/uDigWorkspace/zonas_limpia.shp"
    private String cleanZones;
    
    public RemoveUnwantedZonesFilter(String cleanZones) {
    	this.cleanZones = cleanZones;
    	initDataStore();
    }
    
    
    
    private void initDataStore() {
    	try {
            this.store = FileDataStoreFinder.getDataStore(new File(this.cleanZones));            
            this.featureSource = this.store.getFeatureSource();
            this.filterFactory = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints()); 
            this.geometryPropertyName = this.featureSource.getSchema().getGeometryDescriptor().getLocalName();
            this.targetCRS = this.featureSource.getSchema().getGeometryDescriptor().getCoordinateReferenceSystem();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    

    
    public Trace cleanTrace (Trace trace) {
    	Trace newTrace = new Trace();
    	for(Record record: trace.getRecords()){
    		if (isValidRecord(record)){
    			newTrace.addRecord(record);
    		}
    	}
    	return newTrace;
    }
    

	private boolean isValidRecord(Record record) {
    	return cuentaIncidencias(record.getLat(), record.getLon()) == 0;
	}




	private long cuentaIncidencias(double latitud, double longitud) {
		try {

			ReferencedEnvelope bbox = new ReferencedEnvelope(longitud,
					longitud + 0.00001, latitud, latitud + 0.00001, this.targetCRS);
			Filter filter = this.filterFactory.bbox(
					this.filterFactory.property(this.geometryPropertyName), bbox);
			SimpleFeatureCollection lista = this.featureSource.getFeatures(filter);
			return lista.size();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
    


    
    public void dispose(){
        this.store.dispose();
    }



}