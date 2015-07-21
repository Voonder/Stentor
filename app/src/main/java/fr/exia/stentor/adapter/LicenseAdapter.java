/**************************************************************************************************
 * The MIT License (MIT)                                                                          *
 *                                                                                                *
 * Copyright (c) 2015 - Julien Normand                                                            *
 *                                                                                                *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software  *
 * and associated documentation files (the "Software"),  to deal in the Software without          *
 * restriction, including without limitation  the  rights to use, copy, modify, merge, publish,   *
 * distribute, sublicense, and/or  sell copies of the Software, and to permit persons to whom the *
 * Software is furnished to do so, subject to the following conditions:                           *
 *                                                                                                *
 * The above copyright notice and this permission notice shall be included in all copies or       *
 * substantial portions of the Software.                                                          *
 *                                                                                                *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING  *
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND     *
 * NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,  *
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING       *
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.  *
 **************************************************************************************************/

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
