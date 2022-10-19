package com.ly.review.netty;

import java.io.IOException;

public class Main {
    static class A {
        private String a;

        protected String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }
    static class B extends A{
        public Integer a;
        public String getA(){
            return null;
        }
    }
    public static void main(String[] args) throws IOException {
       /*  Selector selector=Selector.open();
        int select = selector.select();*/

       /* ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        serverSocketChannel.socket().bind(inetSocketAddress);
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;

        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead = " + byteRead);
                Arrays.asList(byteBuffers).stream().map(buffer -> {
                    return "position" + buffer.position() + ",limit=" + buffer.limit();
                }).forEach(System.out::println);

            }
            //将所有的buffer进行flip
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            //将数据读出显示到客户端
            long byteWrite=0;
            while (byteWrite < messageLength){
                //注意，write对于byteBuffer来说是读
                long l = socketChannel.write(byteBuffers);
                //注意，这个方法，在telnet用send，体现不出来效果，不要用send 就能体现出来了
                byteWrite+=l;

            }
            Arrays.asList(byteBuffers).forEach(byteBuffer -> {
                byteBuffer.clear();
            });
            System.out.println("byteRead:="+byteRead+" byteWrite="+byteWrite+",messagelength"+messageLength);
            //socketChannel.();
        }*/
        B b=new B();
        b.setA("a");
        System.out.println(b.a);

    }
}
