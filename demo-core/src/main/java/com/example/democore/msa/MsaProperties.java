package com.example.democore.msa;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
@Data
@ConfigurationProperties(value = MsaProperties.PREFIX)
public class MsaProperties {
	public static final String PREFIX = "msa";
	
	private HashMap<String, String> proxyUri;
	
	private Set<String> byPassHeader;
	
}
