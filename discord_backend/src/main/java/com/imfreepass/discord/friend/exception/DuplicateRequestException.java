package com.imfreepass.discord.friend.exception;

public class DuplicateRequestException extends RuntimeException{
    public DuplicateRequestException(String message) {
        super(message);
    }
}
