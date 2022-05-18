package com.example.semester_app.presentation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.example.semester_app.adapters.MealRecyclerAdapter
import com.example.semester_app.data.pojo.MealDetail
    import com.example.semester_app.databinding.FragmentSearchBinding
import com.example.semester_app.viewmodel.SearchVM
import com.example.semester_app.presentation.activities.MealDetailsActivity

class SearchFragment : Fragment() {
    private lateinit var myAdapter: MealRecyclerAdapter
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchMvvm: SearchVM
    private var mealId = ""
    private var mealStr = ""
    private var mealThub = ""

    companion object{
        const val MEAL_ID = "com.example.foodoptions_and.fragments.idMeal"
        const val MEAL_STR = "com.example.foodoptions_and.fragments.strMeal"
        const val MEAL_THUMB = "com.example.foodoptions_and.fragments.thumbMeal"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = MealRecyclerAdapter()
        searchMvvm = ViewModelProvider(this)[SearchVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSearchClick()
        observeSearchLiveData()
        setOnMealCardClick()
    }

    private fun setOnMealCardClick() {
        binding.searchedMealCard.setOnClickListener {
            val intent = Intent(context, MealDetailsActivity::class.java)

            intent.putExtra(MEAL_ID, mealId)
            intent.putExtra(MEAL_STR, mealStr)
            intent.putExtra(MEAL_THUMB, mealThub)

            startActivity(intent)


        }
    }

    private fun onSearchClick() {
        binding.icSearch.setOnClickListener {
            searchMvvm.searchMealDetail(binding.edSearch.text.toString(),context)

        }
    }

    private fun observeSearchLiveData() {
        searchMvvm.observeSearchLiveData()
            .observe(viewLifecycleOwner, object : Observer<MealDetail> {
                override fun onChanged(t: MealDetail?) {
                    if (t == null) {
                        Toast.makeText(context, "No such a meal", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.apply {

                            mealId = t.idMeal
                            mealStr = t.strMeal
                            mealThub = t.strMealThumb

                            Glide.with(context!!.applicationContext)
                                .load(t.strMealThumb)
                                .into(imgSearchedMeal)

                            tvSearchedMeal.text = t.strMeal
                            searchedMealCard.visibility = View.VISIBLE
                        }
                    }
                }
            })
    }


}