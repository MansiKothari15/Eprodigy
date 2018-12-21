package com.bacancy.eprodigy.ResponseModel;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

public class ContactListResponse {
    /**
     * status : 200
     * message : Success
     * response_time : 0.0124 Second
     * response_data : [{"username":"","name":"Chirag Vekariya","userstatus":"","displayname":"","email":"","phone":"7866099810","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"","name":"Harindra","userstatus":"","displayname":"","email":"","phone":"9879792298,7990285003","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"eprodigy5qzvoia0r41537768945","userstatus":"\\u263a\\ufe0f\\ud83d\\ude15\\ud83d\\ude02\\ud83d\\ude00\\ud83d\\ude22","name":"Harindra","displayname":"jio","email":"eprodigy@eprodigy.com","phone":"7990285003","country_code":"91","withcountrycode":"917990285003","profilepicture":"https://s3.us-east-2.amazonaws.com/eprodigy/eprodigy5qzvoia0r41537768945/profilepic/15ba88735a9884.jpeg","status":1,"contactcategory":"","privacylastseenstatus":"0","privacyprofilephotostatus":"1","privacystatus":"0","readreceiptstatus":"0","coutryname":"India"},{"username":"","name":"Kaushik","userstatus":"","displayname":"","email":"","phone":"9714960345","country_code":"","withcountrycode":"","profilepicture":"","status":0,"contactcategory":"","coutryname":""},{"username":"eprodigyiqsv6pvxc61543822451","userstatus":"Available","name":"Pratik patel","displayname":"as","email":"eprodigy@eprodigy.com","phone":"6354698146","country_code":"91","withcountrycode":"916354698146","profilepicture":"","status":1,"contactcategory":"","privacylastseenstatus":"0","privacyprofilephotostatus":"0","privacystatus":"0","readreceiptstatus":"1","coutryname":"India"}]
     */

    private int status;
    private String message;
    private String response_time;
    private List<ResponseDataBean> response_data;

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

    public String getResponse_time() {
        return response_time;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    }

    public List<ResponseDataBean> getResponse_data() {
        return response_data;
    }

    public void setResponse_data(List<ResponseDataBean> response_data) {
        this.response_data = response_data;
    }


    @Entity(tableName = "UserPojo")
    public static class ResponseDataBean {
        /**
         * username :
         * name : Chirag Vekariya
         * userstatus :
         * displayname :
         * email :
         * phone : 7866099810
         * country_code :
         * withcountrycode :
         * profilepicture :
         * status : 0
         * contactcategory :
         * coutryname :
         * privacylastseenstatus : 0
         * privacyprofilephotostatus : 1
         * privacystatus : 0
         * readreceiptstatus : 0
         */
@NonNull
        @PrimaryKey
        private String username;
        private String name;
        private String userstatus;
        private String displayname;
        private String email;
        private String phone;
        private String country_code;
        private String withcountrycode;
        private String profilepicture;
        private int status;
        private String contactcategory;
        private String coutryname;
        private String privacylastseenstatus;
        private String privacyprofilephotostatus;
        private String privacystatus;
        private String readreceiptstatus;

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

