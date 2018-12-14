package com.bacancy.eprodigy.API;


import com.bacancy.eprodigy.ResponseModel.ContactListResponse;
import com.bacancy.eprodigy.ResponseModel.CountryResponse;
import com.bacancy.eprodigy.ResponseModel.LastSeenResponse;
import com.bacancy.eprodigy.ResponseModel.LogoutResponse;
import com.bacancy.eprodigy.ResponseModel.MediaUploadResponse;
import com.bacancy.eprodigy.ResponseModel.MobileNumberChangeResponse;
import com.bacancy.eprodigy.ResponseModel.ProfileUploadResponse;
import com.bacancy.eprodigy.ResponseModel.RegisterResponse;
import com.bacancy.eprodigy.ResponseModel.ResendCodeResponse;
import com.bacancy.eprodigy.ResponseModel.StatusUpdateResponse;
import com.bacancy.eprodigy.ResponseModel.UserDetailResponse;
import com.bacancy.eprodigy.ResponseModel.UserDisplayNameResponse;
import com.bacancy.eprodigy.ResponseModel.VerifyUserResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by vishal on 10/2/17.
 */

public interface ApiInterface {

//    @GET("about")
//    Call<AboutResponse> about();

    @FormUrlEncoded
    @POST("createnewuser")
    Call<RegisterResponse> register(
            @FieldMap HashMap<String, String> data
    );

    @FormUrlEncoded
    @POST("verifyuser")
    Call<VerifyUserResponse> verifyUser(
            @FieldMap HashMap<String, String> data
    );

    @FormUrlEncoded
    @POST("getcountrycodelist")
    Call<CountryResponse> country(
            @FieldMap HashMap<String, String> data
    );

    @FormUrlEncoded
    @POST("resendtoken")
    Call<ResendCodeResponse> resendToken(
            @FieldMap HashMap<String, String> data
    );

    @FormUrlEncoded
    @POST("logout")
    Call<LogoutResponse> logout(
            @FieldMap HashMap<String, String> data
    );

    @FormUrlEncoded
    @POST("get_phone_contact_list")
    Call<ContactListResponse> contactList(
            @FieldMap HashMap<String, String> data
    );


    @FormUrlEncoded
    @POST("userdisplaynameupdate")
    Call<UserDisplayNameResponse> userDisplayName(
            @FieldMap HashMap<String, String> data
    );

    @Multipart
    @POST("profilepictureuploadd")
    Call<ProfileUploadResponse> profileUpload(
            @Part("username") RequestBody id,
            @Part("login_token") RequestBody fullName,
            @Part MultipartBody.Part image
    );

    @FormUrlEncoded
    @POST("getuserdetails")
    Call<UserDetailResponse> userDetails(
            @FieldMap HashMap<String, String> data
    );

    @FormUrlEncoded
    @POST("userstatusupdate")
    Call<StatusUpdateResponse> statusUpdate(
            @FieldMap HashMap<String, String> data
    );


    @FormUrlEncoded
    @POST("usermobilenumberchange")
    Call<MobileNumberChangeResponse> mobileNoChange(
            @FieldMap HashMap<String, String> data
    );



    @Multipart
    @POST("usermultiplemediaupload")
    Call<MediaUploadResponse> mediaUpload(

            @Part("username") RequestBody id,
            @Part("login_token") RequestBody fullName,
            @Part("mediacount") RequestBody mediaCount,
            @Part MultipartBody.Part[] media
    );



    @FormUrlEncoded
    @POST("userlastseenupdate")
    Call<LastSeenResponse> lastSeen(
            @FieldMap HashMap<String, String> data
    );
}
