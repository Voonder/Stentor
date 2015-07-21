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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.List;

import fr.exia.stentor.R;
import fr.exia.stentor.interfaces.OnClickLicenseItem;
import fr.exia.stentor.model.ui.HelpFeedbackItem;

public class HelpFeedbackAdapter extends RecyclerView.Adapter<HelpFeedbackAdapter.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_HELP = 1;
    public OnClickLicenseItem onClickLicenseItem;
    private List<HelpFeedbackItem> items;
    private Context context;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SnackbarManager.show(Snackbar.with(context).text(R.string.todo_more_info));
        }
    };

    public HelpFeedbackAdapter(Context context, List<HelpFeedbackItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HELP) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help_feedback_plus, parent, false);
            return new ViewHolder(v, viewType);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help_feedback, parent, false);
            return new ViewHolder(v, viewType);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.holderId == TYPE_HELP) {
            holder.layout1.setOnClickListener(onClickListener);
            holder.layout2.setOnClickListener(onClickListener);
            holder.layout3.setOnClickListener(onClickListener);
            holder.layout4.setOnClickListener(onClickListener);
            holder.layout5.setOnClickListener(onClickListener);
            holder.btnMoreHelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SnackbarManager.show(Snackbar.with(context).text(R.string.todo_more_help));
                }
            });
        } else {
            if (position == TYPE_NORMAL) {
                holder.txtName.setText(items.get(position).getName());
                holder.imgIcon.setImageDrawable(items.get(position).getIcon());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickLicenseItem.onClickLicenseItem();
                    }
                });
            } else {
                holder.txtName.setText(items.get(position - 1).getName());
                holder.imgIcon.setImageDrawable(items.get(position - 1).getIcon());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SnackbarManager.show(Snackbar.with(context).text(R.string.todo_more_feedback));
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHelp(position)) {
            return TYPE_HELP;
        } else {
            return TYPE_NORMAL;
        }
    }

    private boolean isPositionHelp(int position) {
        return position == TYPE_HELP;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        int holderId;

        // For NORMAL Item
        TextView txtName;
        ImageView imgIcon;

        // For HELP Item
        RelativeLayout layout1;
        RelativeLayout layout2;
        RelativeLayout layout3;
        RelativeLayout layout4;
        RelativeLayout layout5;
        Button btnMoreHelp;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_HELP) {
                holderId = TYPE_HELP;
                layout1 = (RelativeLayout) itemView.findViewById(R.id.item_help_feedback_plus_layout1);
                layout2 = (RelativeLayout) itemView.findViewById(R.id.item_help_feedback_plus_layout2);
                layout3 = (RelativeLayout) itemView.findViewById(R.id.item_help_feedback_plus_layout3);
                layout4 = (RelativeLayout) itemView.findViewById(R.id.item_help_feedback_plus_layout4);
                layout5 = (RelativeLayout) itemView.findViewById(R.id.item_help_feedback_plus_layout5);
                btnMoreHelp = (Button) itemView.findViewById(R.id.item_help_feedback_plus_btn);
            } else {
                holderId = TYPE_NORMAL;
                imgIcon = (ImageView) itemView.findViewById(R.id.item_help_feedback_icon);
                txtName = (TextView) itemView.findViewById(R.id.item_help_feedback_name);
            }
        }
    }
}
