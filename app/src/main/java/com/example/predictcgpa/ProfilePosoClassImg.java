package com.example.predictcgpa;

public class ProfilePosoClassImg {
    private String fullName;
    private String department;
    private String email;
    private String phone;
    private String university;
    private String fatherName;
    private String motherName;
    private String address;
    private String imgUrl;

    public ProfilePosoClassImg() {
    }

    public ProfilePosoClassImg(String fullName, String department, String email, String phone, String university, String fatherName, String motherName, String address, String imgUrl) {
        this.fullName = fullName;
        this.department = department;
        this.email = email;
        this.phone = phone;
        this.university = university;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.address = address;
        this.imgUrl = imgUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
