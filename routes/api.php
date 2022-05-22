<?php

use App\Http\Controllers\EstateController;
use App\Http\Controllers\RoadController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/


Route::resource('estate', EstateController::class);
Route::resource('road', RoadController::class);
/*Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});
Route::get('returnLastFive', [CommentsController::class, 'lastFive']);*/
