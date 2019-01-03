//package com.weatharium.v4n0v.weathariummvvm.repositories.images;
//
//import com.example.v4n0v.wethariumreact.okApi.ConnectionCore;
//import com.weatharium.v4n0v.weathariummvvm.App;
//
//import java.net.URL;
//import java.util.ArrayList;
//
//import javax.inject.Inject;
//
//import io.reactivex.Observable;
//import timber.log.Timber;
//
///**
// * класс загузчик списка ссылок на фото из Flicker, поиск по городу
// */
//
//
//public class FlickerLinkLoader implements IFlickerLoader{
//
//    private static final String OPEN_API_MAP = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
//    private static final String OPEN_API_KEY = "1aa546d01134ed09d869b84c7e83e34f";
//
//    private final String FLIKR_KEY = "9c6b4a5f6ad93dafa5a5ca0ef3b2f864";
//    private final int IMAGE_COUNT = 50;
//    private final String IMAGE_SIZE = "url_m";
//    private final String FLIKR_REQUEST_IMAGE_KEY = "weatherInfoData";
//
//
//
//    public FlickerLinkLoader(ConnectionCore core) {
//        this.core = core;
//    }
//
//    @Override
//    public Observable<String> getPhotoLinks(String weatherInfoData) {
//
//
//        return Observable.fromCallable(() -> {
//            String strNoSpaces = weatherInfoData.replace(" ", "+");
//
//            String link = "https://api.flickr.com/services/rest/?safe_search=safe&api_key=" +
//                    FLIKR_KEY + "&format=json&content_type=1&sort=relevance&method=flickr.photos.search&media=photos&extras="
//                    + IMAGE_SIZE + "&per_page=" + IMAGE_COUNT + "&text=" + strNoSpaces + "+" + FLIKR_REQUEST_IMAGE_KEY;
//
//            URL url = new URL(link);
//            Timber.d(url.toString());
//            String json = App.Companion.getInstance()..getResponse(url);
//
//            return getImagesArray(json);
//        });
//
//    }
//
//
//    private String getImagesArray(String json) {
//        ArrayList<String> links = new ArrayList<>();
//        String[] lines = json.split(",");
//        for (int i = 0; i < lines.length; i++) {
//            if (lines[i].contains("\"" + IMAGE_SIZE + "\":")) {
//                String[] tempString = lines[i].split("\"" + IMAGE_SIZE + "\":");
//                String tmp = tempString[1];
//                tmp = tmp.replace("\"", "");
//                links.add(tmp);
//            }
//        }
//        int id = (int) (Math.random() * IMAGE_COUNT);
//        Timber.d("Array links parsing complete");
//        return links.get(id).replace("\\", "");
//    }
//
//}
