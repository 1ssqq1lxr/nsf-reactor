package com.lxr.reactor.tcp;

import com.lxr.reactor.tcp.edec.TcpDecoder;
import com.lxr.reactor.tcp.edec.TcpEncoder;
import org.junit.Test;
import reactor.ipc.netty.options.ClientOptions;
import reactor.ipc.netty.options.ServerOptions;
import reactor.ipc.netty.tcp.TcpClient;

import java.util.UUID;
import java.util.function.Consumer;

public class TestClient {

    @Test
    public void startClient()
    {
        Consumer<? super ClientOptions.Builder<?>> opsHandler= ops-> ops.host("localhost").port(10008).afterNettyContextInit(afterChannelInit->{
            afterChannelInit
                    .addHandler("decode",new TcpDecoder(1024 * 1024*10,1,2,0,0))
                    .addHandler("encode",new TcpEncoder());
        });

        TcpClient.create(opsHandler)
                .startAndAwait((in,out)->{
                    in.receiveObject().map(obj->"client测试结果=========="+obj).subscribe(System.out::println);
                    in.onReadIdle(2000,()->{
                        out.sendObject(Data.builder().header((byte)1).body(UUID.randomUUID().toString()).build()).then().subscribe();
                    });
                    return out.neverComplete();
                });
    }

}
