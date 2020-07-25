package com.bot.man.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bot.man.databinding.ItemResponseBinding
import com.bot.man.model.data.Response
import com.bot.man.view.adapter.MainAdapter.MainViewHolder
import com.bot.man.viewmodel.ResponseItemViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {
    private var mResponse: Response? = null
    fun setResponse(response: Response?) {
        if (response != null && response.ok!!) {
            mResponse = response
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding =
                ItemResponseBinding
                        .inflate(LayoutInflater.from(parent.context), parent,
                                false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        if (mResponse == null) {
            return
        }
        val result = mResponse!!.result!![position]!!
        val viewModel = ResponseItemViewModel()
        viewModel.mFromName.value = result.message!!.from!!.username
        viewModel.mDate.value = getFormattedDate(result.message.date!!.toLong())
        viewModel.mUpdateId.value = "" + result.updateId
        viewModel.mMessage.value = result.message.text
        holder.bind(viewModel)
    }

    /**
     * Method to get the formatted timestamp to display.
     */
    private fun getFormattedDate(time: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time * 1000
        return SimpleDateFormat("MMM dd yyyy, hh:mm a", Locale.getDefault())
                .format(calendar.time)
    }

    override fun getItemCount(): Int {
        return if (mResponse == null || mResponse!!.result!!.isEmpty()) {
            0
        } else mResponse!!.result!!.size
    }

    class MainViewHolder(
            private val mBinding: ItemResponseBinding) :
            ViewHolder(mBinding.root) {
        fun bind(viewModel: ResponseItemViewModel?) {
            mBinding.viewModel = viewModel
            mBinding.executePendingBindings()
        }

    }
}