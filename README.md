# cleangpstrace

Takes a file with nmea data and split it in diferent files attending times where the receiver where off.
It also optionally cleans the data, removing points in defined areas or simplifying points on a route.


# usage


    java -jar cleangpstrace-1.0-jar-with-dependencies.jar source_nmea.txt

## Options

    --exclude-areas shapefile
Indicates de shapefile that contains areas which should be removed from source. (WGS84)

    --prefix prefix
Prefix to use on the output files

    --output-dir dir
Directory where the output files will be stored

    --seconds-to-split seconds
Number of seconds without data needed to start a new file

    --skip-simplify
Skip the simplication (reduction of points) in the trace
