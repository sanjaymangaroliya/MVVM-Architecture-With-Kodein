package com.mvvmarchitecturewithkodein.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.mvvmarchitecturewithkodein.R
import com.mvvmarchitecturewithkodein.databinding.ActivityProductBinding
import com.mvvmarchitecturewithkodein.extensions.TAG
import com.mvvmarchitecturewithkodein.extensions.gone
import com.mvvmarchitecturewithkodein.extensions.showToast
import com.mvvmarchitecturewithkodein.extensions.visible
import com.mvvmarchitecturewithkodein.model.ProductsItem
import com.mvvmarchitecturewithkodein.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ProductActivity : AppCompatActivity(), KodeinAware, ProductAdapter.OnItemClickListener {

    private lateinit var binding: ActivityProductBinding
    private var productAdapter: ProductAdapter? = null
    override val kodein by kodein()
    private val productFactory: ProductFactory by instance()
    private val viewModel: ProductViewModel by viewModels { productFactory }
    private var arrayList: ArrayList<ProductsItem?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        setupObservers()
        apiCall()

    }

    //MARK: Init UI
    private fun initUI() {
        binding.productViewModel = viewModel
        binding.lifecycleOwner = this

        //MARK: Pull down to refresh data
        binding.swipeContainer.setOnRefreshListener { apiCall() }
        binding.swipeContainer.setColorSchemeResources(R.color.colorPrimary)

        //MARK: ProductAdapter
        productAdapter = ProductAdapter()
        binding.recyclerView.adapter = productAdapter
    }

    //MARK: Api call
    private fun apiCall() {
        viewModel.getProductList(this)
    }

    //MARK: Setup observer
    private fun setupObservers() {
        viewModel.validationMessage.observe(this) {
            if (it != null) {
                showToast(it)
            }
        }

        viewModel.progress.observe(this) {
            if (it) {
                binding.progressBar.visible()
            } else {
                binding.progressBar.gone()
                binding.swipeContainer.isRefreshing = false
            }
        }

        viewModel.apiFail.observe(this) {
            if (it != null) {
                showToast(it)
                setData()
            }
        }

        viewModel.productListResponse.observe(this) {
            arrayList.clear()
            it.products?.let { it1 -> arrayList.addAll(it1) }
            setData()
        }
    }

    private fun setData() {
        //MARK: Product list show
        if (arrayList.isNotEmpty()) {
            binding.recyclerView.visible()
            binding.imgNoDataFound.gone()
            productAdapter?.setData(arrayList, this)
        } else {
            binding.recyclerView.gone()
            binding.imgNoDataFound.visible()
        }
    }

    override fun onClickItem(productTitle: String) {
        showToast(productTitle)
    }
}