deegree-layer-mapnik
====================

deegree layer which renders layers using the mapnik renderer.

Prerequisites are that you have mapnik installed and working, and the
mapnik-jni library compiled. The proj data file is currently expected
to be at /usr/share/proj/epsg.

Put the libmapnik-jni.so into the modules directory of your workspace,
you can also put the mapnik-jni.jar and the deegree-layer-mapnik.jar
there.

The configuration format is very simple, just specify the path to your
mapnik XML configuration style for each layer:

    <?xml version="1.0"?>
    <MapnikLayers xmlns="http://www.deegree.org/layers/mapnik" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:l="http://www.deegree.org/layers/base" xmlns:d="http://www.deegree.org/metadata/description" xmlns:s="http://www.deegree.org/metadata/spatial" xsi:schemaLocation="http://www.deegree.org/layers/mapnik mapnik.xsd" configVersion="3.4.0">
      <MapnikLayer>
        <MapnikConfiguration>../osm.mml</MapnikConfiguration>
        <l:Name>osm</l:Name>
        <d:Title>OSM</d:Title>
        <s:CRS>EPSG:25832</s:CRS>
      </MapnikLayer>
    </MapnikLayers>
