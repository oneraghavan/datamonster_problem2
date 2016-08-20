package datamonster.notifier;

import com.twilio.sdk.TwilioRestException;

public interface Notifier {

    public void notify(String message) throws TwilioRestException;
}
