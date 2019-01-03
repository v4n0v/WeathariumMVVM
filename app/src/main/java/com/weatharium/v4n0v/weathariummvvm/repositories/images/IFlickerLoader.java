package com.weatharium.v4n0v.weathariummvvm.repositories.images;

import io.reactivex.Observable;

/**
 * Created by v4n0v on 25.04.18.
 */

interface IFlickerLoader {
    Observable<String> getPhotoLinks(String city);
}
