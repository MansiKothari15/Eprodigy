package com.bacancy.eprodigy.ResponseModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContactListResponse {

    /**
     * status : 200
     * message : Success
     * response_time : 0.0083 Second
     * response_data : [{"username":"","name":"04 Ayurved Doc","userstatus":"","displayname":"","email":"53512","phone":"53512","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"10 FilmyGossip","userstatus":"","displayname":"","email":"53000","phone":"53000","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"22 Masti Zone","userstatus":"","displayname":"","email":"53111","phone":"53111","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"28 Wikipedia","userstatus":"","displayname":"","email":"123122","phone":"123122","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Best Deals","userstatus":"","displayname":"","email":"121","phone":"121","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"David Bakhem","userstatus":"","displayname":"","email":"(989) 898-9777","phone":"(989) 898-9777","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Distress Number","userstatus":"","displayname":"","email":"112","phone":"112","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Eprodigy Krina","userstatus":"","displayname":"","email":"97272 92061","phone":"97272 92061","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Eprodigy Nilesh","userstatus":"","displayname":"","email":"(799) 053-8383","phone":"(799) 053-8383","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Eprodigy Nilesh","userstatus":"","displayname":"","email":"79-90538383","phone":"79-90538383","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Harindra Pittalia","userstatus":"","displayname":"","email":"88664 50087","phone":"88664 50087","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Jaini","userstatus":"","displayname":"","email":"94090 54048","phone":"94090 54048","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Jaini","userstatus":"","displayname":"","email":"94090 54048","phone":"94090 54048","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Jayesh Thummar","userstatus":"","displayname":"","email":"820-0738991","phone":"820-0738991","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Jio","userstatus":"","displayname":"","email":"79-90285003","phone":"79-90285003","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Justin Langur","userstatus":"","displayname":"","email":"(989) 898-9898","phone":"(989) 898-9898","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Kaushik Movaliya","userstatus":"","displayname":"","email":"98989 34056","phone":"98989 34056","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Lalabhai","userstatus":"","displayname":"","email":"94270 72591","phone":"94270 72591","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"MK","userstatus":"","displayname":"","email":"(989) 898-8189","phone":"(989) 898-8189","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Mark","userstatus":"","displayname":"","email":"(989) 898-9899","phone":"(989) 898-9899","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Mpesa BijliPay","userstatus":"","displayname":"","email":"*400*555#","phone":"*400*555#","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Tanisha","userstatus":"","displayname":"","email":"5065-001904","phone":"5065-001904","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"UIDAI","userstatus":"","displayname":"","email":"1800-300-1947","phone":"1800-300-1947","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""}]
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("response_time")
    private String responseTime;
    @SerializedName("response_data")
    private List<ResponseDataBean> responseData;

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

    public List<ResponseDataBean> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<ResponseDataBean> responseData) {
        this.responseData = responseData;
    }

    public static class ResponseDataBean {
        /**
         * username :
         * name : 04 Ayurved Doc
         * userstatus :
         * displayname :
         * email : 53512
         * phone : 53512
         * country_code :
         * withcountrycode :
         * profilepicture :
         * status : 0
         * contactcategory :
         * coutryname :
         */

        @SerializedName("username")
        private String username;
        @SerializedName("name")
        private String name;
        @SerializedName("userstatus")
        private String userstatus;
        @SerializedName("displayname")
        private String displayname;
        @SerializedName("email")
        private String email;
        @SerializedName("phone")
        private String phone;
        @SerializedName("country_code")
        private String countryCode;
        @SerializedName("withcountrycode")
        private String withcountrycode;
        @SerializedName("profilepicture")
        private String profilepicture;
        @SerializedName("status")
        private int status;
        @SerializedName("contactcategory")
        private String contactcategory;
        @SerializedName("coutryname")
        private String coutryname;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserstatus() {
            return userstatus;
        }

        public void setUserstatus(String userstatus) {
            this.userstatus = userstatus;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public String getProfilepicture() {
            return profilepicture;
        }

        public void setProfilepicture(String profilepicture) {
            this.profilepicture = profilepicture;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getContactcategory() {
            return contactcategory;
        }

        public void setContactcategory(String contactcategory) {
            this.contactcategory = contactcategory;
        }

        public String getCoutryname() {
            return coutryname;
        }

        public void setCoutryname(String coutryname) {
            this.coutryname = coutryname;
        }
    }
}
