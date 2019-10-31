package com.mobitrack.mobi.api.model;

public class FenceR {
    private boolean success;
    private Fence fence;

    public FenceR() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Fence getFence() {
        return fence;
    }

    public void setFence(Fence fence) {
        this.fence = fence;
    }
}
