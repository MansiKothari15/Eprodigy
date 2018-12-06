package com.bacancy.eprodigy.ResponseModel;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    /**
     * status : 200
     * message : You have to verify your number.
     * userdata : {"username":"eprodigy0ne3ooi9t11542270733","displayname":"mansi","email":"eprodigy@eprodigy.com","password":"eprodigy0ne3ooi9t11542270733","phone_number":"9978441122","country_code":"91","withcountrycode":"919978441122","country_name":"India","device_type":"0","device_token":"0a01a1af-b522-42bc-8c6f-54d69933ab9b","userstatus":"Available","authy_id":110562883,"created_at":"2018-11-15 08:32:13"}
     * username : eprodigy0ne3ooi9t11542270733
     * authy_id : 110562883
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("userdata")
    private UserdataBean userdata;
    @SerializedName("username")
    private String username;
    @SerializedName("authy_id")
    private int authyId;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserdataBean getUserdata() {
        return userdata;
    }

    public void setUserdata(UserdataBean userdata) {
        this.userdata = userdata;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAuthyId() {
        return authyId;
    }

    public void setAuthyId(int authyId) {
        this.authyId = authyId;
    }

    public static class UserdataBean {
        /**
         * username : eprodigy0ne3ooi9t11542270733
         * displayname : mansi
         * email : eprodigy@eprodigy.com
         * password : eprodigy0ne3ooi9t11542270733
         * phone_number : 9978441122
         * country_code : 91
         * withcountrycode : 919978441122
         * country_name : India
         * device_type : 0
         * device_token : 0a01a1af-b522-42bc-8c6f-54d69933ab9b
         * userstatus : Available
         * authy_id : 110562883
         * created_at : 2018-11-15 08:32:13
         */

        @SerializedName("username")
        private String username;
        @SerializedName("displayname")
        private String displayname;
        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;
        @SerializedName("phone_number")
        private String phoneNumber;
        @SerializedName("country_code")
        private String countryCode;
        @SerializedName("withcountrycode")
        private String withcountrycode;
        @SerializedName("country_name")
        private String countryName;
        @SerializedName("device_type")
        private String deviceType;
        @SerializedName("device_token")
        private String deviceToken;
        @SerializedName("userstatus")
        private String userstatus;
        @SerializedName("authy_id")
        private int authyId;
        @SerializedName("created_at")
        private String createdAt;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDisplayname() {
            return displayname;
        }

        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getWithcountrycode() {
            return withcountrycode;
        }

        public void setWithcountrycode(String withcountrycode) {
            this.withcountrycode = withcountrycode;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getUserstatus() {
            return userstatus;
        }

        public void setUserstatus(String userstatus) {
            this.userstatus = userstatus;
        }

        public int getAuthyId() {
            return authyId;
        }

        public void setAuthyId(int authyId) {
            this.authyId = authyId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
