<?php

namespace App\Http\Controllers;

use App\Models\Road;
use Illuminate\Http\Request;
use Illuminate\Http\Response;

class RoadController extends Controller
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
        return Road::all();
    }

    public function store(Request $request)
    {
        $road = new Road();
        $data = json_decode($request->input("geoData"), true);
        //return response()->json($data["crs"]);
        $road->type = $data["type"];
        $road->name = "roads";
        $road->crs = $data["crs"];
        $road->features = $data["features"];
        $road->save();
        return response()->json($road);
    }
}
