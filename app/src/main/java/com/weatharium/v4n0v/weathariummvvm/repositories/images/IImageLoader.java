package com.weatharium.v4n0v.weathariummvvm.repositories.images;

public interface IImageLoader<T> {
    void loadInto(String city, T container);
    void downloadPhoto(String city, T container);
}
