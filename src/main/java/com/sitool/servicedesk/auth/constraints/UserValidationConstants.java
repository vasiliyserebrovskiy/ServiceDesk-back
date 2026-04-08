package com.sitool.servicedesk.auth.constraints;

public final class UserValidationConstants {
    private  UserValidationConstants() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * User firstname minimum length.
     */
    public static final int FIRSTNAME_MIN = 2;
    /**
     * User firstname maximum length.
     */
    public static final int  FIRSTNAME_MAX = 100;

    /**
     * User lastname minimum length.
     */
    public static final int LASTNAME_MIN = 2;
    /**
     * User lastname maximum length.
     */
    public static final int  LASTNAME_MAX = 100;

    /**
     * Email minimum length.
     */
    public static final int EMAIL_MIN_LENGTH = 5;
    /**
     * Email maximum length.
     */
    public static final int EMAIL_MAX_LENGTH = 255;

    /**
     * Regular expression to validate email addresses with the following rules:
     * <ul>
     *   <li>The local part (before '@') may contain letters, digits, dots ('.'), underscores ('_'), percent ('%'), plus ('+'), and hyphens ('-').</li>
     *   <li>The local part cannot start or end with a dot ('.').</li>
     *   <li>The domain part (after '@') cannot start with a hyphen ('-').</li>
     *   <li>The domain consists of one or more labels separated by dots ('.'), each label containing letters, digits, or hyphens.</li>
     *   <li>The top-level domain (TLD) must be between 2 and 6 letters.</li>
     * </ul>
     */
    public static final String EMAIL_REGEX = "^(?!\\.)[A-Za-z0-9._%+-]+(?<!\\.)@(?!-)[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,6}$";

    /**
     * Password minimum length.
     */
    public static final int PASSWORD_MIN_LENGTH = 8;
    /**
     * Password maximum length.
     */
    public static final int PASSWORD_MAX_LENGTH = 255;

    /**
     * Regular expression pattern for validating password.
     * <p>
     * <ul>
     *  <li>Allows only Latin letters</li>
     *  <li>must include at least one uppercase letter</li>
     *  <li>must include at least one lowercase letter</li>
     *  <li>must include at least one digit</li>
     *  <li>must include at least one special character:  ~ ! @ # $ % ^ & * _ + = - / \ . ,</li>
     *  <li>must not contain spaces</li>
     * </ul>
     */
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*_\\+=\\-\\/\\\\\\.,])[A-Za-z\\d~!@#$%^&*_\\+=\\-\\/\\\\\\.,]+$";

}
