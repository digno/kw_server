package nz.co.rubz.kiwi.service;

import nz.co.rubz.kiwi.protocol.beans.Content;

public interface IBizService {
	
	public  Content process(Content requestContent,Object classInstance);
	
	
}
