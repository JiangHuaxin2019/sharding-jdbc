package com.jhxapi.shardingjbbc.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ComplexShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

@Configuration
public class DataConfig {

	@Bean("one")
	@ConfigurationProperties("spring.datasource.druid.one")
	public DataSource dataSourceOne() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean("one-slave")
	@ConfigurationProperties("spring.datasource.druid.one-slave")
	public DataSource dataSourceOneSlave() {
		return DruidDataSourceBuilder.create().build();
	}

	@Bean("two")
	@ConfigurationProperties("spring.datasource.druid.two")
	public DataSource dataSourceTwo() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean("two-slave")
	@ConfigurationProperties("spring.datasource.druid.two-slave")
	public DataSource dataSourceTwoSlave() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean("leaf")
	@ConfigurationProperties("spring.datasource.druid.leaf")
	public DataSource leafDataSource() {
		return DruidDataSourceBuilder.create().build();
	}
	
	@Bean("dataSourceMap")
	public Map<String, DataSource> getDataSourceMap(@Qualifier("one") DataSource one,@Qualifier("one-slave") DataSource oneSlave,
			@Qualifier("two") DataSource two,@Qualifier("two-slave") DataSource twoSlave) {
		Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
		dataSourceMap.put("ds_master_0", one);
		dataSourceMap.put("ds_slave_0", oneSlave);
		dataSourceMap.put("ds_master_1", two);
		dataSourceMap.put("ds_slave_1", twoSlave);
		return dataSourceMap;
	}
	
	@Primary
	@Bean
	public DataSource shardingDataSource(@Qualifier("dataSourceMap")Map<String, DataSource> dataSourceMap) throws SQLException{
		Properties properties = new Properties();
		//打印sharding-jdbc sql
		properties.put("sql.show", "true");
		return ShardingDataSourceFactory.createDataSource(dataSourceMap, getShardingRuleConfiguration(), properties);
	}
	
	/**
	 * 获取sharding-jdbc配置
	 * @return
	 */
	public ShardingRuleConfiguration getShardingRuleConfiguration() {
		// 数据分片配置
		ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
		// 读写分离配置
		shardingRuleConfig.setMasterSlaveRuleConfigs(getMasterSlaveRuleConfiguration());
		// 分库分表配置
		shardingRuleConfig.getTableRuleConfigs().add(getTableRuleConfiguration());
		// 设置分库策略
		shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(getDefaultDatabaseComplexShardingStrategyConfiguration());
		// 设置分表策略
		shardingRuleConfig.setDefaultTableShardingStrategyConfig(getTableComplexShardingStrategyConfiguration());
		// 默认Id生成器
		shardingRuleConfig.setDefaultKeyGeneratorConfig(getKeyGeneratorConfig());
		// 设置默认数据源
		shardingRuleConfig.setDefaultDataSourceName("ds0");
		return shardingRuleConfig;
	}
	
	public KeyGeneratorConfiguration getKeyGeneratorConfig() {
		KeyGeneratorConfiguration keyGeneratorConfiguration = new KeyGeneratorConfiguration("AUTO", "id");
		return keyGeneratorConfiguration;
	}

	/**
	 * 获取读写分离配置
	 * @return
	 */
	public Collection<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfiguration() {
		Collection<MasterSlaveRuleConfiguration> masterSlaveRuleConfigs = new ArrayList<MasterSlaveRuleConfiguration>();
		masterSlaveRuleConfigs.add(new MasterSlaveRuleConfiguration( "ds0", "ds_master_0", Arrays.asList("ds_slave_0") ));
		masterSlaveRuleConfigs.add(new MasterSlaveRuleConfiguration( "ds1", "ds_master_1", Arrays.asList("ds_slave_1") ));
		return masterSlaveRuleConfigs;
	}

	/**
	 * 获取分表策略
	 * @return
	 */
	public ShardingStrategyConfiguration getTableComplexShardingStrategyConfiguration() {
		ComplexKeysShardingAlgorithm<Long> tableAlgorithm = new ComplexKeysShardingAlgorithm<Long>() {

			@Override
			public Collection<String> doSharding(Collection<String> availableTargetNames,
					ComplexKeysShardingValue<Long> shardingValue) {
				
				Collection<String> collection = new ArrayList<String>();
				collection.add("t_user");
				
				return collection;
			}

		};
		ShardingStrategyConfiguration defaultTableShardingStrategyConfig = new ComplexShardingStrategyConfiguration("id",tableAlgorithm);
		return defaultTableShardingStrategyConfig;
	}

	/**
	 * 获取分库策略
	 * @return
	 */
	public ComplexShardingStrategyConfiguration getDefaultDatabaseComplexShardingStrategyConfiguration() {
		ComplexKeysShardingAlgorithm<Long> algorithm = new ComplexKeysShardingAlgorithm<Long>() {

			@Override
			public Collection<String> doSharding(Collection<String> availableTargetNames,
					ComplexKeysShardingValue<Long> shardingValue) {
				
				Collection<String> collection = new ArrayList<String>();
				collection.add("ds0");
//				collection.add("ds1");
				
				return collection;
			}

		};
		ComplexShardingStrategyConfiguration defaultDatabaseShardingStrategyConfig = new ComplexShardingStrategyConfiguration("id,user",algorithm);
		return defaultDatabaseShardingStrategyConfig;
	}

	/**
	 * 创建分表策略
	 * @param shardingRuleConfig
	 */
	public TableRuleConfiguration getTableRuleConfiguration() {
		TableRuleConfiguration userTableRuleConfig = new TableRuleConfiguration("t_user","ds${0..1}.t_user");
		//分库策略
//		userTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "ds${id % 2}"));
		//分表策略
//	    userTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "t_user"));
	    return userTableRuleConfig;
	}

}
