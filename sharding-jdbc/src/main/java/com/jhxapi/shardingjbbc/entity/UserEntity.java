package com.jhxapi.shardingjbbc.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String user;
	
}
