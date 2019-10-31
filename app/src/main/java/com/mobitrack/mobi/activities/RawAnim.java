package com.mobitrack.mobi.activities;

import com.mobitrack.mobi.singleton.RawFData;
import com.mobitrack.mobi.utility.Constant;

public class RawAnim extends BaseAnimActivity {
    @Override
    public void initData() {
        rDataList = RawFData.getInstance().getData();
        vType = getIntent().getIntExtra(Constant.V_TYPE,1);
    }
}
