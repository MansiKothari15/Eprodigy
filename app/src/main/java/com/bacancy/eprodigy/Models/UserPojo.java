package com.bacancy.eprodigy.Models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by samir on 28/2/18.
 */
@Entity(tableName = "UserPojo")
public class UserPojo implements Parcelable {

    @PrimaryKey
    @NonNull
    private String username;
    private String email;
    private String displayname;
    private String userstatus;
    private String password;
    private String phone_number;
    private String country_code;
    private String role_id;
    private String authy_id;
    private String verified;
    private String updated_at;
    private String profilepicture;
    private String created_at;
    private String country_name;
    private String deleteaccountstatus;
    private String privacystatus;
    private String privacyprofilephotostatus;
    private String login_status;
    private String userlastseen;
    private String contacts;
    private String approved_by_admin;
    private String IsDisable;
    private String contactcategory;
    private String profilepicture_thumb;
    private String name;
    private int status;

    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    @Embedded
    private CategoryModel categoryModel;

    public UserPojo(){

    }

    protected UserPojo(Parcel in) {
        username = in.readString();
        email = in.readString();
        displayname = in.readString();
        userstatus = in.readString();
        password = in.readString();
        phone_number = in.readString();
        country_code = in.readString();
        role_id = in.readString();
        authy_id = in.readString();
        verified = in.readString();
        updated_at = in.readString();
        profilepicture = in.readString();
        created_at = in.readString();
        country_name = in.readString();
        deleteaccountstatus = in.readString();
        privacystatus = in.readString();
        privacyprofilephotostatus = in.readString();
        login_status = in.readString();
        userlastseen = in.readString();
        contacts = in.readString();
        approved_by_admin = in.readString();
        IsDisable = in.readString();
        contactcategory = in.readString();
        profilepicture_thumb = in.readString();
        name = in.readString();
        status = in.readInt();
    }

    public static final Creator<UserPojo> CREATOR = new Creator<UserPojo>() {
        @Override
        public UserPojo createFromParcel(Parcel in) {
            return new UserPojo(in);
        }

        @Override
        public UserPojo[] newArray(int size) {
            return new UserPojo[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getAuthy_id() {
        return authy_id;
    }

    public void setAuthy_id(String authy_id) {
        this.authy_id = authy_id;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getDeleteaccountstatus() {
        return deleteaccountstatus;
    }

    public void setDeleteaccountstatus(String deleteaccountstatus) {
        this.deleteaccountstatus = deleteaccountstatus;
    }

    public String getPrivacystatus() {
        return privacystatus;
    }

    public void setPrivacystatus(String privacystatus) {
        this.privacystatus = privacystatus;
    }

    public String getPrivacyprofilephotostatus() {
        return privacyprofilephotostatus;
    }

    public void setPrivacyprofilephotostatus(String privacyprofilephotostatus) {
        this.privacyprofilephotostatus = privacyprofilephotostatus;
    }

    public String getLogin_status() {
        return login_status;
    }

    public void setLogin_status(String login_status) {
        this.login_status = login_status;
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

    public String getApproved_by_admin() {
        return approved_by_admin;
    }

    public void setApproved_by_admin(String approved_by_admin) {
        this.approved_by_admin = approved_by_admin;
    }

    public String getIsDisable() {
        return IsDisable;
    }

    public void setIsDisable(String isDisable) {
        IsDisable = isDisable;
    }

    public String getContactcategory() {
        return contactcategory;
    }

    public void setContactcategory(String contactcategory) {
        this.contactcategory = contactcategory;
    }

    public String getProfilepicture_thumb() {
        return profilepicture_thumb;
    }

    public void setProfilepicture_thumb(String profilepicture_thumb) {
        this.profilepicture_thumb = profilepicture_thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(displayname);
        parcel.writeString(userstatus);
        parcel.writeString(password);
        parcel.writeString(phone_number);
        parcel.writeString(country_code);
        parcel.writeString(role_id);
        parcel.writeString(authy_id);
        parcel.writeString(verified);
        parcel.writeString(updated_at);
        parcel.writeString(profilepicture);
        parcel.writeString(created_at);
        parcel.writeString(country_name);
        parcel.writeString(deleteaccountstatus);
        parcel.writeString(privacystatus);
        parcel.writeString(privacyprofilephotostatus);
        parcel.writeString(login_status);
        parcel.writeString(userlastseen);
        parcel.writeString(contacts);
        parcel.writeString(approved_by_admin);
        parcel.writeString(IsDisable);
        parcel.writeString(contactcategory);
        parcel.writeString(profilepicture_thumb);
        parcel.writeString(name);
        parcel.writeInt(status);
    }
}
