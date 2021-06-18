package com.example.meerkat;

public class Token {
    private String token;
    private String position;

    public Token(){}
    public Token(String token, String position){
        this.token = token;
        this.position = position;
    }
    public String getToken(){
        return token;
    }
    public void setToken(String token){
        this.token = token;
    }
    public String getPosition(){
        return position;
    }
    public void setPosition(String position){
        this.position = position;
    }
}
