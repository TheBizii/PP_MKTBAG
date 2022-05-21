<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        DB::collection('users')->insert([
            'email' => 'admin@admin.com',
            'password' => Hash::make('admin1234'),
            'name' => 'admin',
            'surname' => 'admin',
            'is_admin' => 1,
        ]);
    }
}
