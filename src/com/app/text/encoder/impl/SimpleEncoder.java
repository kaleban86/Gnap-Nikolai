package com.app.text.encoder.impl;

import com.app.text.Encoder;

public class SimpleEncoder implements Encoder {

    @Override
    public String encode(String text) {
        return text;
    }
}
