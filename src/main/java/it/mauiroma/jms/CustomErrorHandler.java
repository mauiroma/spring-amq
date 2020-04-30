package it.mauiroma.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class CustomErrorHandler implements ErrorHandler {
    private static Logger log = LoggerFactory.getLogger(CustomErrorHandler.class);
    @Override
    public void handleError(Throwable throwable) {
        log.error(throwable.getCause().getMessage());
    }
}
