package org.daou.Exception;

/**
 * STAF관련 Class를 수행하다가 에러발생시, Error발생 calss명, Error Message를 담은 String을 출력한다.
 */
public class MyStafException extends Exception {
    public MyStafException(String msg){
        super(msg);
    }

}
