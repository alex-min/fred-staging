package freenet.node;

import freenet.client.async.ClientRequester;
import freenet.support.RandomGrabArrayItem;

/**
 * A low-level request which can be sent immediately. These are registered
 * on the ClientRequestScheduler.
 */
public abstract class SendableRequest implements RandomGrabArrayItem {
	
	/** Get the priority class of the request. */
	public abstract short getPriorityClass();
	
	public abstract int getRetryCount();
	
	/** ONLY called by RequestStarter. Start the actual request using the NodeClientCore
	 * provided. The request has been removed from the structure already, if canRemove().
	 * @return True if a request was sent, false otherwise (in which case the request will
	 * be removed if it hasn't already been). */
	public abstract boolean send(NodeClientCore node);
	
	/** Get client context object */
	public abstract Object getClient();
	
	/** Get the ClientRequest */
	public abstract ClientRequester getClientRequest();
	
}
