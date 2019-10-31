package com.mobitrack.mobi.ui.newExpenses.transactionSummery.monthly;



import com.mobitrack.mobi.model.Tran;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MonthlyPresenter implements MonthlyContract.Presenter {

    private MonthlyContract.View mView;

    public MonthlyPresenter(MonthlyContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void updateMonthText() {
        mView.updateMonth();
    }

    @Override
    public void processData(List<Tran> tranList, final Date start, final Date finish) {
        mView.clearAdapter();

        Observable.from(tranList)
                .filter(new Func1<Tran, Boolean>() {
                    @Override
                    public Boolean call(Tran tran) {
                        return tran.getDate().compareTo(start)>=0 &&  tran.getDate().compareTo(finish)<=0;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Tran>() {
                    @Override
                    public void call(Tran tran) {
                        mView.addItem(tran);
                    }
                });
    }
}
