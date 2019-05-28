package com.codingwithmitch.unittesting2.ui;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    public Resource(@NonNull Status status, @Nullable T data, @NonNull String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data, @NonNull String message) {
        return new Resource<>(Status.SUCCESS, data, message);
    }

    public static <T> Resource<T> error( @Nullable T data, @NonNull String msg) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    public enum Status { SUCCESS, ERROR, LOADING}

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != getClass() || obj.getClass() != Resource.class){
            return false;
        }

        Resource<T> resource = (Resource) obj;

        if(resource.status != this.status){
            return false;
        }

        if(this.data != null){
            if(resource.data != this.data){
                return false;
            }
        }

        if(resource.message != null){
            if(this.message == null){
                return false;
            }
            if(!resource.message.equals(this.message)){
                return false;
            }
        }

        return true;
    }
}

