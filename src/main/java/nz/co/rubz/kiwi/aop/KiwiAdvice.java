package nz.co.rubz.kiwi.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import nz.co.rubz.kiwi.MsgConstants;
import nz.co.rubz.kiwi.protocol.converter.ResponseContentHelper;


@Aspect
@Component
public class KiwiAdvice {
	
	private Logger log  = Logger.getLogger(KiwiAdvice.class);

	
	@Around("@annotation(nz.co.rubz.kiwi.KiwiBiz)")
	public Object processException(ProceedingJoinPoint joinPoint){
		Object result= null;
		try {
			long startTime = System.currentTimeMillis();
			result = ((MethodInvocationProceedingJoinPoint) joinPoint).proceed();
			long endTime = System.currentTimeMillis();
			log.info(joinPoint.getSignature().getName() + " used " +( endTime -startTime) +" ms.");
		} catch (Throwable e) {
			log.error(joinPoint.toShortString() + " Caught Exception", e);
			result = ResponseContentHelper
					.genSimpleResponseContentWithoutType(
							MsgConstants.SERVER_INNER_ERROR_CODE,
							MsgConstants.SERVER_INNER_ERROR_MSG);
		}
		return  result;
		
	}
	
}
