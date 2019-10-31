package com.mobitrack.mobi.activities;

import com.mobitrack.mobi.api.model.RData;
import com.mobitrack.mobi.model.FData;
import com.mobitrack.mobi.utility.Constant;

import java.util.List;

public class HourlyAnimActivy extends BaseAnimActivity {
    @Override
    public void initData() {
        rDataList = (List<RData>) getIntent().getSerializableExtra(Constant.DATA);
        vType = getIntent().getIntExtra(Constant.V_TYPE,1);
    }
}
