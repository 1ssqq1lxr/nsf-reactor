package com.lxr.reactor.tcp.edec;

import com.lxr.reactor.tcp.Data;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.Date;
import java.util.List;

public class TcpEncoder extends MessageToByteEncoder<Data> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Data data , ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(data.getHeader());
        byteBuf.writeShort(data.getBody().length());
        byteBuf.writeBytes(data.getBody().getBytes());
    }
}
