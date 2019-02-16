package mycodlabs.utils;

/**
 * Created by yoonus on 5/23/2018.
 */

public class Events {

    // Event used to send message from fragment to activity.
    public static class FragmentActivityMessage {
        private String message;
        public FragmentActivityMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to fragment.
    public static class ActivityFragmentMessage {
        private String message;
        public ActivityFragmentMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to activity.
    public static class ActivityActivityMessage {
        private String message;
        public ActivityActivityMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}