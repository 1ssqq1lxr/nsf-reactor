package com.lxr.reactor.tcp;

import lombok.Builder;

@lombok.Data
@Builder
public class Data {

    private  byte header;

    private String body;

}
