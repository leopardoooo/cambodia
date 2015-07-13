package com.ycsoft.business.commons.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;

/**
 * 多实例的Bean作用域实现，替代Scope注解的方式
 *
 * @author hh
 */
public class PrototypeScopeMetadataResolver implements ScopeMetadataResolver {

	/**
	 * 空构造函数
	 */
	public PrototypeScopeMetadataResolver(){}


	public ScopeMetadata resolveScopeMetadata(@SuppressWarnings("unused")
	BeanDefinition bd) {
		ScopeMetadata metadata = new ScopeMetadata();
		metadata.setScopeName(BeanDefinition.SCOPE_PROTOTYPE);
		return metadata;
	}

}
