package com.hawen.thrift.client;

import com.hawen.thrift.pojo.RolePojo;
import com.hawen.thrift.pojo.UserPojo;
import com.hawen.thrift.service.RoleService;
import com.hawen.thrift.service.UserService;
import com.hawen.thrift.utils.R4jUtils;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import org.apache.thrift.TConfiguration;
import org.apache.thrift.TException;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.List;

public class ThriftClient {
    public static final String SERVER_IP = "localhost";//服务器IP
    public static final int SERVER_PORT = 8888;//端口
    public static final int TIMEOUT = 30000;//连接超时时间

    public static void testClient() {
        TTransport transport = null;
        try {
            TConfiguration configuration = new TConfiguration();
            //传输层
            transport = new TSocket(configuration, SERVER_IP, SERVER_PORT, TIMEOUT);
            //数据协议层
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            //从处理器层获取业务接口
            TMultiplexedProtocol userServiceMp = new TMultiplexedProtocol(protocol, "userService");
            TMultiplexedProtocol roleServiceMp = new TMultiplexedProtocol(protocol, "roleService");
            UserService.Client userClient = new UserService.Client(userServiceMp);
            transport.open();
            long id = 0L;
            long current = System.currentTimeMillis();
            while (true) {
                id++;
                UserPojo result = userClient.getUser(id);
                long now = System.currentTimeMillis();
                if (now - current >= 1000L) {
                    break;
                }
            }
            System.out.println("循环了" + id + "次");
            //获取处理器层的RoleService客户端接口
            RoleService.Client roleClient = new RoleService.Client(roleServiceMp);
            List<RolePojo> roleList = roleClient.getRoleByUserId(1L);
            System.out.println(roleList.get(0).getRoleName());
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                transport.close();//关闭连接
            }
        }


    }

    public static void testClient2() {
        TTransport transport = null;
        try {
            TConfiguration configuration = new TConfiguration();
            //传输层
            transport = new TSocket(configuration, SERVER_IP, SERVER_PORT, TIMEOUT);
            //数据协议层
            TBinaryProtocol protocol = new TBinaryProtocol(transport);
            //从处理器层获取业务接口
            TMultiplexedProtocol userServiceMp = new TMultiplexedProtocol(protocol, "userService");
            UserService.Client userClient = new UserService.Client(userServiceMp);
            //打开连接
            transport.open();
            //获取断路器
            CircuitBreaker circuitBreaker = R4jUtils.circuitBreakerRegistry().circuitBreaker("thrift");
            //捆绑事件和断路器
            CheckedFunction0<UserPojo> decorateCheckedSupplier =
                    CircuitBreaker.decorateCheckedSupplier(circuitBreaker,
                            () -> userClient.getUser(1L));
            //发送事件
            Try<UserPojo> result = Try.of(decorateCheckedSupplier)
                    .recover(ex -> null);
            System.out.println(result.get().getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                transport.close();//关闭连接
            }
        }
    }


    public static void main(String[] args) {
//        testClient();
        testClient2();
    }


}
