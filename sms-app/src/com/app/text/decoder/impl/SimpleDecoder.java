package com.app.text.decoder.impl;

import com.app.text.Decoder;

public class SimpleDecoder implements Decoder {

    @Override
    public String decode(String text) {
        return text;
    }
}
