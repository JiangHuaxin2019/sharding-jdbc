package com.jhxapi.shardingjbbc.mapper;

import java.util.List;

import com.jhxapi.shardingjbbc.entity.UserEntity;

public interface UserMapper {
	
	/**
	 * 查询用户名称
	 * @param id 用户id
	 * @return
	 */
	UserEntity selectUserNameById(int id);
	
	/**
	 * 查询用户名称
	 * @param id 用户id
	 * @return
	 */
	List<UserEntity> selectUserNameByUser(String user);
	
	/**
	 * 插入实体
	 * @param entity
	 * @return
	 */
	Long insertUser(UserEntity entity);
	
}
