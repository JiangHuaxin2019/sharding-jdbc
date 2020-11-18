package com.jhxapi.shardingjbbc.mapper;

import com.jhxapi.shardingjbbc.entity.TokenEntity;

public interface TokenMapper {
	
	/**
	 * 查询用户名称
	 * @param id 用户id
	 * @return
	 */
	TokenEntity selectTokenById(int id);
	
	/**
	 * 插入实体
	 * @param entity
	 * @return
	 */
	Integer insertToken(TokenEntity entity);
	
}
