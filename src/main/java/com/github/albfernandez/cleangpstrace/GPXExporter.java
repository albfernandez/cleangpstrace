package com.github.albfernandez.cleangpstrace;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;



public final class GPXExporter {
    

    private Trace trace = null;
    private SimpleDateFormat sdf;
    
    private double maxLat = -5000;
    private double maxLon = -5000;
    private double minLat = 5000;
    private double minLon = 5000;
    
    public GPXExporter(Trace trace) {
    	super();
        this.trace = trace;
        this.sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
    }
    
    public void export(File output, boolean compress) throws Exception   {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = dbf.newDocumentBuilder().newDocument(); 
        Element gpx = doc.createElement("gpx");        
        gpx.setAttribute("version", "1.1");
        gpx.setAttribute("creator", "CleanGPSTrace");
        doc.appendChild(gpx);
        
        Element track = doc.createElement("trk");
        gpx.appendChild(track);
        
        Element segment = doc.createElement("trkseg");
        track.appendChild(segment);
        
        for (Record r: this.trace.getRecords()) {
            Element point = doc.createElement("trkpt");
            segment.appendChild(point);
            double lat = r.getLat();
            double lon = r.getLon();
            this.minLat = Math.min(this.minLat, lat);
            this.minLon = Math.min(this.minLon, lon);
            this.maxLat = Math.max(this.maxLat, lat);
            this.maxLon = Math.max(this.maxLon, lon);
            point.setAttribute("lat", Double.toString(r.getLat()));
            point.setAttribute("lon", Double.toString(r.getLon()));
            
            if (r.getAltitude() != null) {
                Element elevation = doc.createElement("ele");
                elevation.setTextContent(r.getAltitude().toString());
                point.appendChild(elevation);
            }
            Element timepoint = doc.createElement("time");
            timepoint.setTextContent(sdf.format(new Date(r.getTime())));
            point.appendChild(timepoint);            
        }
        Element metadata = createMetadataNode(gpx);
        gpx.insertBefore(metadata, track);
        try (OutputStream os = FileUtils.createOutputStream(output, compress)) {
        	toXML(doc, os);
        }
    }
    private Element createMetadataNode(Element gpx) {
        Document doc = gpx.getOwnerDocument();
        Element metadata = doc.createElement("metadata");        
        Element time = doc.createElement("time");
        time.setTextContent(fomatDate(new Date()));
        metadata.appendChild(time);
        Element bounds = doc.createElement("bounds");
        bounds.setAttribute("minlat", String.valueOf(minLat));
        bounds.setAttribute("minlon", String.valueOf(minLon));
        bounds.setAttribute("maxlat", String.valueOf(maxLat));
        bounds.setAttribute("maxLon", String.valueOf(maxLon));
        metadata.appendChild(bounds);
        
        return metadata;
    }
    private String fomatDate(Date date) {
        return sdf.format(date);
    }
    private void toXML(Document xmlDoc, OutputStream os) throws TransformerException  {
        DOMSource domSource = new DOMSource(xmlDoc);
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        // Unless a width is set, there will be only line breaks but no
        // indentation.
        // The IBM JDK and the Sun JDK don't agree on the property name,
        // so we set them both.
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        transformer.transform(domSource, new StreamResult(os));
    }

}



/*
<?xml version="1.0" encoding="UTF-8"?>
<gpx version="1.0" creator="GPSBabel - http://www.gpsbabel.org" xmlns="http://www.topografix.com/GPX/1/0">
  <time>2019-07-12T14:30:11.940Z</time>
  <bounds minlat="42.345375839" minlon="-4.969667144" maxlat="42.384899724" maxlon="-4.935617700"/>
  <trk>
    <name>SkyTraq tracklog</name>
    <desc>SkyTraq GPS tracklog data</desc>
    <trkseg>
      <trkpt lat="42.363401397" lon="-4.951342457">
        <ele>820.134219</ele>
        <time>1999-11-24T15:09:16Z</time>
        <speed>0.000000</speed>
        <name>TP0001</name>
      </trkpt>
*/