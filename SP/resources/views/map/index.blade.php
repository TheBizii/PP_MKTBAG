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
                        <form class="form-inline">
                            <label>Action type &nbsp;</label>
                            <select id="type" class="form-control">
                                <option value="click1" selected>Track layout 1</option>
                                <option value="click2">Track layout 2</option>
                                <option value="click3">Track layout 3</option>
                                <option value="click4">Track layout 4</option>
                                <option value="click5">Track layout 5</option>
                                <option value="none">None</option>
                            </select>
                            <span id="status">&nbsp;0 selected features</span>
                        </form>
                        <button id="download-geojson">Export</button>
                        <form class="form-inline">
                            <input type="file" id="import">
                        </form>
                        <script type="text/javascript" defer>
                            console.log(ol)
                            const estates = {!! $estate !!}[0];
                            const roads = {!! $road !!}[0];
                            const estatesSource = new ol.source.Vector({
                                features: new ol.format.GeoJSON().readFeatures(estates),
                                transition: 0
                            });
                            const roadsSource = new ol.source.Vector({
                                features: new ol.format.GeoJSON().readFeatures(roads),
                                transition: 0
                            });

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
                                        width: 3,
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

                            const estatesLayer = new ol.layer.Vector({
                                source: estatesSource,
                                style: (feature) => {
                                    return styles[feature.getGeometry().getType()]
                                },
                            });
                            const roadsLayer = new ol.layer.Vector({
                                source: roadsSource,
                                style: (feature) => {
                                    return styles[feature.getGeometry().getType()]
                                },
                            });

                            const map = new ol.Map({
                                target: 'map',
                                /*controls:[
                                    new ol.Control.Navigation(),
                                    new ol.Control.PanZoomBar(),
                                    new ol.Control.LayerSwitcher(),
                                    new ol.Control.Attribution()],*/
                                projection: new ol.proj.Projection("EPSG:900913"),
                                displayProjection: new ol.proj.Projection("EPSG:4326"),
                                layers: [
                                    new ol.layer.Tile({
                                        source: new ol.source.OSM(),
                                    }),
                                    estatesLayer,
                                    roadsLayer,
                                ],
                                view: new ol.View({
                                    projection: 'EPSG:4326',
                                    center: [15.645, 46.55],
                                    zoom: 15,
                                }),
                            });

                            let select = null;
                            const selected = new ol.style.Style({
                                fill: new ol.style.Fill({
                                    color: '#ff0000',
                                }),
                                stroke: new ol.style.Stroke({
                                    color: 'rgb(255,0,0)',
                                    width: 4,
                                }),
                            });

                            function selectStyle(feature) {
                                const color = feature.get('COLOR') || '#ff0000';
                                selected.getFill().setColor(color);
                                return selected;
                            }
                            const selectClick1 = new ol.interaction.Select({
                                condition: ol.events.condition.click,
                                style: selectStyle,
                            });
                            const selectClick2 = new ol.interaction.Select({
                                condition: ol.events.condition.click,
                                style: selectStyle,
                            });
                            const selectClick3 = new ol.interaction.Select({
                                condition: ol.events.condition.click,
                                style: selectStyle,
                            });
                            const selectClick4 = new ol.interaction.Select({
                                condition: ol.events.condition.click,
                                style: selectStyle,
                            });
                            const selectClick5 = new ol.interaction.Select({
                                condition: ol.events.condition.click,
                                style: selectStyle,
                            });
                            const selectElement = document.getElementById('type');

                            let selectedTrackLayout = {"click1":"","click2":"","click3":"","click4":"","click5":""};
                            const changeInteraction = function () {
                                console.log(selectedTrackLayout)
                                if (select !== null) {
                                    map.removeInteraction(select);
                                }
                                let value = selectElement.value;
                                if (value === 'click1') {
                                    select = selectClick1;
                                } else if (value === 'click2') {
                                    select = selectClick2;
                                } else if (value === 'click3') {
                                    select = selectClick3;
                                } else if (value === 'click4') {
                                    select = selectClick4;
                                } else if (value === 'click5') {
                                    select = selectClick5;
                                } else {
                                    select = null;
                                }
                                if (select !== null) {
                                    console.log(select)
                                    map.addInteraction(select);
                                    select.on('select', function (e) {
                                        if (e.selected.length === 0) {
                                            console.log("NO FEATURES SELECTED")
                                            return
                                        }
                                        console.log(e.selected)
                                        selectedTrackLayout[value] = new ol.format.GeoJSON().writeFeatures(e.selected, {
                                            dataProjection: 'EPSG:4326', featureProjection: 'EPSG:3857'
                                        });
                                        console.log(selectedTrackLayout)
                                        document.getElementById('status').innerHTML = '&nbsp;' +
                                            e.target.getFeatures().getLength() +
                                            ' selected features (last operation selected ' + e.selected.length +
                                            ' and deselected ' + e.deselected.length + ' features)';
                                    });
                                }
                            };

                            selectElement.onchange = changeInteraction;
                            changeInteraction();

                            document.getElementById('download-geojson')
                                .addEventListener('click', function () {
                                    const a = document.createElement("a");
                                    const blob = new Blob([JSON.stringify(selectedTrackLayout)], {type: 'text/plain'});
                                    a.href = URL.createObjectURL(blob);
                                    a.download = "traclayouts.geojson";
                                    a.click();
                                });

                            const inputElement = document.getElementById('import');
                            inputElement.onchange = async function (event) {
                                var fileList = inputElement.files;
                                for (const file of fileList) {
                                    const fileParsed = JSON.parse(await file.text())
                                    for (const index in fileParsed){
                                        if (fileParsed[index] === "") {
                                            continue
                                        }
                                        const parsed = JSON.parse(fileParsed[index]).features[0]
                                        //console.log(roadsLayer.getSource())
                                        //var fakeOnSelectEvent = new ol.interaction.Select.Event(ol.interaction.Select.EventType.SELECT, [], [], false);
                                        const collection = new ol.Collection(parsed);
                                        const newSelect = new ol.interaction.Select({
                                            condition: ol.events.condition.click,
                                            features: collection,
                                            style: selectStyle,
                                        });
                                        map.addInteraction(newSelect);
                                        console.log("Dispatching")
                                        newSelect.on('select', function (e) {
                                            console.log(e.selected)
                                            if (e.selected.length === 0) {
                                                console.log("NO FEATURES SELECTED")
                                                return
                                            }
                                            selectedTrackLayout["click1"] = new ol.format.GeoJSON().writeFeatures(e.selected);
                                            console.log(selectedTrackLayout)
                                            document.getElementById('status').innerHTML = '&nbsp;' +
                                                e.target.getFeatures().getLength() +
                                                ' selected features (last operation selected ' + e.selected.length +
                                                ' and deselected ' + e.deselected.length + ' features)';
                                        });
                                        newSelect.dispatchEvent({
                                           type: 'select',
                                           selected: [collection],
                                           deselected: []
                                        });



                                        /*for(const road of roadsLayer.getSource().getFeatures()) {
                                            console.log(road.getGeometry())
                                        }*/
                                        //map.dispatchEvent ({ type: 'singleClick', coordinate: JSON.parse(fileParsed[index]), pixel: pixel })
                                    }
                                }
                            }

                            /*this.selectType = (mapBrowserEvent) => {
                                return ol.events.condition.singleClick(mapBrowserEvent) ||
                                    ol.events.condition.doubleClick(mapBrowserEvent);
                            }

                            this.selectInteraction = new ol.interaction.Select({
                                condition: this.selectType,
                                toggleCondition: ol.events.condition.shiftKeyOnly,
                                layers: this.layerFilter,
                                features: this.features,
                                style: this.selectStyle,
                            });*/
                            //(15.645, 46.55)
                        </script>
                    </div>
                </div>
                <div class="col-md-auto">
                    <h1>Vreme</h1>
                    @foreach($weather['original'] as $forecastDay)
                        <b>{{ $forecastDay['date'] }}</b><br>
                        <img src="{{ $forecastDay['icon'] }}">
                        <p>Max: {{ $forecastDay['maxTemp'] }}°C</p>
                        <p>Min: {{ $forecastDay['minTemp'] }}°C</p>
                        <p>Možnost padavin: {{ $forecastDay['chanceOfRain'] }}%</p>
                        <p>Vlažnost: {{ $forecastDay['humidity'] }}%</p>
                    @endforeach
                </div>
            </div>
        </div>
    </div>
@endsection
