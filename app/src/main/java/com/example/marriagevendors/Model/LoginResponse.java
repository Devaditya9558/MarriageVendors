package com.example.marriagevendors.Model;

public class LoginResponse {
     private Boolean success; // Success status
     private String message; // Success or error message
     private String accessToken; // JWT token for authentication
     private User user; // Minimal user details for login

     // Getters
     public Boolean getSuccess() {
          return success;
     }

     public String getMessage() {
          return message;
     }

     public String getAccessToken() {
          return accessToken;
     }

     public User getUser() {
          return user;
     }

     // Nested User class for minimal user details after login
     public static class User {
          private String id;
          private String email;
          private String refresh_Token; // Refresh token
          private String googleUid; // Google UID for Google login
          private String user_name; // User name
          private String role; // User role
          private Boolean is_verified; // Verification status

          // Getters
          public String getId() {
               return id;
          }

          public String getEmail() {
               return email;
          }

          public String getRefresh_Token() {
               return refresh_Token;
          }

          public String getGoogleUid() {
               return googleUid;
          }

          public String getUser_name() {
               return user_name;
          }

          public String getRole() {
               return role;
          }

          public Boolean getIs_verified() {
               return is_verified;
          }
     }
}