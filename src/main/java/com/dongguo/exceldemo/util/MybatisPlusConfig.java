package com.dongguo.exceldemo.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MybatisPlusConfig {

	@Bean
	public EasySqlInjector easySqlInjector () {
		return new EasySqlInjector();
	}

}
