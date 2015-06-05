package fr.exia.stentor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fr.exia.stentor.R;
import fr.exia.stentor.model.ui.LicenseItem;

public class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder> {

	private List<LicenseItem> licenseItemList;
	private Context context;

	public LicenseAdapter(Context context, List<LicenseItem> licenseItemList) {
		this.context = context;
		this.licenseItemList = licenseItemList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_license, parent, false);
		return new ViewHolder(rootView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.txtName.setText(licenseItemList.get(position).getName());

		for (String s : licenseItemList.get(position).getLibraryList()) {
			holder.layout.addView(createLibrary(s));
		}
	}

	@Override
	public int getItemCount() {
		return licenseItemList.size();
	}

	private TextView createLibrary(String library) {
		TextView item = new TextView(context);

		item.setTextColor(context.getResources().getColor(R.color.material_drawer_accent));
		item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		item.setText(library);
		item.setSingleLine(true);
		item.setLinksClickable(true);
		item.setTextColor(Color.parseColor("#8A000000"));

		return item;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView txtName;
		LinearLayout layout;

		public ViewHolder(View itemView) {
			super(itemView);
			layout = (LinearLayout) itemView.findViewById(R.id.item_licence_layout);
			txtName = (TextView) itemView.findViewById(R.id.item_license_name);
		}
	}
}
