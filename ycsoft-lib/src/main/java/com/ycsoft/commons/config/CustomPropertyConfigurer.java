package com.ycsoft.commons.config;

import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import com.ycsoft.commons.helper.DES;
import com.ycsoft.commons.helper.LoggerHelper;
import com.ycsoft.commons.helper.StringHelper;

public class CustomPropertyConfigurer extends PropertyPlaceholderConfigurer {

	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess,
			Properties props) throws BeansException {
		String password = props.getProperty("jdbc.password");
		if(StringHelper.isNotEmpty(password)){
			DES des = new DES();
			try {
				props.setProperty("jdbc.password", des.getDesString(password));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.processProperties(beanFactoryToProcess, props);
	}

}
