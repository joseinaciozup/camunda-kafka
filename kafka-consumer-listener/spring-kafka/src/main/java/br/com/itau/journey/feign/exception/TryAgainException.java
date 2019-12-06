package br.com.itau.journey.feign.exception;

import feign.Request;
import feign.RetryableException;

import java.util.Date;

public abstract class TryAgainException extends RuntimeException {

}
