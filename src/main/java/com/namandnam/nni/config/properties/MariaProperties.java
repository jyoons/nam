package com.namandnam.nni.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = MariaProperties.PREFIX)
public class MariaProperties implements DatabaseProperties {
	public static final String PREFIX = "spring.maria.datasource";

	public static final boolean DEFAULT_INITIALIZE = false;

	private boolean initialize = DEFAULT_INITIALIZE;

	private String driverClassName;

	private String url;

	private String username;

	private String password;

	private int initialSize;

	private int maxActive;

	private int maxIdle;

	private int minIdle;

	private int maxWait;
}
