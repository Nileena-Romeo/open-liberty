/*******************************************************************************
 * Copyright (c) 2008, 2021 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package com.ibm.ws.sip.stack.transport.sip.netty;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ibm.websphere.ras.Tr;
import com.ibm.websphere.ras.TraceComponent;
import com.ibm.ws.sip.container.protocol.SipProtocolLayer;
import com.ibm.ws.sip.stack.transaction.transport.SIPConnectionsModel;
import com.ibm.ws.sip.stack.transaction.transport.connections.SIPConnectionFactory;

import jain.protocol.ip.sip.ListeningPoint;

/**
 * factory that creates channel-framework chains
 * 
 */
public class SIPConnectionFactoryImplWs implements SIPConnectionFactory
{
	/** class logger */
	private static final TraceComponent tc = Tr.register(SIPConnectionFactoryImplWs.class);
	
	/** singleton instance */
	private static SIPConnectionFactoryImplWs s_instance = new SIPConnectionFactoryImplWs();

	/**
	 * map of all inbound channels created by this factory, indexed by listening
	 * points. this includes channels that initialized but failed to start.
	 */
	private Map<ListeningPoint, SipInboundChannel> m_channels;

	/**
	 * @return the signleton instance
	 */
	public static SIPConnectionFactoryImplWs instance() {
		return s_instance;
	}

	/**
	 * private constructor
	 */
	private SIPConnectionFactoryImplWs() {
		m_channels = new HashMap<ListeningPoint, SipInboundChannel>(8);
	}

	/**
	 * @see com.ibm.ws.sip.stack.transaction.transport.connections.SIPConnectionFactory#createListeningConnection(jain.protocol.ip.sip.ListeningPoint)
	 */
	public SipInboundChannel createListeningConnection(ListeningPoint lp)
	{
		SipInboundChannel channel = m_channels.get(lp);
		if (TraceComponent.isAnyTracingEnabled() && tc.isDebugEnabled()) {
			if (lp !=null) 
				Tr.debug(this, tc,"createListeningConnection",
				   "listening point [" + lp.toString() + ']');
			else
				Tr.debug(this, tc,"createListeningConnection", "no listening point");
		}
		return channel;
	}
	
	public SipInboundChannel getListeningConnection(ListeningPoint lp) {
		return createListeningConnection(lp);
	}
	

	/**
	 * called by the channel factory when a new inbound channel is created
	 * 
	 * @param lp listening point of the channel
	 * @param channel the new inbound channel
	 * @param chainName name of the inbound chain
	 * @throws IOException 
	 * @see SipInboundChannelFactoryWs#createChannel(com.ibm.wsspi.channelfw.framework.ChannelData)
	 */
	void addListeningConnection(ListeningPoint lp, SipInboundChannel channel, String chainName) throws IOException {
		if (TraceComponent.isAnyTracingEnabled() && tc.isDebugEnabled()) {
			Tr.debug(this, tc,"addListeningConnection",
				"adding channel ["+ channel +"] to chain [" + chainName
					+ "] on listening point [" + lp + ']');
		}
		m_channels.put(lp, channel);
		SIPConnectionsModel.instance().addedListeningPoint(lp);
		SipProtocolLayer.getInstance().initNewInterfaces(lp);
	}
	
	public Map<ListeningPoint, SipInboundChannel> getInboundChannels() {
		return m_channels;
	}
}
