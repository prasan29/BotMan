package com.bot.man.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bot.man.databinding.ItemResponseBinding;
import com.bot.man.model.data.Response;
import com.bot.man.model.data.ResultItem;
import com.bot.man.viewmodel.ResponseItemViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainAdapter
		extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

	private Response mResponse;

	public void setResponse(Response response) {
		if (response != null && response.component2()) {
			mResponse = response;
			notifyDataSetChanged();
		}
	}

	@NonNull
	@Override
	public MainViewHolder onCreateViewHolder(
			@NonNull ViewGroup parent, int viewType) {
		ItemResponseBinding binding = ItemResponseBinding
				.inflate(LayoutInflater.from(parent.getContext()), parent,
				         false);

		return new MainViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
		if (mResponse == null) {
			return;
		}

		ResultItem result = mResponse.getResult().get(position);

		ResponseItemViewModel viewModel = new ResponseItemViewModel();

		viewModel.getMFromName()
		         .setValue(result.getMessage().getFrom().getUsername());

		viewModel.getMDate()
		         .setValue(getFormattedDate(result.getMessage().getDate()));

		viewModel.getMUpdateId().setValue("" + result.getUpdateId());
		viewModel.getMMessage().setValue(result.getMessage().getText());

		holder.bind(viewModel);
	}

	/**
	 * Method to get the formatted timestamp to display.
	 */
	private String getFormattedDate(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time * 1000);

		return new SimpleDateFormat("MMM dd yyyy, hh:mm a", Locale.getDefault())
				.format(calendar.getTime());
	}


	@Override
	public int getItemCount() {
		if (mResponse == null || mResponse.getResult().size() <= 0) {
			return 0;
		}
		return mResponse.getResult().size();
	}

	static class MainViewHolder extends RecyclerView.ViewHolder {
		private ItemResponseBinding mBinding;

		public MainViewHolder(
				@NonNull ItemResponseBinding itemResponseBinding) {
			super(itemResponseBinding.getRoot());
			mBinding = itemResponseBinding;
		}

		public void bind(ResponseItemViewModel viewModel) {
			mBinding.setViewModel(viewModel);
			mBinding.executePendingBindings();
		}
	}
}
