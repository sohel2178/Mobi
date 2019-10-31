package com.mobitrack.mobi.ui.newExpenses.helper.head;


import com.mobitrack.mobi.model.Head;
import com.mobitrack.mobi.model.Tran;

public interface HeadListener {

    void onHeadInserted(Head head);
    void transactionAdded(Tran transaction);
    void transactionUpdated(Tran transaction);
}
