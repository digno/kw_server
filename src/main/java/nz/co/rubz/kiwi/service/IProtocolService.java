package nz.co.rubz.kiwi.service;

import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;
import nz.co.rubz.kiwi.protocol.beans.Content;

public interface IProtocolService {

	public Content dispatch(KiwiMessage message);
	
	@Deprecated
	public boolean boardcast(Integer id ,String from,String to,String message);
	
	public boolean route(String from,String to,String message);
	
	
	
}
