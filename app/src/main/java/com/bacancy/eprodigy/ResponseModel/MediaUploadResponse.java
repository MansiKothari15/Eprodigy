package com.bacancy.eprodigy.ResponseModel;

import java.util.List;

public class MediaUploadResponse {
    /*{"status":400,"message":"Oops, Something was wrong.","response_time":"2.0037 Second","mediaurl":[],"thumb":[],"type":null,"size":[],"duration":[]}*/
    /**
     * status : 200
     * message : Media file upload successfully
     * response_time : 1.5593 Second
     * mediaurl : ["https://s3.us-east-1.amazonaws.com/bacancy/eprodigy8xs3hgu1iw1543554629/media/images/eprodigy_1544767430384.jpg"]
     * thumb : []
     * type : image
     * size : ["36.99 KB"]
     * duration : []
     */

    private int status;
    private String message;
    private String response_time;
    private String type;
    private List<String> mediaurl;
    private List<String> thumb;
    private List<String> size;
    private List<String> duration;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getMediaurl() {
        return mediaurl;
    }

    public void setMediaurl(List<String> mediaurl) {
        this.mediaurl = mediaurl;
    }

    public List<?> getThumb() {
        return thumb;
    }

    public void setThumb(List<String> thumb) {
        this.thumb = thumb;
    }

    public List<String> getSize() {
        return size;
    }

    public void setSize(List<String> size) {
        this.size = size;
    }

    public List<?> getDuration() {
        return duration;
    }

    public void setDuration(List<String> duration) {
        this.duration = duration;
    }
}
