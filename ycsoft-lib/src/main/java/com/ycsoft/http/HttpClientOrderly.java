package com.ycsoft.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 保证发送的请求是有序的，顺序是根据请求的数组从0开始迭代
 *
 * @author Killer
 */
public class HttpClientOrderly extends AbstractHttpClient {

	@Override
	public Collection<ResponseBody> doSendRequest(
			CloseableHttpClient httpClient, Collection<HttpTask> tasks)throws Throwable{
		Collection<ResponseBody> bodyList = new ArrayList<>();
		for (Iterator<HttpTask> iterator = tasks.iterator(); iterator.hasNext();) {
			HttpTask aTask = iterator.next();
			bodyList.add(doRequestForSingleTask(httpClient, aTask));
		}
		return bodyList;
	}
	
	private ResponseBody doRequestForSingleTask(CloseableHttpClient httpClient,
			HttpTask task) throws Exception{
		HttpPost post = new HttpPost(task.getRequestUrl());
		post.setEntity(new StringEntity(task.getJsonData(), getContentType()));
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(post);
			HttpEntity entity = response.getEntity();
			return new ResponseBody(EntityUtils.toString(entity));
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}
}
