package com.sutiao.app.pojo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class userLoginRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -1241508406167091751L;
    private String userAccount;
    private String userPassword;
}
