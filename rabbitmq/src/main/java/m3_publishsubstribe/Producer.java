package m3_publishsubstribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 连接
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140");
        //f.setPort(5672); //默认端口可以省略
        f.setUsername("admin");
        f.setPassword("admin");

        Channel c = f.newConnection().createChannel();

        //定义交换机
        c.exchangeDeclare("logs", BuiltinExchangeType.FANOUT);

        //发送消息
        while (true){
            System.out.println("输入消息: ");
            String msg = new Scanner(System.in).nextLine();
            c.basicPublish("logs","",null,msg.getBytes());
        }
    }
}
