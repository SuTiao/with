package com.sutiao.app.pojo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
@Data
public class userRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -6221393892182178061L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
