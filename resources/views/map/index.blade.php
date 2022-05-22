@extends('layouts.map')

@section('content')
    <div class="container">
        <h1 class="text-center">
            Virutalno dirkališče po ulicah Maribora
        </h1>
        <div class="card">
            <div class="row text-dark">
                <div class="col-md-auto">
                    <div>
                        <div id="map" style="width:1000px; height:800px;"></div>
                        <script type="text/javascript">
                            console.log(ol)
                            const estates = {!! $estate !!}[0];
                            const vectorSource = new ol.source.Vector({
                                features: new ol.format.GeoJSON().readFeatures(estates),
                            });
                            console.log(vectorSource)

                            const image = new ol.style.Circle({
                                radius: 5,
                                fill: null,
                                stroke: new ol.style.Stroke({color: 'red', width: 1}),
                            });
                            const styles = {
                                'Point': new ol.style.Style({
                                    image: image,
                                }),
                                'LineString': new ol.style.Style({
                                    stroke: new ol.style.Stroke({
                                        color: 'green',
                                        width: 1,
                                    }),
                                }),
                                'MultiLineString': new ol.style.Style({
                                    stroke: new ol.style.Stroke({
                                        color: 'green',
                                        width: 1,
                                    }),
                                }),
                                'MultiPoint': new ol.style.Style({
                                    image: image,
                                }),
                                'MultiPolygon': new ol.style.Style({
                                    stroke: new ol.style.Stroke({
                                        color: 'rgba(0,0,255, 0.3)',
                                        width: 1,
                                    }),
                                    fill: new ol.style.Fill({
                                        color: 'rgba(0, 0, 255, 0.1)',
                                    }),
                                }),
                                'Polygon': new ol.style.Style({
                                    stroke: new ol.style.Stroke({
                                        color: 'blue',
                                        lineDash: [4],
                                        width: 3,
                                    }),
                                    fill: new ol.style.Fill({
                                        color: 'rgba(0, 0, 255, 0.1)',
                                    }),
                                }),
                                'GeometryCollection': new ol.style.Style({
                                    stroke: new ol.style.Stroke({
                                        color: 'magenta',
                                        width: 2,
                                    }),
                                    fill: new ol.style.Fill({
                                        color: 'magenta',
                                    }),
                                    image: new ol.style.Circle({
                                        radius: 10,
                                        fill: null,
                                        stroke: new ol.style.Stroke({
                                            color: 'magenta',
                                        }),
                                    }),
                                }),
                                'Circle': new ol.style.Style({
                                    stroke: new ol.style.Stroke({
                                        color: 'red',
                                        width: 2,
                                    }),
                                    fill: new ol.style.Fill({
                                        color: 'rgba(255,0,0,0.2)',
                                    }),
                                }),
                            };

                            const vectorLayer = new ol.layer.Vector({
                                source: vectorSource,
                                style: (feature) => {
                                    return styles[feature.getGeometry().getType()]
                                },
                            });
                            console.log(vectorLayer)

                            const map = new ol.Map({
                                target: 'map',
                                /*controls: [
                                    new ol.control.Control.Navigation(),
                                    new ol.control.Control.ScaleLine(),
                                    new ol.control.Control.MousePosition(),
                                ],*/
                                projection: new ol.proj.Projection("EPSG:900913"),
                                displayProjection: new ol.proj.Projection("EPSG:4326"),
                                layers: [
                                    new ol.layer.Tile({
                                        source: new ol.source.OSM(),
                                    }),
                                    vectorLayer
                                ],
                                view: new ol.View({
                                    projection: 'EPSG:4326',
                                    center: [15.645, 46.55],
                                    zoom: 13,
                                }),
                            });
                            //(15.645, 46.55)
                        </script>
                    </div>
                </div>
                <div class="col-md-auto">
                    <h1>Some text</h1>
                </div>
            </div>
        </div>
    </div>
@endsection
