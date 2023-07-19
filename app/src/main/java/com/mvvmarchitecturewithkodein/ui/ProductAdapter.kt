package com.mvvmarchitecturewithkodein.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvmarchitecturewithkodein.R
import com.mvvmarchitecturewithkodein.databinding.ItemProductBinding
import com.mvvmarchitecturewithkodein.extensions.loadUrl
import com.mvvmarchitecturewithkodein.model.ProductsItem
import com.mvvmarchitecturewithkodein.utils.Utils


class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    //MARK: Variable declaration
    private var arrayList = ArrayList<ProductsItem?>()
    private lateinit var mContext: Context
    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onClickItem(productTitle: String)
    }

    //MARK: Set data
    fun setData(list: ArrayList<ProductsItem?>, listener: OnItemClickListener) {
        this.arrayList = list
        this.listener = listener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        mContext = parent.context
        val binding = ItemProductBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = arrayList[position]
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    //MARK: View holder class
    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: ProductsItem) {
            binding.item = item

            //MARK: Product image
            binding.imgProductImage.loadUrl(Utils.chkStr(item.images?.get(0)), R.drawable.img_no_data_found)

            itemView.setOnClickListener {
                listener.onClickItem(Utils.chkStr(item.title))
            }
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    /* @BindingAdapter("productImageUrl")
     fun loadImage(view: ImageView, imageUrl: String?) {
         Glide.with(view.context)
             .load(imageUrl)
             .apply(RequestOptions().error(R.drawable.ic_menu_gallery))
             .into(view)
     }*/
}
