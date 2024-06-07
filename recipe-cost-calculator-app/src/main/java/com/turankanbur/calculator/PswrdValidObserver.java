/**
 * The package com.turankanbur.calculator contains classes related to the calculator application.
 */
package com.turankanbur.calculator;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This class represents an observer for password validation.
 */
public class PswrdValidObserver implements DocumentListener {

	/**
	 * Password field for entering the password.
	 */
	private JPasswordField passwordField1;

	/**
	 * Label for displaying warnings or messages.
	 */
	private JLabel warningLabel;


	/**
	 * Constructs a PasswordValidationObserver with the specified password field and
	 * warning label.
	 * 
	 * @param passwordField the password field to observe
	 * @param warningLabel  the label to display warning messages
	 */
	public PswrdValidObserver(JPasswordField passwordField, JLabel warningLabel) {
		this.passwordField1 = passwordField;
		this.warningLabel = warningLabel;
	}

	/**
	 * This method is called whenever text is inserted into the password field.
	 * It triggers the validation of the password.
	 * 
	 * @param e The DocumentEvent object representing the event.
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
	    validatePassword();
	}

	/**
	 * This method is called whenever text is removed from the password field.
	 * It triggers the validation of the password.
	 * 
	 * @param e The DocumentEvent object representing the event.
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
	    validatePassword();
	}

	/**
	 * This method is called whenever text in the password field is changed.
	 * It triggers the validation of the password.
	 * 
	 * @param e The DocumentEvent object representing the event.
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
	    validatePassword();
	}


	/**
	 * Validates the password entered in the password field and updates the warning
	 * label accordingly.
	 */
	private void validatePassword() {
		String password = new String(passwordField1.getPassword());
		if (password.length() < 2) {
			warningLabel.setText("Password must be at least 2 characters.");
		} else {
			warningLabel.setText("");
		}
	}
}