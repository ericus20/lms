package com.developersboard.lms.service.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * Created by Eric on 2/25/2018.
 *
 * @author Eric Opoku
 */
@Service
public class I18NService {

    private final MessageSource messageSource;

    @Autowired
    public I18NService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String messageId) {
        Locale locale = LocaleContextHolder.getLocale();
        return getMessage(messageId, new Object[]{}, locale);
    }

    /**
     * Try to resolve the message. Treat as an error if the message can't be found.
     *
     * @param code   the code to lookup up, such as 'calculator.noRateSet'
     * @param args   an array of arguments that will be filled in for params within
     *               the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
     *               or {@code null} if none.
     * @param locale the locale in which to do the lookup
     * @return the resolved message
     * @throws NoSuchMessageException if the message wasn't found
     * @see MessageFormat
     */
    private String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(code, args, locale);
    }
}
