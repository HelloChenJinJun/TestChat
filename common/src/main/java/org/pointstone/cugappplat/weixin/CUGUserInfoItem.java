package org.pointstone.cugappplat.weixin;

/**
 * Created by Administrator on 2017/4/8.
 */

public class CUGUserInfoItem {
    String name;
    String identity_card_number;
    String student_or_staff_id;
    String mobile_phone_number;
    boolean is_team_member;
    boolean is_betatest_member;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity_card_number() {
        return identity_card_number;
    }

    public void setIdentity_card_number(String identity_card_number) {
        this.identity_card_number = identity_card_number;
    }

    public String getStudent_or_staff_id() {
        return student_or_staff_id;
    }

    public void setStudent_or_staff_id(String student_or_staff_id) {
        this.student_or_staff_id = student_or_staff_id;
    }

    public String getMobile_phone_number() {
        return mobile_phone_number;
    }

    public void setMobile_phone_number(String mobile_phone_number) {
        this.mobile_phone_number = mobile_phone_number;
    }

    public boolean is_team_member() {
        return is_team_member;
    }

    public void setIs_team_member(boolean is_team_member) {
        this.is_team_member = is_team_member;
    }

    public boolean is_betatest_member() {
        return is_betatest_member;
    }

    public void setIs_betatest_member(boolean is_betatest_member) {
        this.is_betatest_member = is_betatest_member;
    }
}
