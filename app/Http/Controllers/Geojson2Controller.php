<?php

namespace App\Http\Controllers;

use App\Models\Geojson2;
use Illuminate\Http\Response;

class Geojson2Controller extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        //$this->middleware('auth');
    }

    /**
     * Display a listing of the resource.
     *
     * @return Response
     */
    public function index()
    {
        return GeoJson2::all();
    }
}
