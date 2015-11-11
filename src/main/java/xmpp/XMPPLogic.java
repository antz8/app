package xmpp;

import org.jivesoftware.smack.XMPPConnection;

public class XMPPLogic {

	private XMPPConnection connection = null;
	
	String host = "192.168.141.1";	
    String port = "5222";
    String service = "ptalk";
    
    String cekNim="0";
    int a=0;
    String id = "0";
	  private static XMPPLogic instance = null;

	  public synchronized static XMPPLogic getInstance() {
	    if(instance==null){
	      instance = new XMPPLogic();
	    }
	    return instance;
	  }

	  public void setConnection(XMPPConnection connection){
	    this.connection = connection;
	  }

	  public XMPPConnection getConnection() {
	    return this.connection;
	  }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getCekNim() {
		return cekNim;
	}

	public void setCekNim(String cekNim) {
		this.cekNim = cekNim;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	  
	
	
	
	
	
	
	  
}
