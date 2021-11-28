package com.madcrew.testcardapp.view.main

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.madcrew.testcardapp.R
import com.madcrew.testcardapp.adapters.PageRecyclerAdapter
import com.madcrew.testcardapp.databinding.ActivityMainBinding
import com.madcrew.testcardapp.domain.BaseUrl.Companion.BASE_IMAGE_URL
import com.madcrew.testcardapp.domain.Repository
import com.madcrew.testcardapp.models.ImagePreviewModel
import com.madcrew.testcardapp.tools.isOnline
import com.madcrew.testcardapp.tools.noInternet
import com.madcrew.testcardapp.tools.setGone
import com.madcrew.testcardapp.tools.setVisible
import kotlin.math.roundToInt


class MainActivity(var pagging: Int = 0) : AppCompatActivity(), PageRecyclerAdapter.OnImageClickListener {

    private lateinit var binding: ActivityMainBinding

    lateinit var mViewModel: MainActivityViewModel
    lateinit var mAdapter: PageRecyclerAdapter
    private var mImageList = mutableListOf<ImagePreviewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val swipeContainer = binding.swipeLayout

        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)

        val dest = resources.displayMetrics.density
        val width = (((resources.displayMetrics.widthPixels * dest).roundToInt() / 2 ).toDouble() / dest).roundToInt() - 4
        val height = (resources.displayMetrics.heightPixels * dest).roundToInt()

        Log.i("destiny", "$height , $width")

        binding.textLoading.setVisible()
        binding.linearProgressIndicator.hide()

        val repository = Repository()
        val viewModelFactory = MainActivityViewModelFactory(repository)
        mViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainActivityViewModel::class.java]

        getPage(pagging)

        mAdapter = PageRecyclerAdapter(mImageList, this)
        binding.mainRecycler.apply {
            isNestedScrollingEnabled = true
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            adapter = mAdapter
        }

        mViewModel.categoryPage.observe(this, {response ->
            if (response.isSuccessful){
                val startCount = mImageList.size
                for (i in response.body()!!.data?.post_card!!){
//                    mImageList.add(ImagePreviewModel(BASE_IMAGE_URL + i.image?.preview.toString(), i.image?.dimentions?.height!!, i.image?.dimentions?.width!!, i.name.toString()))
                    val imgH = i.image?.dimentions?.height!!
                    val imgW = i.image?.dimentions?.width!!
                    mImageList.add(ImagePreviewModel(BASE_IMAGE_URL + i.image?.preview.toString(), ((imgW.toDouble() / width.toDouble()) * imgH).roundToInt(), width , i.name.toString()))
                    Log.i("imgdimens", "${(imgW.toDouble() / width.toDouble()) * imgH} image $width")
                }
                if (mImageList.size != response.body()!!.data?.post_card?.size!!){
                    mAdapter.notifyItemRangeInserted(startCount + 1, response.body()!!.data?.post_card?.size!!)
                } else {
                    mAdapter.notifyDataSetChanged()
                }

                swipeContainer.isRefreshing = false
                swipeContainer.isEnabled = true
                binding.linearProgressIndicator.hide()
                binding.textLoading.setGone()
            }
        })

        swipeContainer.setOnRefreshListener {
            mImageList.clear()
            pagging = 0
            getPage(pagging)
            swipeContainer.isEnabled = false
        }


        binding.mainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if (mViewModel.categoryPage.value!!.body()?.data?.post_card?.size != 0){
                        pagging++
                        getPage(pagging)
                    }
                }
            }
        })

    }

    private fun getPage(page: Int){
        if (mImageList.size == 0){
            binding.linearProgressIndicator.hide()
        } else {
            binding.linearProgressIndicator.show()
        }
        if (isOnline(this)){
            mViewModel.getCategoryPage("2", page.toString())
        } else {
            noInternet(this)
        }
    }

    override fun onImageClick(itemView: View?, position: Int) {
        Toast.makeText(this, mImageList[position].name, Toast.LENGTH_SHORT).show()
    }

}