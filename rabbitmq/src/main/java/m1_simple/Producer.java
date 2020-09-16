package m1_simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws Exception{
        //1.连接
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Channel c = f.newConnection().createChannel();

        //2.定义队列
        //指定用来发送消息的队列，如果队列不存在，服务器会为你创建队列
        //如果已经存在，直接使用这个队列
        c.queueDeclare("helloworld",false,false,false,null);

        //3.发送消息
        c.basicPublish("","helloworld",null,"hello world!".getBytes());
        System.out.println("消息已发送");

        //断开连接
        c.close();

    }
}
