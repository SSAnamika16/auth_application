package com.substring.auth.auth_app_backend.helpers;

import java.util.UUID;

public class UserHelper {

    //change string userid into uuid

    public static UUID parseUUID(String uuid) {
        return UUID.fromString(uuid);
    }



}
