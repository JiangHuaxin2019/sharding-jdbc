package com.jhxapi.jvm;

import java.util.ServiceLoader;

import org.apache.shardingsphere.spi.keygen.ShardingKeyGenerator;

public class MainTest {
	
	public static void main(String[] args) {
		
		for (ShardingKeyGenerator each : ServiceLoader.load(ShardingKeyGenerator.class)) {
			System.out.println(each.getClass().getName());
		}
		
	}
	
}
