package com.bacancy.eprodigy.ResponseModel;

import com.google.gson.annotations.SerializedName;

public class UserDetailResponse {


    /**
     * status : 200
     * message : User detail fetched successfully.
     * response_time : 0.0040 Second
     * userdata : {"username":"eprodigyo6pm0w45f91542272066","password":"eprodigyo6pm0w45f91542272066","email":"eprodigy@eprodigy.com","serverkey":"","salt":"","iterationcount":"0","created_at":"2018-11-15 08:54:26","displayname":"man_c","userstatus":"Available","phone_number":"9601564269","country_code":"91","withcountrycode":"919601564269","authy_id":"110564521","verified":"1","updated_at":"2018-11-15 03:54:26","device_type":"0","profilepicture":"https://s3.us-east-1.amazonaws.com/bacancy/eprodigyo6pm0w45f91542272066/profilepic/15bfbacdd2c306.png","device_token":"c7bc87da-1bd0-4b1b-9016-04d73183a6b0","country_name":"India","deleteaccountstatus":"0","readreceiptstatus":"1","privacystatus":"0","userlastseen":"2018-11-15 03:54:26","contacts":"","IsDisable":"0","contactcategory":"Home","login_token":"jeH8WLLy7367284581bb07597c61361a62b5f23e","role_id":"0","last_login":null,"privacylastseenstatus":"0","privacyprofilephotostatus":"0","chatbackupfileurl":null,"old_device_token":"c7bc87da-1bd0-4b1b-9016-04d73183a6b0","notification_enabled":null}
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("response_time")
    private String responseTime;
    @SerializedName("userdata")
    private UserdataBean userdata;

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

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public UserdataBean getUserdata() {
        return userdata;
    }

    public void setUserdata(UserdataBean userdata) {
        this.userdata = userdata;
    }

    public static class UserdataBean {
        /**
         * username : eprodigyo6pm0w45f91542272066
         * password : eprodigyo6pm0w45f91542272066
         * email : eprodigy@eprodigy.com
         * serverkey :
         * salt :
         * iterationcount : 0
         * created_at : 2018-11-15 08:54:26
         * displayname : man_c
         * userstatus : Available
         * phone_number : 9601564269
         * country_code : 91
         * withcountrycode : 919601564269
         * authy_id : 110564521
         * verified : 1
         * updated_at : 2018-11-15 03:54:26
         * device_type : 0
         * profilepicture : https://s3.us-east-1.amazonaws.com/bacancy/eprodigyo6pm0w45f91542272066/profilepic/15bfbacdd2c306.png
         * device_token : c7bc87da-1bd0-4b1b-9016-04d73183a6b0
         * country_name : India
         * deleteaccountstatus : 0
         * readreceiptstatus : 1
         * privacystatus : 0
         * userlastseen : 2018-11-15 03:54:26
         * contacts :
         * IsDisable : 0
         * contactcategory : Home
         * login_token : jeH8WLLy7367284581bb07597c61361a62b5f23e
         * role_id : 0
         * last_login : null
         * privacylastseenstatus : 0
         * privacyprofilephotostatus : 0
         * chatbackupfileurl : null
         * old_device_token : c7bc87da-1bd0-4b1b-9016-04d73183a6b0
         * notification_enabled : null
         */

