<?php

use App\Http\Controllers\MapController;
use App\Http\Controllers\PagesController;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/
//Route::resource('ads', AdsController::class);
Route::get('/', [ MapController::class, "index" ]);
Route::get('/login', [ PagesController::class, "login" ]);
Route::get('/register', [ PagesController::class, "register" ]);

Auth::routes();

//Route::get('/home', [HomeController::class, 'index'])->name('home');
//Route::resource('users', UsersController::class);
