package com.ycsoft.boss.remoting.ott;

public class URLBuilder {
	private String ip="127.0.0.1";
	private int port=80;
	
	public enum Method{
		CREATE_USER,
		EDIT_USER,
		DELETE_USER,
		OPEN_USER_PRODCT,
		STOP_USER_PRODCT,
		ADD_UPDATE_PRODUCT,
		DELETE_PRODUCT
	}
	
	public String getUrl(Method method){
		String url = "http://"+ip;
		if (port !=80)
			url = url +":"+port+"/";
		else 
			url = url +"/";
		switch (method) {
		case CREATE_USER: 
			url+="nn_cms/api/jpz/k6/maintain_user.php";
			break;
		case EDIT_USER:
			url+="nn_cms/api/jpz/k6/maintain_user.php";
			break;
		case DELETE_USER:
			url+="nn_cms/api/jpz/k6/delete_user.php";
			break;
		case OPEN_USER_PRODCT:
			url+="nn_cms/api/jpz/k6/maintain_product_authorize.php";
			break;
		case STOP_USER_PRODCT:
			url+="nn_cms/api/jpz/k6/delete_product_authorize.php";
			break;
		case ADD_UPDATE_PRODUCT:
			url+="nn_cms/api/jpz/k6/maintain_product.php";
			break;
		case DELETE_PRODUCT:
			url+="nn_cms/api/jpz/k6/delete_product.php";
			break;
		default:
			url = null;
			break;
		}
		
		return url;
		
	}


	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setPort(int port) {
		this.port = port;
	}
	

}
