package com.mobitrack.mobi.api.headModel;


import com.mobitrack.mobi.model.Head;

import java.util.List;

public class HeadGetResponse {
    private int count;
    private List<Head> heads;

    public HeadGetResponse() {
    }

    public int getCount() {
        return count;
    }

    public List<Head> getHeads() {
        return heads;
    }
}
