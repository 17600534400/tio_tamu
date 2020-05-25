/**
 * 
 */
package com.tamu.tio.websocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.http.common.HttpRequest;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.WsServerAioListener;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author tanyaowu
 * 用户根据情况来完成该类的实现
 */
public class ShowcaseServerAioListener extends WsServerAioListener {
	private static Logger log = LoggerFactory.getLogger(ShowcaseServerAioListener.class);

	public static final ShowcaseServerAioListener me = new ShowcaseServerAioListener();

	public static final String REMOVE_REMARK = "长连接断开";


	/**
	 * 本连接已处理的packet数
	 */
	public final AtomicLong        handledPackets                = new AtomicLong();

	/**
	 * 处理消息包耗时，单位：毫秒
	 * 拿这个值除以handledPackets，就是处理每个消息包的平均耗时
	 */
	public final AtomicLong        handledPacketCosts            = new AtomicLong();

	private ShowcaseServerAioListener() {

	}

	/**
	 * 建链后触发本方法，注：建连不一定成功，需要关注参数isConnected
	 * @param channelContext
	 * @param isConnected 是否连接成功,true:表示连接成功，false:表示连接失败
	 * @param isReconnect 是否是重连, true: 表示这是重新连接，false: 表示这是第一次连接
	 * @throws Exception
	 * @author: tanyaowu
	 */
	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
		super.onAfterConnected(channelContext, isConnected, isReconnect);
		if (log.isInfoEnabled()) {
			log.info("onAfterConnected\r\n{}", channelContext);
		}

	}


	/**
	 * 消息包发送之后触发本方法
	 * @param channelContext
	 * @param packet
	 * @param isSentSuccess true:发送成功，false:发送失败
	 * @throws Exception
	 * @author tanyaowu
	 */
	@Override
	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
		super.onAfterSent(channelContext, packet, isSentSuccess);
		if (log.isInfoEnabled()) {
			log.info("onAfterSent\r\n{}\r\n{}", packet.logstr(), channelContext);
		}
	}



	/**
	 * 连接关闭前触发本方法
	 * @param channelContext the channelcontext
	 * @param throwable the throwable 有可能为空
	 * @param remark the remark 有可能为空
	 * @param isRemove
	 * @author tanyaowu
	 * @throws Exception
	 */
	@Override
	public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
		super.onBeforeClose(channelContext, throwable, remark, isRemove);
		if (log.isInfoEnabled()) {
			log.info("onBeforeClose\r\n{}", channelContext);
		}

		// WsSessionContext wsSessionContext = (WsSessionContext) channelContext.get();
		//
		// if (wsSessionContext != null && wsSessionContext.isHandshaked()) {
		// 	Tio.remove(channelContext, REMOVE_REMARK);
		// 	Tio.unbindGroup("123", channelContext);
		//
		// 	SetWithLock<ChannelContext> byGroup = Tio.getByGroup(channelContext.tioConfig, "123");
		// 	int count = byGroup.getObj().size();
		//
		// 	String msg = channelContext.getClientNode().toString() + " 离开了，现在共有【" + count + "】人在线";
		// 	//用tio-websocket，服务器发送到客户端的Packet都是WsResponse
		// 	WsResponse wsResponse = WsResponse.fromText(msg, HelloPcaket.CHARSET);
		// 	HttpRequest httpRequest = wsSessionContext.getHandshakeRequest();//获取websocket握手包
		// 	String groupId = httpRequest.getParam("groupId");
		//
		// 	//群发
		// 	//Tio.sendToGroup(channelContext.tioConfig, Const.GROUP_ID, wsResponse);
		// 	Tio.sendToGroup(channelContext.tioConfig, groupId, wsResponse);
		// }
	}



	/**
	 * 原方法名：onAfterDecoded
	 * 解码成功后触发本方法
	 * @param channelContext
	 * @param packet
	 * @param packetSize
	 * @throws Exception
	 * @author: yang
	 */
	@Override
	public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
		super.onAfterDecoded(channelContext, packet, packetSize);
		if (log.isInfoEnabled()) {
			log.info("onAfterDecoded\r\n{}\r\n{}", packet.logstr(), channelContext);
		}
	}


	/**
	 * 接收到TCP层传过来的数据后
	 * @param channelContext
	 * @param receivedBytes 本次接收了多少字节
	 * @throws Exception
	 */
	@Override
	public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
		super.onAfterReceivedBytes(channelContext, receivedBytes);
		if (log.isInfoEnabled()) {
			log.info("onAfterReceivedBytes\r\n{}", channelContext);
		}
	}

	/**
	 * 处理一个消息包后
	 * @param channelContext
	 * @param packet
	 * @param cost 本次处理消息耗时，单位：毫秒
	 * @throws Exception
	 */
	@Override
	public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
		super.onAfterHandled(channelContext, packet, cost);
		if (handledPackets.get() > 0) {
			System.out.println(handledPacketCosts.get() / handledPackets.get());
		}
		System.out.println(0);
		if (log.isInfoEnabled()) {
			log.info("onAfterHandled\r\n{}\r\n{}", packet.logstr(), channelContext);
		}
	}



	/**
	 *
	 * 服务器检查到心跳超时时，会调用这个函数（一般场景，该方法只需要直接返回false即可）
	 * @param channelContext
	 * @param interval 已经多久没有收发消息了，单位：毫秒
	 * @param heartbeatTimeoutCount 心跳超时次数，第一次超时此值是1，以此类推。此值被保存在：channelContext.stat.heartbeatTimeoutCount
	 * @return 返回true，那么服务器则不关闭此连接；返回false，服务器将按心跳超时关闭该连接
	 */
	@Override
	public boolean onHeartbeatTimeout(ChannelContext channelContext, Long interval, int heartbeatTimeoutCount){
		super.onHeartbeatTimeout(channelContext, interval, heartbeatTimeoutCount);
		log.info("onHeartbeatTimeout\r\n{}\r\n{}\r\n{}",interval,heartbeatTimeoutCount,REMOVE_REMARK);
		Tio.unbindGroup("123", channelContext);
		Tio.remove(channelContext, REMOVE_REMARK);
		return false;
	}
}
