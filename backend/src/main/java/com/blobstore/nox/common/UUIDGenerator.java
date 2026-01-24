package com.blobstore.nox.common;

import java.util.UUID;

public final class UUIDGenerator {

    private UUIDGenerator(){}

    public static String generateUUIDString(){
        return UUID.randomUUID().toString();
    }

}
