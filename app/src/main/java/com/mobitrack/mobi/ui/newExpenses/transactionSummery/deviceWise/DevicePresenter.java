package com.mobitrack.mobi.ui.newExpenses.transactionSummery.deviceWise;



import com.mobitrack.mobi.model.Head;
import com.mobitrack.mobi.model.Tran;
import com.mobitrack.mobi.model.Vehicle;
import com.mobitrack.mobi.ui.newExpenses.transactionSummery.headwise.MyHead;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

public class DevicePresenter implements DeviceContract.Presenter {

    private DeviceContract.View mView;
    private List<Vehicle> vehicleList;

    public DevicePresenter(DeviceContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void processData(List<Tran> tranList) {
        mView.clearAdapter();
        Observable.from(tranList)
                .groupBy(new Func1<Tran, String>() {
                    @Override
                    public String call(Tran tran) {
                        return tran.getDevice_id();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GroupedObservable<String, Tran>>() {
                    @Override
                    public void call(GroupedObservable<String, Tran> stringTranGroupedObservable) {
                        final MyHead myHead = new MyHead(getVehicleName(stringTranGroupedObservable.getKey()));
                        stringTranGroupedObservable.forEach(new Action1<Tran>() {
                            @Override
                            public void call(Tran tran) {
                                myHead.setTotal(myHead.getTotal()+tran.getAmount());
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {

                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                               mView.addItem(myHead);
                            }
                        });
                    }
                });
    }

    @Override
    public void setVehicleList(List<Vehicle> vehicleList) {
      this.vehicleList = vehicleList;
    }

    @Override
    public void filterData(List<Tran> tranList, final Head head) {
        if(head.get_id().equals("")){
            processData(tranList);
        }else {
            mView.clearAdapter();

            Observable.from(tranList)
                    .filter(new Func1<Tran, Boolean>() {
                        @Override
                        public Boolean call(Tran tran) {
                            return tran.getHead().get_id().equals(head.get_id());
                        }
                    }).groupBy(new Func1<Tran, String>() {
                @Override
                public String call(Tran tran) {
                    return tran.getDevice_id();
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<GroupedObservable<String, Tran>>() {
                        @Override
                        public void call(GroupedObservable<String, Tran> stringTranGroupedObservable) {
                            final MyHead myHead = new MyHead(getVehicleName(stringTranGroupedObservable.getKey()));

                            stringTranGroupedObservable.forEach(new Action1<Tran>() {
                                @Override
                                public void call(Tran tran) {
                                    myHead.setTotal(myHead.getTotal()+tran.getAmount());
                                }
                            }, new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {

                                }
                            }, new Action0() {
                                @Override
                                public void call() {
                                    mView.addItem(myHead);
                                }
                            });
                        }
                    });
        }
    }


    private String getVehicleName(String vehicleId){
        String retStr = null;

        for (Vehicle x :vehicleList){
            if(x.getId().equals(vehicleId)){
                retStr = x.getModel();
                break;
            }
        }

        return retStr;
    }
}
