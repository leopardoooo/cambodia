package CASche.common;


/**
 *µ÷¶È´íÎó
 * */
public class SchedultException extends Exception {

	public SchedultException( String msg ){
		super(msg);
	}

	public SchedultException( Exception e){
		super(e);
	}

	public SchedultException( String msg ,Exception e){
		super( msg , e ) ;
	}
}