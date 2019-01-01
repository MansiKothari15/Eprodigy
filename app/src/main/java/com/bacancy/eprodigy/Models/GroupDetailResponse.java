package com.bacancy.eprodigy.Models;

import java.util.List;

public class GroupDetailResponse {

    /**
     * status : 200
     * message : Group details fetch successfully.
     * response_time : 0.0015 Second
     * userdata : [{"id":"50","group_title":"video","groupname":"video_1546337418","groupimage":"https://s3.us-east-2.amazonaws.com/eprodigy/video_1546337418/profilepic/15c2b3c8bdd567.jpg","created_at":"2019-01-01 10:10:20","modify_at":"2019-01-01 05:10:20"}]
     */

    private int status;
    private String message;
    private String response_time;
    private List<UserdataBean> userdata;

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

    public List<UserdataBean> getUserdata() {
        return userdata;
    }

    public void setUserdata(List<UserdataBean> userdata) {
        this.userdata = userdata;
    }

    public static class UserdataBean {
        /**
         * id : 50
         * group_title : video
         * groupname : video_1546337418
         * groupimage : https://s3.us-east-2.amazonaws.com/eprodigy/video_1546337418/profilepic/15c2b3c8bdd567.jpg
         * created_at : 2019-01-01 10:10:20
         * modify_at : 2019-01-01 05:10:20
         */

        private String id;
        private String group_title;
        private String groupname;
        private String groupimage;
        private String created_at;
        private String modify_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGroup_title() {
            return group_title;
        }

        public void setGroup_title(String group_title) {
            this.group_title = group_title;
        }

        public String getGroupname() {
            return groupname;
        }

        public void setGroupname(String groupname) {
            this.groupname = groupname;
        }

        public String getGroupimage() {
            return groupimage;
        }

        public void setGroupimage(String groupimage) {
            this.groupimage = groupimage;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getModify_at() {
            return modify_at;
        }

        public void setModify_at(String modify_at) {
            this.modify_at = modify_at;
        }
    }
}