        public String getCountry_code() {
            return country_code;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
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

        public String getPrivacystatus() {
            return privacystatus;
        }

        public void setPrivacystatus(String privacystatus) {
            this.privacystatus = privacystatus;
        }

        public String getReadreceiptstatus() {
            return readreceiptstatus;
        }

        public void setReadreceiptstatus(String readreceiptstatus) {
            this.readreceiptstatus = readreceiptstatus;
        }
    }



//new response don't delete
   /*{
    "status": 200,
    "message": "Success",
    "response_time": "0.0092 Second",
    "response_data": [
        {
            "username": "",
            "name": "04 Ayurved Doc",
            "userstatus": "",
            "displayname": "",
            "email": "53512",
            "phone": "53512",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "10 FilmyGossip",
            "userstatus": "",
            "displayname": "",
            "email": "53000",
            "phone": "53000",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "123 456",
            "userstatus": "",
            "displayname": "",
            "email": "(985) 236-9988",
            "phone": "9852369988",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "22 Masti Zone",
            "userstatus": "",
            "displayname": "",
            "email": "53111",
            "phone": "53111",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "28 Wikipedia",
            "userstatus": "",
            "displayname": "",
            "email": "123122",
            "phone": "123122",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Ad",
            "userstatus": "",
            "displayname": "",
            "email": "6354 698 146",
            "phone": "6354698146",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "eprodigy8xs3hgu1iw1543554629",
            "userstatus": "Available",
            "name": "Ad2",
            "displayname": "as",
            "email": "eprodigy@eprodigy.com",
            "phone": "9879792298",
            "country_code": "91",
            "withcountrycode": "919879792298",
            "profilepicture": "",
            "status": 1,
            "contactcategory": "",
            "privacylastseenstatus": "0",
            "privacyprofilephotostatus": "0",
            "privacystatus": "0",
            "readreceiptstatus": "1",
            "coutryname": "India"
        },
        {
            "username": "",
            "name": "Best Deals",
            "userstatus": "",
            "displayname": "",
            "email": "121",
            "phone": "121",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Chat 2",
            "userstatus": "",
            "displayname": "",
            "email": "91123456789",
            "phone": "91123456789",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "eprodigyq5fjx1fd1v1538366837",
            "userstatus": "Available",
            "name": "Chat 3",
            "displayname": "manali",
            "email": "eprodigy@eprodigy.com",
            "phone": "7787694241",
            "country_code": "1",
            "withcountrycode": "17787694241",
            "profilepicture": "https://s3.us-east-2.amazonaws.com/eprodigy/eprodigyq5fjx1fd1v1538366837/profilepic/15bb19dbd4ac9c.jpeg",
            "status": 1,
            "contactcategory": "",
            "privacylastseenstatus": "0",
            "privacyprofilephotostatus": "0",
            "privacystatus": "0",
            "readreceiptstatus": "1",
            "coutryname": "Canada"
        },
        {
            "username": "",
            "name": "Chat Rochit",
            "userstatus": "",
            "displayname": "",
            "email": "6137 044 957",
            "phone": "6137044957",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "David Bakhem",
            "userstatus": "",
            "displayname": "",
            "email": "(989) 898-9777",
            "phone": "9898989777",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Distress Number",
            "userstatus": "",
            "displayname": "",
            "email": "112",
            "phone": "112",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Eprodigy Krina",
            "userstatus": "",
            "displayname": "",
            "email": "97272 92061",
            "phone": "97272 92061",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Eprodigy Nilesh",
            "userstatus": "",
            "displayname": "",
            "email": "(799) 053-8383",
            "phone": "799 0538383",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Eprodigy Nilesh",
            "userstatus": "",
            "displayname": "",
            "email": "79-90538383",
            "phone": "7990538383",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Harindra Pittalia",
            "userstatus": "",
            "displayname": "",
            "email": "88664 50087",
            "phone": "8866450087",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Jaini",
            "userstatus": "",
            "displayname": "",
            "email": "94090 54048",
            "phone": "9409054048",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Jaini",
            "userstatus": "",
            "displayname": "",
            "email": "94090 54048",
            "phone": "94090 54048",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Jayesh",
            "userstatus": "",
            "displayname": "",
            "email": "(820) 073-8991",
            "phone": "820 0738991",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Jigar",
            "userstatus": "",
            "displayname": "",
            "email": "81281 82143",
            "phone": "8128182143",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "eprodigy5qzvoia0r41537768945",
            "userstatus": "\\u263a\\ufe0f\\ud83d\\ude15\\ud83d\\ude02\\ud83d\\ude00\\ud83d\\ude22",
            "name": "Jio",
            "displayname": "jio",
            "email": "eprodigy@eprodigy.com",
            "phone": "7990285003",
            "country_code": "91",
            "withcountrycode": "917990285003",
            "profilepicture": "https://s3.us-east-2.amazonaws.com/eprodigy/eprodigy5qzvoia0r41537768945/profilepic/15ba88735a9884.jpeg",
            "status": 1,
            "contactcategory": "",
            "privacylastseenstatus": "0",
            "privacyprofilephotostatus": "1",
            "privacystatus": "0",
            "readreceiptstatus": "0",
            "coutryname": "India"
        },
        {
            "username": "",
            "name": "Justin Langur",
            "userstatus": "",
            "displayname": "",
            "email": "(989) 898-9898",
            "phone": "9898989898",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Kaushik Movaliya",
            "userstatus": "",
            "displayname": "",
            "email": "98989 34056",
            "phone": "9898934056",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Lalabhai",
            "userstatus": "",
            "displayname": "",
            "email": "94270 72591",
            "phone": "94270 72591",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "MK",
            "userstatus": "",
            "displayname": "",
            "email": "(989) 898-8189",
            "phone": "9898988189",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "eprodigyzc64ymo08q1542269685",
            "userstatus": "At work",
            "name": "Mansi",
            "displayname": "mansi",
            "email": "eprodigy@eprodigy.com",
            "phone": "9601564269",
            "country_code": "91",
            "withcountrycode": "919601564269",
            "profilepicture": "",
            "status": 1,
            "contactcategory": "",
            "privacylastseenstatus": "0",
            "privacyprofilephotostatus": "0",
            "privacystatus": "0",
            "readreceiptstatus": "1",
            "coutryname": "India"
        },
        {
            "username": "",
            "name": "Mansi Bt",
            "userstatus": "",
            "displayname": "",
            "email": "99784 41122",
            "phone": "9978441122",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Mark",
            "userstatus": "",
            "displayname": "",
            "email": "(989) 898-9899",
            "phone": "9898989899",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Mpesa BijliPay",
            "userstatus": "",
            "displayname": "",
            "email": "*400*555#",
            "phone": "*400*555#",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "Tanisha",
            "userstatus": "",
            "displayname": "",
            "email": "5065-001904",
            "phone": "5065001904",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        },
        {
            "username": "",
            "name": "UIDAI",
            "userstatus": "",
            "displayname": "",
            "email": "1800-300-1947",
            "phone": "18003001947",
            "country_code": "",
            "withcountrycode": "",
            "profilepicture": "",
            "status": 0,
            "contactcategory": "",
            "coutryname": ""
        }
    ]
}*/
}
