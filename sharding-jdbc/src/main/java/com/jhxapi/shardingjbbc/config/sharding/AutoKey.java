package com.jhxapi.shardingjbbc.config.sharding;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

/**
 * sharding jdbc自定义id生成器
 * 需要在resources/META-INF/services/org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator
 * 创建文件并配置
 * @author Administrator
 *
 */
public class AutoKey implements ShardingKeyGenerator{

	AtomicLong id = new AtomicLong(8000);
	
	public AutoKey() {
		System.out.println(111);
	}
	
	@Override
	public String getType() {
		return "AUTO";
	}

	@Override
	public Properties getProperties() {
		return null;
	}

	@Override
	public void setProperties(Properties properties) {
		
	}

	@Override
	public Comparable<?> generateKey() {
		return null;
	}

}
