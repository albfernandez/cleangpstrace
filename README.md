# cleangpstrace

Takes a file with nmea data and split it in diferent files attending times where the receiver where off.
It also optionally cleans the data, removing points in defined areas or simplifying points on a route.


# usage


    java -jar cleangpstrace-1.0-jar-with-dependencies.jar source_nmea.txt

## Options

    --exclude-areas shapefile
Indicates the shapefile that contains areas which should be removed from source. (WGS84)

    --prefix prefix
Prefix to use on the output files

    --output-dir dir
Directory where the output files will be stored

    --seconds-to-split seconds
Number of seconds without data needed to start a new file

    --skip-simplify
Skip the simplification (reduction of points) in the trace

    --gps-week-rollover
Set the "gps week epoch"
 - -1 autodectect
 - 0 from 1980-01-06 to 1999-08-21
 - 1 from 1999-08-22 to 2019-04-06
 - 2 from 2019-04-07 to 2039-04-06
    
    