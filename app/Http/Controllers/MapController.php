<?php

namespace App\Http\Controllers;

use App\Models\Comment;
use App\Models\Estate;
use App\Models\Road;
use App\Models\User;
use Illuminate\Contracts\Foundation\Application;
use Illuminate\Contracts\View\Factory;
use Illuminate\Contracts\View\View;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\Routing\Redirector;
use Illuminate\Support\Facades\Hash;

class MapController extends Controller
{

    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        /*if (!auth()->user()->is_admin) {
            redirect()
        }*/
    }

    /**
     * Display a listing of the resource.
     *
     * @return Application|Factory|View
     */
    public function index()
    {
        //$locations = DB::table('locations')->get();
        //return view('gmaps',compact('locations'));
        $estate = Estate::All();
        $road = Road::All();
        return view('map.index')->with('estate', $estate)->with('road', $road);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return Application|Factory|View
     */
    public function create()
    {
        die("create");
        /*if(!auth()->user()->is_admin) {
            return redirect('/ads')->with('error', 'Unauthorized page');
        }*/
        return view('users.create');
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param Request $request
     * @return Application|RedirectResponse|Redirector
     */
    public function store(Request $request)
    {
        die("store");
        $this->validate($request, [
            'email' => ['required', 'string', 'email', 'max:255'],
            'password' => ['required', 'string', 'min:8', 'confirmed'],
            'name' => ['required', 'string', 'max:255' ],
            'surname' => ['required', 'string', 'max:255' ],
            'phone' => ['nullable','regex:/^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/','min:10'],
            'gender' => ['nullable','string', 'in:male,female,undefined/non-binary'],
            'address' => ['nullable','string', 'max:255'],
            'bday' => ['nullable','date'],
        ]);
        $user = new User();
        $user->password = Hash::make($request->input('password'));
        $this->extracted($request, $user);
        return redirect('/users')->with('success', 'User created');
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return Application|Factory|Redirector|RedirectResponse|View
     */
    public function show($id)
    {
        die("show");
        $user = User::find($id);
        if ($user == null){
            return redirect('/users')->with('error', 'Unauthorized page');
        }
        return view('users.show')->with('user', $user);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return Application|Factory|View
     */
    public function edit($id)
    {
        die("edit");
        $user = User::find($id);
        if ($user == null){
            return redirect('/users')->with('error', 'Unauthorized page');
        }
        return view('users.edit')->with('user', $user);
    }

    /**
     * Update the specified resource in storage.
     *
     * @param Request $request
     * @param  int  $id
     * @return Application|Redirector|RedirectResponse
     */
    public function update(Request $request, $id)
    {
        die("update");
        $this->validate($request, [
            'email' => ['required', 'string', 'email', 'max:255'],
            'name' => ['required', 'string', 'max:255' ],
            'surname' => ['required', 'string', 'max:255' ],
            'phone' => ['nullable','regex:/^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/','min:10'],
            'gender' => ['nullable','string', 'in:male,female,undefined/non-binary'],
            'address' => ['nullable','string', 'max:255'],
            'bday' => ['nullable','date'],
        ]);
        $user = User::find($id);
        $this->extracted($request, $user);
        return redirect('/users')->with('success', 'User updated');
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return Application|Redirector|RedirectResponse
     */
    public function destroy($id)
    {
        die("destroy");
        $user = User::find($id);
        if (auth()->user()->id !== $user->user_id){
            return redirect('/ads')->with('error', 'Unauthorized page');
        }
        Ad::where('user_id', $id)->get()->each->delete();
        $user->delete();
        return redirect('/users')->with('success', 'User deleted');
    }

    /**
     * @param Request $request
     * @param $user
     * @return void
     */
    public function extracted(Request $request, $user): void
    {
        $user->email = $request->input('email');
        $user->name = $request->input('name');
        $user->surname = $request->input('surname');
        $user->phone = $request->input('phone');
        $user->gender = $request->input('gender');
        $user->address = $request->input('address');
        $user->bday = $request->input('bday');
        $user->save();
    }

}
