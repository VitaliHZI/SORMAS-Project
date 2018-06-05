package de.symeda.sormas.app.contact.edit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import de.symeda.sormas.app.R;
import de.symeda.sormas.app.core.adapter.databinding.DataBoundAdapter;
import de.symeda.sormas.app.core.adapter.databinding.DataBoundViewHolder;
import de.symeda.sormas.app.core.adapter.databinding.OnListItemClickListener;
import de.symeda.sormas.app.databinding.RowReadFollowupListItemLayoutBinding;
import de.symeda.sormas.app.event.read.EventReadTaskListAdapter;

import java.util.ArrayList;
import java.util.List;

import de.symeda.sormas.app.backend.visit.Visit;

/**
 * Created by Orson on 13/02/2018.
 * <p>
 * www.technologyboard.org
 * sampson.orson@gmail.com
 * sampson.orson@technologyboard.org
 */

public class ContactEditFollowupListAdapter extends DataBoundAdapter<RowReadFollowupListItemLayoutBinding> {

    private static final String TAG = EventReadTaskListAdapter.class.getSimpleName();

    private final Context context;
    private List<Visit> data = new ArrayList<>();
    private OnListItemClickListener mOnListItemClickListener;

    private LayerDrawable backgroundRowItem;
    private Drawable unreadListItemIndicator;

    public ContactEditFollowupListAdapter(Context context, int rowLayout, OnListItemClickListener onListItemClickListener) {
        this(context, rowLayout, onListItemClickListener, new ArrayList<Visit>());
    }

    public ContactEditFollowupListAdapter(Context context, int rowLayout, OnListItemClickListener onListItemClickListener, List<Visit> data) {
        super(rowLayout);
        this.context = context;
        this.mOnListItemClickListener = onListItemClickListener;

        if (data == null)
            this.data = new ArrayList<>();
        else
            this.data = new ArrayList<>(data);
    }

    @Override
    protected void bindItem(DataBoundViewHolder<RowReadFollowupListItemLayoutBinding> holder,
                            int position, List<Object> payloads) {

        Visit record = data.get(position);
        holder.setData(record);
        holder.setOnListItemClickListener(this.mOnListItemClickListener);

        //Sync Icon
        if (record.isModifiedOrChildModified()) {
            holder.binding.imgSyncIcon.setVisibility(View.VISIBLE);
            holder.binding.imgSyncIcon.setImageResource(R.drawable.ic_sync_blue_24dp);
        } else {
            holder.binding.imgSyncIcon.setVisibility(View.GONE);
        }

        updateUnreadIndicator(holder, record);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateUnreadIndicator(DataBoundViewHolder<RowReadFollowupListItemLayoutBinding> holder, Visit item) {
        backgroundRowItem = (LayerDrawable) ContextCompat.getDrawable(holder.context, R.drawable.background_list_activity_row);
        unreadListItemIndicator = backgroundRowItem.findDrawableByLayerId(R.id.unreadListItemIndicator);

        if (item != null) {
            if (item.isUnreadOrChildUnread()) {
                unreadListItemIndicator.setTint(holder.context.getResources().getColor(R.color.unreadIcon));
            } else {
                unreadListItemIndicator.setTint(holder.context.getResources().getColor(android.R.color.transparent));
            }
        }
    }

}
