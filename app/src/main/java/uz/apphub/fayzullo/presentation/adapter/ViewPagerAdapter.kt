package uz.apphub.fayzullo.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import uz.apphub.fayzullo.R
import uz.apphub.fayzullo.databinding.ViewPagerItemBinding
import uz.apphub.fayzullo.domain.model.SplashItemModel

/**Created By Begzod Shokirov on 01/05/2024 **/

class ViewPagerAdapter(private val viewPagerList: List<SplashItemModel>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    private var listener: OnSplashButtonClickListener? = null

    inner class ViewHolder(private val binding: ViewPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            if (position == viewPagerList.size - 1) {
                binding.button.visibility = View.VISIBLE
                binding.button.isEnabled = true
            } else {
                binding.button.visibility = View.INVISIBLE
                binding.button.isEnabled = false
            }
            val item = viewPagerList[position]
            for (i in 0..<binding.gridLayout.childCount) {
                val view = binding.gridLayout[i]
                if (position == i) {
                    view.setBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.green
                        )
                    )
                } else {
                    view.background = ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.indicator_background
                    )
                }
            }
            binding.image.setImageResource(item.image)
            binding.title.text = item.title

            binding.button.setOnClickListener {
                it.isEnabled = false
                listener?.onButtonClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ViewPagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = viewPagerList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun initListener(listener: OnSplashButtonClickListener) {
        this.listener = listener
    }

    interface OnSplashButtonClickListener {
        fun onButtonClick(position: Int)
    }
}
