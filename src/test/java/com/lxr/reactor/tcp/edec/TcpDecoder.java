package com.lxr.reactor.tcp.edec;

import com.lxr.reactor.tcp.Data;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Optional;

@Slf4j
public class TcpDecoder extends LengthFieldBasedFrameDecoder {

    public TcpDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                          int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength,
                lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        try{
           return Optional.ofNullable((ByteBuf)super.decode(ctx, in))
                    .map(byteBuf -> {
                        byte aByte = byteBuf.readByte();
                        short i = byteBuf.readShort();
                        String body = byteBuf.toString(byteBuf.readerIndex(), i, Charset.forName("utf-8"));
                        byteBuf.readBytes(i);
                        return Data.builder()
                                .header(aByte)
                                .body(body)
                                .build();
                    }).orElse(null);
        }
        catch (Exception e){
            log.error("协议decoder错误" ,e);
        }
        return  null;
    }
}
