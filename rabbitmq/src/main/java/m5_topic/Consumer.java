package m5_topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        //连接
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.64.140");
        f.setUsername("admin");
        f.setPassword("admin");
        Channel c = f.newConnection().createChannel();

        //1.定义交换机 //2.定义随机队列  3.用绑定键绑定
        c.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);
        String queue = UUID.randomUUID().toString();
        c.queueDeclare(queue,false,true,true,null);

        System.out.println("输入绑定键,用空格隔开: ");
        String s = new Scanner(System.in).nextLine();
        String[] a = s.split("\\s+");
        for (String key : a){
            c.queueBind(queue,"topic_logs",key);
        }

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                byte[] a = delivery.getBody();
                String msg = new String(a);
                System.out.println("收到:" + msg);
                String key = delivery.getEnvelope().getRoutingKey();
                System.out.println("收到: "+msg+",  key="+key);
            }
        };
        CancelCallback cancelCallback = new CancelCallback(){
            @Override
            public void handle(String s) throws IOException {

            }
        };

        //正常的消费数据
        c.basicConsume(queue,true, deliverCallback, cancelCallback);
    }
}
