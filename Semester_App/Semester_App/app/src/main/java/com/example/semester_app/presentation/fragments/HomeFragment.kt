package com.example.semester_app.presentation.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide

import com.example.semester_app.R
import com.example.semester_app.adapters.CategoriesRecyclerAdapter
import com.example.semester_app.data.pojo.*
import com.example.semester_app.databinding.FragmentHomeBinding
import com.example.semester_app.viewmodel.DetailsVM
import com.example.semester_app.viewmodel.MainFragMVVM
import com.example.semester_app.presentation.activities.MealActivity
import com.example.semester_app.presentation.MealBottomDialog
import com.example.semester_app.presentation.activities.MealDetailsActivity
import com.example.semester_app.data.pojo.*


class HomeFragment : Fragment() {
    private lateinit var meal: RandomMealResponse
    private lateinit var detailMvvm: DetailsVM
    private var randomMealId = ""

    companion object{
        const val MEAL_ID = "com.example.foodoptions_and.fragments.idMeal"
        const val MEAL_AREA = "com.example.foodoptions_and.fragments.areaMeal"
        const val MEAL_STR = "com.example.foodoptions_and.fragments.strMeal"
        const val MEAL_THUMB = "com.example.foodoptions_and.fragments.thumbMeal"
        const val MEAL_NAME = "com.example.foodoptions_and.fragments.nameMeal"
        const val CATEGORY_NAME = "com.example.foodoptions_and.fragments.nameCategory"

    }

    private lateinit var myAdapter: CategoriesRecyclerAdapter
    lateinit var binding: FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailMvvm = ViewModelProvider(this)[DetailsVM::class.java]
        binding = FragmentHomeBinding.inflate(layoutInflater)
        myAdapter = CategoriesRecyclerAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainFragMVVM = ViewModelProvider(this)[MainFragMVVM::class.java]
        showLoadingCase()


        prepareCategoryRecyclerView()
        onRandomMealClick()
        onRandomLongClick()


        mainFragMVVM.observeMealByCategory().observe(viewLifecycleOwner, object : Observer<MealsResponse> {
            override fun onChanged(t: MealsResponse?) {
                val meals = t!!.meals
                setMealsByCategoryAdapter(meals)
                cancelLoadingCase()
            }


        })

        mainFragMVVM.observeCategories().observe(viewLifecycleOwner, object : Observer<CategoryResponse> {
            override fun onChanged(t: CategoryResponse?) {
                val categories = t!!.categories
                setCategoryAdapter(categories)

            }
        })

        mainFragMVVM.observeRandomMeal().observe(viewLifecycleOwner, object : Observer<RandomMealResponse> {
            override fun onChanged(t: RandomMealResponse?) {
                val mealImage = view.findViewById<ImageView>(R.id.img_random_meal)
                val imageUrl = t!!.meals[0].strMealThumb
                randomMealId = t.meals[0].idMeal
                Glide.with(this@HomeFragment)
                    .load(imageUrl)
                    .into(mealImage)
                meal = t
            }

        })

        myAdapter.onItemClicked(object : CategoriesRecyclerAdapter.OnItemCategoryClicked {
            override fun onClickListener(category: Category) {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(CATEGORY_NAME, category.strCategory)
                startActivity(intent)
            }

        })


        detailMvvm.observeMealBottomSheet()
            .observe(viewLifecycleOwner, object : Observer<List<MealDetail>> {
                override fun onChanged(t: List<MealDetail>?) {
                    val bottomSheetFragment = MealBottomDialog()
                    val b = Bundle()
                    b.putString(CATEGORY_NAME, t!![0].strCategory)
                    b.putString(MEAL_AREA, t[0].strArea)
                    b.putString(MEAL_NAME, t[0].strMeal)
                    b.putString(MEAL_THUMB, t[0].strMealThumb)
                    b.putString(MEAL_ID, t[0].idMeal)

                    bottomSheetFragment.arguments = b

                    bottomSheetFragment.show(childFragmentManager, "BottomSheetDialog")
                }

            })


        // on search icon click
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun setMealsByCategoryAdapter(meals: List<Meal>) {

    }


    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            val temp = meal.meals[0]
            val intent = Intent(activity, MealDetailsActivity::class.java)
            intent.putExtra(MEAL_ID, temp.idMeal)
            intent.putExtra(MEAL_STR, temp.strMeal)
            intent.putExtra(MEAL_THUMB, temp.strMealThumb)
            startActivity(intent)
        }

    }

    private fun onRandomLongClick() {

        binding.randomMeal.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                detailMvvm.getMealByIdBottomSheet(randomMealId)
                return true
            }

        })
    }

    private fun showLoadingCase() {
        binding.apply {
            header.visibility = View.INVISIBLE
            tvRandomMealToday.visibility = View.INVISIBLE
            randomMeal.visibility = View.INVISIBLE
            tvCategory.visibility = View.INVISIBLE
            categoryCard.visibility = View.INVISIBLE
            loadingGif.visibility = View.VISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.navi))

        }
    }

    private fun cancelLoadingCase() {
        binding.apply {
            header.visibility = View.VISIBLE
            tvRandomMealToday.visibility = View.VISIBLE
            randomMeal.visibility = View.VISIBLE
            tvCategory.visibility = View.VISIBLE
            categoryCard.visibility = View.VISIBLE
            loadingGif.visibility = View.INVISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        }
    }


    private fun setCategoryAdapter(categories: List<Category>) {
        myAdapter.setCategoryList(categories)
    }

    private fun prepareCategoryRecyclerView() {
        binding.recyclerView.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
    }

        }
