package cn.hizhangyang.securitydemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppUser {

    private String username;
    private String password;
}
