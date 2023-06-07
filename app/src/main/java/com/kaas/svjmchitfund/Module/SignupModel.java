package com.kaas.svjmchitfund.Module;

public class SignupModel {
    public String token;
    public String message;
    public String status;
    public User user;


    public class User {
        public String name;
        public String phone;
        public String password;
        public String email;
        public String password_confirmation;
        public String created_at;
        public String id;

    }
}
