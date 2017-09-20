package com.chen.mvp.api;

import android.support.annotation.NonNull;

import com.chen.mvp.AndroidApplication;
import com.chen.mvp.api.bean.NewsDetailInfo;
import com.chen.mvp.api.bean.NewsInfo;
import com.chen.mvp.api.bean.PhotoInfo;
import com.chen.mvp.api.bean.PhotoSetInfo;
import com.chen.mvp.api.bean.SpecialInfo;
import com.chen.mvp.local.table.VideoInfo;
import com.chen.mvp.utils.NetUtil;
import com.chen.mvp.utils.StringUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by chen on 2017/9/11.
 * retrofi的网络类 添加了缓存处理机制
 */

public class RetrofitService {
    private static final String HEAD_LINE_NEWS = "T1348647909107";
    //缓存期限为一天
    static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    static final String AVOID_HTTP403_FORBIDDEN = "User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    private static final String NEWS_HOST = "http://c.3g.163.com/";
    private static final String WELFARE_HOST = "http://gank.io/";
    private static INewsApi sNewsService;
    private static IWelfareApi sWelfareService;
    // 递增页码
    private static final int INCREASE_PAGE = 20;

    private RetrofitService() {
        throw new AssertionError();
    }

    /**
     * 初始化网络通信服务
     */
    public static void init() {
        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(AndroidApplication.getContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(sLoggingInterceptor)
                .addInterceptor(sRewriteCacheControlInterceptor)
                .addNetworkInterceptor(sRewriteCacheControlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(NEWS_HOST)
                .build();
        sNewsService = retrofit.create(INewsApi.class);

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(WELFARE_HOST)
                .build();
        sWelfareService = retrofit.create(IWelfareApi.class);
    }
    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor sRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable(AndroidApplication.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                Logger.e("no network");
            }
            Response originalResponse = chain.proceed(request);

            if (NetUtil.isNetworkAvailable(AndroidApplication.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    /**
     * 打印返回的json数据拦截器
     */
    private static final Interceptor sLoggingInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request request = chain.request();
            Buffer requestBuffer = new Buffer();
            if (request.body() != null) {
                request.body().writeTo(requestBuffer);
            } else {
                Logger.d("LogTAG", "request.body() == null");
            }
            //打印url信息
           // Logger.w(request.url() + (request.body() != null ? "?" + _parseParams(request.body(), requestBuffer) : ""));
            final Response response = chain.proceed(request);

            return response;
        }
    };

    @NonNull
    private static String _parseParams(RequestBody body, Buffer requestBuffer) throws UnsupportedEncodingException {
        if (body.contentType() != null && !body.contentType().toString().contains("multipart")) {
            return URLDecoder.decode(requestBuffer.readUtf8(), "UTF-8");
        }
        return "null";
    }

    /*******************************获取api******************************/
    /**
     * 获取新闻列表
     * @return
     */
    public static Observable<NewsInfo> getNewsList(String newsId, int page) {
        String type;
        if (newsId.equals(HEAD_LINE_NEWS)) {
            type = "headline";
        } else {
            type = "list";
        }
        return sNewsService.getNewsList(type, newsId, page * INCREASE_PAGE)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapNews(newsId));
    }


    /**
     * 获取新闻详情
     * @param newsId 新闻ID
     * @return
     */
    public static Observable<NewsDetailInfo> getNewsDetail(final String newsId) {
        return sNewsService.getNewsDetail(newsId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Map<String, NewsDetailInfo>, Observable<NewsDetailInfo>>() {
                    @Override
                    public Observable<NewsDetailInfo> call(Map<String, NewsDetailInfo> newsDetailMap) {
                        return Observable.just(newsDetailMap.get(newsId));
                    }
                });
    }
    /**
     * 获取视频列表
     * @return
     */
    public static Observable<List<VideoInfo>> getVideoList(String videoId, int page) {
        return sNewsService.getVideoList(videoId, page * INCREASE_PAGE / 2)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(_flatMapVideo(videoId));
    }


    /**
     * 获取图集
     * @param photoId 图集ID
     * @return
     */
    public static Observable<PhotoSetInfo> getPhotoSet(String photoId) {
        return sNewsService.getPhotoSet(StringUtils.clipPhotoSetId(photoId))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取图片列表
     * @return
     */
    public static Observable<List<PhotoInfo>> getPhotoList() {
        return sNewsService.getPhotoList()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取更多图片列表
     * @return
     */
    public static Observable<List<PhotoInfo>> getPhotoMoreList(String setId) {
        return sNewsService.getPhotoMoreList(setId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 类型转换
     * @param typeStr 新闻类型
     * @return
     */
    private static Func1<Map<String, List<NewsInfo>>, Observable<NewsInfo>> _flatMapNews(final String typeStr) {
        return new Func1<Map<String, List<NewsInfo>>, Observable<NewsInfo>>() {
            @Override
            public Observable<NewsInfo> call(Map<String, List<NewsInfo>> newsListMap) {
                return Observable.from(newsListMap.get(typeStr));
            }
        };
    }


    /**
     * 类型转换
     * @param typeStr 视频类型
     * @return
     */
    private static Func1<Map<String, List<VideoInfo>>, Observable<List<VideoInfo>>> _flatMapVideo(final String typeStr) {
        return new Func1<Map<String, List<VideoInfo>>, Observable<List<VideoInfo>>>() {
            @Override
            public Observable<List<VideoInfo>> call(Map<String, List<VideoInfo>> newsListMap) {
                return Observable.just(newsListMap.get(typeStr));
            }
        };
    }


}
