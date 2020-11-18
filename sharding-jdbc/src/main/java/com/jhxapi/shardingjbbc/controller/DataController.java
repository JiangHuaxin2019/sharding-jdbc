package com.jhxapi.shardingjbbc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jhxapi.shardingjbbc.config.sharding.LeafKeyGenerator;
import com.jhxapi.shardingjbbc.entity.TokenEntity;
import com.jhxapi.shardingjbbc.entity.UserEntity;
import com.jhxapi.shardingjbbc.mapper.TokenMapper;
import com.jhxapi.shardingjbbc.mapper.UserMapper;

@RestController
public class DataController {

	@Autowired
	UserMapper user;

	@Autowired
	TokenMapper tonken;
	
	@Autowired
	LeafKeyGenerator leafKeyGenerator;

	@GetMapping("/user/selectOne")
	public Object user(@RequestParam Integer id) {
		return user.selectUserNameById(id);
	}

	@GetMapping("/user/select")
	public Object user(@RequestParam String user) {
		return this.user.selectUserNameByUser(user);
	}

	@GetMapping("/user/insert")
	public Object userInsert(@RequestParam String user) {
		UserEntity entity = new UserEntity();
		entity.setUser(user);
		Long insertUser = this.user.insertUser(entity);
		return insertUser;
	}

	@GetMapping("/token/selectOne")
	public Object token(@RequestParam Integer id) {
		return tonken.selectTokenById(id);
	}

	@GetMapping("/token/insert")
	public Object tokenInsert(@RequestParam String token) {
		TokenEntity entity = new TokenEntity();
		entity.setToken(token);
		return tonken.insertToken(entity);
	}
	
	@GetMapping("leafId")
	public Object getCache(@RequestParam String key) { 
		return leafKeyGenerator.getId(key);
	}

}
