package com.example.ttlts.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrException {
    USER_EXISTED(101,"User existed"),
    USER_NOT_EXISTED(102,"User not existed"),
    INVALID_USERNAME(103,"Username muse be at least 4 characters"),
    INVALID_PASSWORD(104,"Password muse be at least 4 characters"),
    INVALID_KEY(105,"Invalid key"),
    EMAIL_NOT_EXISTED(106,"Email not existed"),
    EMAIL_EXISTED(107,"Email existed"),
    NOT_FILE(108,"Not file"),
    DIRECTORY_CREATION_FAILD(109,"DIRECTORY_CREATION_FAILD"),
    FILE_STORAGE_FAILD(110, "File storage failed"),
    UNKNOWN_ERR(99,"Unknown error");

    private int code;
    private String message;
}
