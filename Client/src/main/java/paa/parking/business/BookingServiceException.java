package paa.parking.business;

public class BookingServiceException extends RuntimeException {

  public BookingServiceException(String message) {
    super(message);
  }
  
  public BookingServiceException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public BookingServiceException(Throwable cause) {
    super(cause);
  }

  /**
   * 
   */
  private static final long serialVersionUID = -5612784179050543568L;

}
