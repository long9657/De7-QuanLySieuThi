package model;

import java.util.Date;
import java.util.ArrayList;

/**
 * Lớp trừu tượng cho mọi người dùng hệ thống.
 */
public abstract class User0 {
    private int id;   // khóa chính
    private String name;
    private String username;   // duy nhất
    private String password;
    private Date dateOfBirth;
    private String gender;
    private String address;
    private String email;   // duy nhất
    private String phoneNumber;   // duy nhất
    private String role;   // customer | manager | warehouse | sales | delivery

    public User0() { super(); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
