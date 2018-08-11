package com.lxr.reactor.tcp;


import com.lxr.reactor.tcp.edec.TcpDecoder;
import com.lxr.reactor.tcp.edec.TcpEncoder;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.ipc.netty.NettyInbound;
import reactor.ipc.netty.NettyOutbound;
import reactor.ipc.netty.NettyPipeline;
import reactor.ipc.netty.options.ServerOptions;
import reactor.ipc.netty.tcp.TcpServer;

import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TestServer {


    @Test
    public void serverStart(){


        Consumer<? super ServerOptions.Builder<?>> opsHandler= ops-> ops.host("localhost").port(10008).afterNettyContextInit(afterChannelInit->{
            afterChannelInit
                    .addHandler("decode",new TcpDecoder(1024 * 1024*10,1,2,0,0))
                    .addHandler("encode",new TcpEncoder());
        });
        BiFunction<? super NettyInbound, ? super NettyOutbound, ? extends Publisher<Void>> newHandler=
                (in,out)->{
                    in.receiveObject().map(obj->"server测试结果=========="+obj).subscribe(System.out::println);
//                    out.onWriteIdle(5000,()->{
//                        out.context().channel().close();
//                    });
                    return out.sendObject(Data.builder().header((byte)1).body(UUID.randomUUID().toString()).build()).neverComplete();
                };
        TcpServer.create(opsHandler)
                .newHandler(newHandler)
                .block()
                .onClose()
                .block();
    }

}
