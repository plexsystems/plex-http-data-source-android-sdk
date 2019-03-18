package com.plex.androidsdk.httpdatasources;

import com.google.gson.annotations.SerializedName;

/**
 * A class used to serialize the request input parameters into the correct JSON structure.
 */
class BaseInputs {
    @SerializedName("inputs")
    private IBaseInput inputs;

    public BaseInputs(IBaseInput baseInput) {
        inputs = baseInput;
    }
}
