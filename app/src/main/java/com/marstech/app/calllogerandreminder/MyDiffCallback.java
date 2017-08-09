package com.marstech.app.calllogerandreminder;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.DiffUtil.Callback;

import com.marstech.app.calllogerandreminder.Model.CalLog;

import java.util.List;

/**
 * Created by HP-PC on 5.08.2017.
 */

public class MyDiffCallback extends Callback {
    List<CalLog> newCallogs;
    List<CalLog> oldCallog;

    public MyDiffCallback(List<CalLog> newCallogs, List<CalLog> oldCallog) {
        this.newCallogs = newCallogs;
        this.oldCallog = oldCallog;
    }

    @Override
    public int getOldListSize() {
        return oldCallog.size();
    }

    @Override
    public int getNewListSize() {
        return newCallogs.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCallog.get(oldItemPosition).getCagriID() == newCallogs.get(newItemPosition).getCagriID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldCallog.get(oldItemPosition).equals(newCallogs.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
