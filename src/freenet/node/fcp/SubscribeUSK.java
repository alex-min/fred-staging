/* This code is part of Freenet. It is distributed under the GNU General
 * Public License, version 2 (or at your option any later version). See
 * http://www.gnu.org/ for further details of the GPL. */
package freenet.node.fcp;

import com.db4o.ObjectContainer;

import freenet.client.async.ClientContext;
import freenet.client.async.USKCallback;
import freenet.keys.USK;
import freenet.node.NodeClientCore;
import freenet.node.RequestStarter;

public class SubscribeUSK implements USKCallback {

	// FIXME allow client to specify priorities
	final FCPConnectionHandler handler;
	final String identifier;
	final NodeClientCore core;
	final boolean dontPoll;
	final short prio;
	final short prioProgress;
	
	public SubscribeUSK(SubscribeUSKMessage message, NodeClientCore core, FCPConnectionHandler handler) {
		this.handler = handler;
		this.dontPoll = message.dontPoll;
		this.identifier = message.identifier;
		this.core = core;
		prio = message.prio;
		prioProgress = message.prioProgress;
		core.uskManager.subscribe(message.key, this, !message.dontPoll, handler.getRebootClient().lowLevelClient);
	}

	public void onFoundEdition(long l, USK key, ObjectContainer container, ClientContext context, boolean wasMetadata, short codec, byte[] data, boolean newKnownGood, boolean newSlotToo) {
		if(handler.isClosed()) {
			core.uskManager.unsubscribe(key, this, !dontPoll);
			return;
		}
		if(newKnownGood && !newSlotToo) return;
		FCPMessage msg = new SubscribedUSKUpdate(identifier, l, key);
		handler.outputHandler.queue(msg);
	}

	public short getPollingPriorityNormal() {
		return prio;
	}

	public short getPollingPriorityProgress() {
		return prioProgress;
	}

}
