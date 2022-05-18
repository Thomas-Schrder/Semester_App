package com.example.semester_app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.semester_app.data.pojo.Meal
import com.example.semester_app.data.pojo.MealsResponse
import com.example.semester_app.data.retrofit.RetrofitInstance
import retrofit2.Response
import retrofit2.Callback


class MealActivityVM :ViewModel() {
    private var mutableMeal = MutableLiveData<List<Meal>>()

    fun getMealsByCategory(category:String){
        RetrofitInstance.foodApi.getMealsByCategory(category).enqueue(object :
            Callback<MealsResponse> {
            override fun onResponse(call: retrofit2.Call<MealsResponse>, response: Response<MealsResponse>) {
                mutableMeal.value = response.body()!!.meals
            }

            override fun onFailure(call: retrofit2.Call<MealsResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }

        })
    }

    fun observeMeal():LiveData<List<Meal>>{
        return mutableMeal
    }
}


