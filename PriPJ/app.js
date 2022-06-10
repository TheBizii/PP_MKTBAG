const fs = require('fs');
const fetch = require('node-fetch');
const shp = require('shpjs')
const axios = require('axios');
var shapefile = require("shapefile");
var express = require('express'),
  bodyParser = require('body-parser'),
  app = express(),
  port = process.env.PORT || 3000;
process.env['NODE_TLS_REJECT_UNAUTHORIZED'] = '0';

app.use(function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/', (req, res) => {
  res.writeHead(200, {'Content-type': 'text/html'})
  fs.readFile('./index.html', null, function(error, data) {
      if (error) {
        res.writeHead(404);
        res.write("File not found!");
      } else {
        res.write(data);
      }
      res.end();
    });
});

async function getData() {
  var data = await fetch("https://egp.gu.gov.si/egp/download-file.html?id=110&format=10&d96=1", {
    "headers": {
      "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
      "accept-language": "en-GB,en-US;q=0.9,en;q=0.8",
      "cache-control": "no-cache",
      "pragma": "no-cache",
      "sec-fetch-dest": "document",
      "sec-fetch-mode": "navigate",
      "sec-fetch-site": "same-origin",
      "sec-fetch-user": "?1",
      "sec-gpc": "1",
      "upgrade-insecure-requests": "1",
      "cookie": "JSESSIONID=zKEuVps6HDO4DfVjIoLWAXW2Zx8lxZ--QJesEfDCW5FFUQnbks9C!-1560337058",
      "Referer": "https://egp.gu.gov.si/egp/index.html",
      "Referrer-Policy": "strict-origin-when-cross-origin"
    },
    "body": null,
    "method": "GET"
  });
  if (data) {
    var geoData = shapefile.read("files/data2s.shp", "files/data2d.dbf");
    //var geoData = {}
    console.log("started reading");
    geoData = await geoData;
    console.log("ended reading");
    geoData.features.splice(0, geoData.features.length - 1000);
    geoData.crs = {type: "name", properties: {name: "urn:ogc:def:crs:OGC:1.3:CRS84"}};
    console.log(geoData);
    console.log("started sending data")
    var form = new URLSearchParams();
    geoData = JSON.stringify(geoData);
    form.append('geoData', geoData);
    var res = await fetch("http://127.0.0.1:8000/api/road", {
      "body": form,
      "credentials": 'include',
      "method": "post"
    });
    res = await res.json();
    console.log(res);
    console.log("finished");
  }
}
getData();
app.listen(port, () => {
  console.log('Server started on: ' + port);
});
