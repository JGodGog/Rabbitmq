package m3_publishsubstribe;

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

        //1.定义交换机  2.定义随机队列  3.绑定
        c.exchangeDeclare("logs", BuiltinExchangeType.FANOUT.getType());

        //由服务器自动命名
        String queue = c.queueDeclare().getQueue();
        //第三个参数对于发布订阅模式无效
        c.queueBind(queue,"logs","");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                byte[] a = delivery.getBody();
                String msg = new String(a);
                System.out.println("收到:" + msg);
                for(int i = 0; i < msg.length();i++){
                    if(','==msg.charAt(i)){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                c.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                System.out.println("消息处理完毕------------------");
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
