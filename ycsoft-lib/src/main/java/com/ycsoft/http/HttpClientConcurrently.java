package com.ycsoft.http;

import java.util.Collection;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * 并行发送请求
 *
 * @author Killer
 */
public class HttpClientConcurrently extends AbstractHttpClient{

	@Override
	public Collection<ResponseBody> doSendRequest(
			CloseableHttpClient httpClient, Collection<HttpTask> tasks)
			throws Throwable {
		throw new UnsupportedOperationException("not support concurrent request");
	}

}
