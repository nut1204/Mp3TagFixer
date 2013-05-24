package com.cman.mediatagconvertor;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cman.mediatagconvertor.model.ID3Tag;

public class ID3TagArrayAdapter extends ArrayAdapter<ID3Tag> {

	private final List<ID3Tag> list;
	private final Activity context;

	public ID3TagArrayAdapter(Activity context, List<ID3Tag> list) {
		super(context, R.layout.list_fruit, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView tbTitle;
		protected TextView tvArtist;
		protected CheckBox checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.list_fruit, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.tbTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvArtist = (TextView) view.findViewById(R.id.artist);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							ID3Tag element = (ID3Tag) viewHolder.checkbox
									.getTag();
							element.setSelected(buttonView.isChecked());
						}
					});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.tbTitle.setText(list.get(position).getTitle());
		holder.tvArtist.setText(list.get(position).getArtist());
		holder.checkbox.setChecked(list.get(position).isSelected());
		return view;
	}

	/*
	 * listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, final View view,
	 * int position, long id) { Toast.makeText(getApplicationContext(),
	 * "Click ListItem Number " + position, Toast.LENGTH_LONG) .show(); } });
	 */
}
