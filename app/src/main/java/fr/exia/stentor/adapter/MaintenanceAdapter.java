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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.exia.stentor.R;
import fr.exia.stentor.interfaces.OnClickOperationItem;
import fr.exia.stentor.model.Operation;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.ViewHolder> {

    public OnClickOperationItem onClickOperationItem;
    private List<Operation> operations;
    private Context context;

    public MaintenanceAdapter(Context context, List<Operation> operations) {
        this.context = context;
        this.operations = operations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_maintenance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Operation operation = operations.get(position);

        holder.txtName.setText(operation.getName());
        holder.txtDescription.setText(operation.getDescription());

        Calendar calendar = Calendar.getInstance(Locale.FRENCH);
        calendar.set(Calendar.HOUR, operation.getDuration().getHour());
        calendar.set(Calendar.MINUTE, operation.getDuration().getMinute());
        calendar.set(Calendar.SECOND, operation.getDuration().getSecond());

        String time;
        if (operation.getDuration().getHour() == 0) {
            if (operation.getDuration().getMinute() == 0) {
                time = context.getString(R.string.maintenance_item_second, operation.getDuration().getSecond());
            } else {
                time = context.getString(R.string.maintenance_item_minute, operation.getDuration().getMinute(), operation.getDuration().getSecond());
            }
        } else {
            time = context.getString(R.string.maintenance_item_hour, operation.getDuration().getHour(), operation.getDuration().getMinute(), operation.getDuration().getSecond());
        }

        holder.txtTime.setText(time);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickOperationItem.onClickOperationItem(operation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return operations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtDescription;
        TextView txtTime;

        public ViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.item_maintenance_name);
            txtDescription = (TextView) itemView.findViewById(R.id.item_maintenance_description);
            txtTime = (TextView) itemView.findViewById(R.id.item_maintenance_time);
        }
    }
}
