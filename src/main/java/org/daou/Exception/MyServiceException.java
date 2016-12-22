package org.daou.Exception;

/**
 * Service class내부 Method를 수행하다가 에러발생시, Error발생 calss명, Error Message를 담은 String을 출력한다.
 */
public class MyServiceException extends Exception{

    public MyServiceException(String msg){
        super(msg);
    }
}
