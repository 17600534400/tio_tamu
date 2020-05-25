package com.tamu.tio.websocket.server;

import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * @author yang
 * 2020年5月16日17:50:54
 */
public class HelloPcaket extends Packet {
    private static final long serialVersionUID = -172060606924066412L;
    public static final int HEADER_LENGTH = 4;//消息头的长度
    public static final String CHARSET = "utf-8";
    private byte[] body;

    /**
     * @return the body
     */
    public byte[] getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    public static ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
        HelloPcaket tioPacket = (HelloPcaket) packet;
        byte[] body = tioPacket.getBody();
        int bodyLen = 0;
        if (body != null) {
            bodyLen = body.length;
        }
        // bytebuffer的总长度是 = 消息头的长度 + 消息体的长度
        int allLen = HelloPcaket.HEADER_LENGTH + bodyLen;
        // 创建一个新的bytebuffer
        ByteBuffer buffer = ByteBuffer.allocate(allLen);
        // 设置字节序
        buffer.order(tioConfig.getByteOrder());
        // 写入消息头----消息头的内容就是消息体的长度
        buffer.putInt(bodyLen);
        // 写入消息体
        if (body != null) {
            buffer.put(body);
        }
        return buffer;
    }
    public static HelloPcaket decode(ByteBuffer buffer, int limit, int position, int readableLength,
                                     ChannelContext channelContext) throws AioDecodeException {
        if (readableLength < HelloPcaket.HEADER_LENGTH) {
            return null;
        }
        // 读取消息体的长度
        int bodyLength = buffer.getInt();
        // 数据不正确，则抛出AioDecodeException异常
        if (bodyLength < 0) {
            throw new AioDecodeException(
                    "bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
        }
        // 计算本次需要的数据长度
        int neededLength = HelloPcaket.HEADER_LENGTH + bodyLength;
        // 收到的数据是否足够组包
        int isDataEnough = readableLength - neededLength;
        // 不够消息体长度(剩下的buffe组不了消息体)
        if (isDataEnough < 0) {
            return null;
        }
        HelloPcaket imPacket = new HelloPcaket();
        if (bodyLength > 0) {
            byte[] dst = new byte[bodyLength];
            buffer.get(dst);
            imPacket.setBody(dst);
        }
        return imPacket;
    }
}
