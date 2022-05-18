package com.example.semester_app.presentation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.example.semester_app.R
import com.example.semester_app.databinding.ActivityCategoriesBinding
import com.example.semester_app.adapters.MealRecyclerAdapter
import com.example.semester_app.adapters.SetOnMealClickListener
import com.example.semester_app.data.pojo.Meal
import com.example.semester_app.viewmodel.MealActivityVM


class MealActivity : AppCompatActivity() {
    private lateinit var mealActivityMvvm: MealActivityVM
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var myAdapter: MealRecyclerAdapter
    private var categoryNme = ""

    companion object{
        const val MEAL_ID = "com.example.foodoptions_and.fragments.idMeal"
        const val MEAL_STR = "com.example.foodoptions_and.fragments.strMeal"
        const val MEAL_THUMB = "com.example.foodoptions_and.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.foodoptions_and.fragments.nameCategory"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mealActivityMvvm = ViewModelProvider(this)[MealActivityVM::class.java]
        startLoading()
        prepareRecyclerView()
        mealActivityMvvm.getMealsByCategory(getCategory())
        mealActivityMvvm.observeMeal().observe(this, object : Observer<List<Meal>> {
            override fun onChanged(t: List<Meal>?) {
                if(t==null){
                    hideLoading()
                    Toast.makeText(applicationContext, "No meals in this category", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }else {
                    myAdapter.setCategoryList(t)
                    binding.tvCategoryCount.text = categoryNme + " : " + t.size.toString()
                    hideLoading()
                }
            }
        })

        myAdapter.setOnMealClickListener(object : SetOnMealClickListener {
            override fun setOnClickListener(meal: Meal) {
                val intent = Intent(applicationContext, MealDetailsActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_STR, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        })
    }

    private fun hideLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.INVISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.white))
        }    }

    private fun startLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.VISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.load_color))
        }
    }

    private fun getCategory(): String {
        val tempIntent = intent
        val x = intent.getStringExtra(CATEGORY_NAME)!!
        categoryNme = x
        return x
    }

    private fun prepareRecyclerView() {
        myAdapter = MealRecyclerAdapter()
        binding.mealRecyclerview.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }
}