<?php

namespace App\Http\Controllers;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Http;

class WeatherController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return JsonResponse
     */
    public function index()
    {
        $response = Http::get("http://api.weatherapi.com/v1/forecast.json", [
            'key' => env('WEATHERAPI_KEY'),
            'q' => 'Maribor',
            'days' => 7,
            'api' => 'yes',
            'alerts' => 'no'
        ]);

        $forecastDays = [];
        foreach ($response['forecast']['forecastday'] as $forecastDay) {
            $day['date'] = $forecastDay['date'];
            $day['maxTemp'] = $forecastDay['day']['maxtemp_c'];
            $day['minTemp'] = $forecastDay['day']['mintemp_c'];
            $day['chanceOfRain'] = $forecastDay['day']['daily_chance_of_rain'];
            $day['humidity'] = $forecastDay['day']['avghumidity'];
            $day['icon'] = $forecastDay['day']['condition']['icon'];
            array_push($forecastDays, $day);
        }

        return response()->json($forecastDays);

    }
}
