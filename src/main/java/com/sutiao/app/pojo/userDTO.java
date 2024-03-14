package com.sutiao.app.pojo;

import lombok.Data;

import java.io.Serializable;
@Data
public class userDTO implements Serializable{
    private static final long serialVersionUID = 0L;
    private Long id;
    private String username;
    private String account;
    private String avatarUrl;
    private Integer status;
    private Integer type;
    private Integer Role;
}
