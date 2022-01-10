package com.hawen.thrift.server;

import com.hawen.thrift.service.RoleService;
import com.hawen.thrift.service.UserService;
import com.hawen.thrift.service.impl.RoleServiceImpl;
import com.hawen.thrift.service.impl.UserServiceImpl;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

//Thrift 服务器（由服务提供者实现）
public class ThriftServer {

    public static final int SERVER_PORT = 8888;

    public static void startServer() {
        try {
            System.out.println("chapter19 thrift starting ...");
            //定义处理器层
            TMultiplexedProcessor processor = new TMultiplexedProcessor();
            //注册定义的两个服务
            processor.registerProcessor("userService",
                    new UserService.Processor<UserService.Iface>(new UserServiceImpl()));
            processor.registerProcessor("roleService",
                    new RoleService.Processor<RoleService.Iface>(new RoleServiceImpl()));
            //定义服务器，以Socket（套接字） 的形式传输数据，从设置启动端口
            TServerSocket serverSocket = new TServerSocket(SERVER_PORT);
            //服务器参数
            TServer.Args args = new TServer.Args(serverSocket);
            //设置处理器层
            args.processor(processor);
            //采用二进制的数据协议
            args.protocolFactory(new TBinaryProtocol.Factory());
            //创建简易的Thrift服务器（TSimpleServer）
            TSimpleServer server = new TSimpleServer(args);
            //启动服务
            server.serve();
        } catch (Exception e) {
            System.out.println("Server start error......");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startServer();
    }



}
