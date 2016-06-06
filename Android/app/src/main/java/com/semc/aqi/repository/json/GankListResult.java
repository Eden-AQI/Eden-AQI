package com.semc.aqi.repository.json;

import android.support.annotation.Keep;

import java.util.List;

@Keep
public class GankListResult<T> {
    private String error;
    private List<T> results;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
