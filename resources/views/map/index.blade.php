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
                            const map = new OpenLayers.Map("map", {
                                controls: [
                                    new OpenLayers.Control.Navigation(),
                                    new OpenLayers.Control.ScaleLine(),
                                    new OpenLayers.Control.MousePosition(),
                                ],
                                projection: new OpenLayers.Projection("EPSG:900913"),
                                displayProjection: new OpenLayers.Projection("EPSG:4326")
                            });
                            const mapnik = new OpenLayers.Layer.OSM("OpenStreetMap (mapnik)");
                            map.addLayer(mapnik);
                            const lonLat = new OpenLayers.LonLat(15.645, 46.55)
                                .transform(
                                    new OpenLayers.Projection("EPSG:4326"), // transform from WGS 1984
                                    map.getProjectionObject() // to Spherical Mercator Projection
                                );
                            map.setCenter(lonLat, 14)
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
