package com.example.android.network;

import android.os.AsyncTask;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * 
 * <p>异步处理Http请求类，源码参考sinaweibo，因sina的SSLSocketFactory有单独的证书，
 *    qq的请求就单独实现
 * </p>
 *
 * @author harding.he@fmjk.com
 * @version $Id: AsyncWeiboRunner.java, v 0.1 2014-5-7 下午4:22:16 harding.he Exp $
 */
public class AsyncWeiboRunner {
    @Deprecated
    public static void requestByThread(final String url, final WeiboParameters params, final String httpMethod,
                                       final RequestListener listener) {
        new Thread() {
            public void run() {
                try {
                    String resp = HttpManager.openUrl(url, httpMethod, params);
                    if (listener != null) {
                        listener.onComplete(resp);
                    }
                } catch (WeiboException e) {
                    if (listener != null) {
                        listener.onWeiboException(e);
                    }
                }
            }
        }.start();
    }

    public static String request(String url, WeiboParameters params, String httpMethod) throws WeiboException {
        return HttpManager.openUrl(url, httpMethod, params);
    }

    public static void requestAsync(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        new RequestRunner(url, params, httpMethod, listener).execute(new Void[1]);
    }

    private static class RequestRunner extends AsyncTask<Void, Void, AsyncWeiboRunner.AsyncTaskResult<String>> {
        private final String          mUrl;

        private final WeiboParameters mParams;

        private final String          mHttpMethod;

        private final RequestListener mListener;

        public RequestRunner(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
            this.mUrl = url;
            this.mParams = params;
            this.mHttpMethod = httpMethod;
            this.mListener = listener;
        }

        protected AsyncWeiboRunner.AsyncTaskResult<String> doInBackground(Void... params) {
            try {
                String result = HttpManager.openUrl(this.mUrl, this.mHttpMethod, this.mParams);
                return new AsyncWeiboRunner.AsyncTaskResult(result);
            } catch (WeiboException e) {
                return new AsyncWeiboRunner.AsyncTaskResult(e);
            }
        }

        protected void onPreExecute() {
        }

        protected void onPostExecute(AsyncWeiboRunner.AsyncTaskResult<String> result) {
            WeiboException exception = result.getError();
            if (exception != null) {
                this.mListener.onWeiboException(exception);
            } else {
                this.mListener.onComplete((String) result.getResult());
            }
        }
    }

    private static class AsyncTaskResult<T> {
        private T              result;
        private WeiboException error;

        public T getResult() {
            return this.result;
        }

        public WeiboException getError() {
            return this.error;
        }

        public AsyncTaskResult(T result) {
            this.result = result;
        }

        public AsyncTaskResult(WeiboException error) {
            this.error = error;
        }
    }
}