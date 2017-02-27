package com.cviac.nheart.nheartapp.restapi;

import java.io.Serializable;

/**
 * Created by user on 1/19/2017.
 */

public class Invitation  implements Serializable{
    private String name;
    private String mobile;
    private String to_mobile;
    private String email_id;
    private String status;
    private String pushid;

    public Invitation() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }


    public String getTo_mobile() {
        return to_mobile;
    }

    public void setTo_mobile(String to_mobile) {
        this.to_mobile = to_mobile;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }
}

