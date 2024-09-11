package com.cairu.statuscar.service;

public interface  LoginCallback   {
    void onSuccess();
    void onFailure(Exception e);
}