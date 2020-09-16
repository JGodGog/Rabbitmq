package m1_simple;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.连接
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Channel c = f.newConnection().createChannel();

        //2.定义队列
        //告诉服务器想使用 helloworld 队列
        //服务器检查队列如果不存在，会新建队列
        c.queueDeclare("helloworld",false,false,false,null);

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                byte[] a = delivery.getBody();
                String msg = new String(a);
                System.out.println("收到:" + msg);
            }
        };
        CancelCallback cancelCallback = new CancelCallback(){

            @Override
            public void handle(String s) throws IOException {

            }
        };

        //3.消费数据
        c.basicConsume("helloworld",true,deliverCallback,cancelCallback);
    }
}