        @SerializedName("username")
        private String username;
        @SerializedName("password")
        private String password;
        @SerializedName("email")
        private String email;
        @SerializedName("serverkey")
        private String serverkey;
        @SerializedName("salt")
        private String salt;
        @SerializedName("iterationcount")
        private String iterationcount;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("displayname")
        private String displayname;
        @SerializedName("userstatus")
        private String userstatus;
        @SerializedName("phone_number")
        private String phoneNumber;
        @SerializedName("country_code")
        private String countryCode;
        @SerializedName("withcountrycode")
        private String withcountrycode;
        @SerializedName("authy_id")
        private String authyId;
        @SerializedName("verified")
        private String verified;
        @SerializedName("updated_at")
        private String updatedAt;
        @SerializedName("device_type")
        private String deviceType;
        @SerializedName("profilepicture")
        private String profilepicture;
        @SerializedName("device_token")
        private String deviceToken;
        @SerializedName("country_name")
        private String countryName;
        @SerializedName("deleteaccountstatus")
        private String deleteaccountstatus;
        @SerializedName("readreceiptstatus")
        private String readreceiptstatus;
        @SerializedName("privacystatus")
        private String privacystatus;
        @SerializedName("userlastseen")
        private String userlastseen;
        @SerializedName("contacts")
        private String contacts;
        @SerializedName("IsDisable")
        private String IsDisable;
        @SerializedName("contactcategory")
        private String contactcategory;
        @SerializedName("login_token")
        private String loginToken;
        @SerializedName("role_id")
        private String roleId;
        @SerializedName("last_login")
        private Object lastLogin;
        @SerializedName("privacylastseenstatus")
        private String privacylastseenstatus;
        @SerializedName("privacyprofilephotostatus")
        private String privacyprofilephotostatus;
        @SerializedName("chatbackupfileurl")
        private Object chatbackupfileurl;
        @SerializedName("old_device_token")
        private String oldDeviceToken;
        @SerializedName("notification_enabled")
        private Object notificationEnabled;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getServerkey() {
            return serverkey;
        }

        public void setServerkey(String serverkey) {
            this.serverkey = serverkey;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getIterationcount() {
            return iterationcount;
        }

        public void setIterationcount(String iterationcount) {
            this.iterationcount = iterationcount;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDisplayname() {
            return displayname;
        }

        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }

        public String getUserstatus() {
            return userstatus;
        }

        public void setUserstatus(String userstatus) {
            this.userstatus = userstatus;
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

        public String getAuthyId() {
            return authyId;
        }

        public void setAuthyId(String authyId) {
            this.authyId = authyId;
        }

        public String getVerified() {
            return verified;
        }

        public void setVerified(String verified) {
            this.verified = verified;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getProfilepicture() {
            return profilepicture;
        }

        public void setProfilepicture(String profilepicture) {
            this.profilepicture = profilepicture;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getDeleteaccountstatus() {
            return deleteaccountstatus;
        }

        public void setDeleteaccountstatus(String deleteaccountstatus) {
            this.deleteaccountstatus = deleteaccountstatus;
        }

        public String getReadreceiptstatus() {
            return readreceiptstatus;
        }

        public void setReadreceiptstatus(String readreceiptstatus) {
            this.readreceiptstatus = readreceiptstatus;
        }

        public String getPrivacystatus() {
            return privacystatus;
        }

        public void setPrivacystatus(String privacystatus) {
            this.privacystatus = privacystatus;
        }

        public String getUserlastseen() {
            return userlastseen;
        }

        public void setUserlastseen(String userlastseen) {
            this.userlastseen = userlastseen;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getIsDisable() {
            return IsDisable;
        }

        public void setIsDisable(String IsDisable) {
            this.IsDisable = IsDisable;
        }

        public String getContactcategory() {
            return contactcategory;
        }

        public void setContactcategory(String contactcategory) {
            this.contactcategory = contactcategory;
        }

        public String getLoginToken() {
            return loginToken;
        }

        public void setLoginToken(String loginToken) {
            this.loginToken = loginToken;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public Object getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(Object lastLogin) {
            this.lastLogin = lastLogin;
        }

        public String getPrivacylastseenstatus() {
            return privacylastseenstatus;
        }

        public void setPrivacylastseenstatus(String privacylastseenstatus) {
            this.privacylastseenstatus = privacylastseenstatus;
        }

        public String getPrivacyprofilephotostatus() {
            return privacyprofilephotostatus;
        }

        public void setPrivacyprofilephotostatus(String privacyprofilephotostatus) {
            this.privacyprofilephotostatus = privacyprofilephotostatus;
        }

        public Object getChatbackupfileurl() {
            return chatbackupfileurl;
        }

        public void setChatbackupfileurl(Object chatbackupfileurl) {
            this.chatbackupfileurl = chatbackupfileurl;
        }

        public String getOldDeviceToken() {
            return oldDeviceToken;
        }

        public void setOldDeviceToken(String oldDeviceToken) {
            this.oldDeviceToken = oldDeviceToken;
        }

        public Object getNotificationEnabled() {
            return notificationEnabled;
        }

        public void setNotificationEnabled(Object notificationEnabled) {
            this.notificationEnabled = notificationEnabled;
        }
    }
}
