package com.cg.Wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WalletExceptionHandler {
	@ExceptionHandler({BadCredentialException.class,InvalidAttributeException.class,WalletBalanceException.class})
	public ResponseEntity<Object> handleException(Exception e)
	{
		return new ResponseEntity<Object>(e.getLocalizedMessage(),HttpStatus.UNAUTHORIZED);
	}

}
