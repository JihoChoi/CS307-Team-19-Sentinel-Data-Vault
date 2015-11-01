package userInterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextPane;
import dataManagement.User;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.awt.event.ActionEvent;
import cryptography.PasswordHasher;
import cryptography.SaltGenerator;
import security.PasswordValidation;
import javax.swing.JPasswordField;
import controllers.VaultController;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
public class PasswordChangeView {

	public JFrame frmChangePassword;
	private JTextField textField_3;
	
	
	private String oldPass;
	private String newPass1;
	private String newPass2;
	private String oldAnswer;
	private boolean passCheck; 
	private String question;
	private String newAnswer;
	/**
	 * Launch the application.
	 */
	public User currentUser;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	private JTextField textField;
	public static void main(String[] args) { //Main for testing
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Make a  user for testing
					PasswordHasher l = new PasswordHasher();
					SaltGenerator twitchChat = new SaltGenerator();
					String salt = twitchChat.generateSalt();
					User u = new User("ben@purdue.edu", l.hashPassword("password", salt), salt, "This is my data key", "This is my sec question", "answer", null);
					PasswordChangeView window = new PasswordChangeView(u);
					window.frmChangePassword.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PasswordChangeView(User user) { //CONSTRUCTOR
		this.currentUser = user; //get the user from SettingsView
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() { //METHOD THAT DOES USEFUL THINGS
		
		
		frmChangePassword = new JFrame();
		frmChangePassword.setResizable(false);
		frmChangePassword.setTitle("Change Password");
		frmChangePassword.setBounds(100, 100, 413, 408);
		frmChangePassword.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmChangePassword.getContentPane().setLayout(null);
		
		JLabel lblOldPassword = new JLabel("Current Password"); //Old password field label
		lblOldPassword.setBounds(167, 33, 114, 16);
		frmChangePassword.getContentPane().add(lblOldPassword);
		
		JLabel lblNewPassword = new JLabel("New Password"); //new password field label
		lblNewPassword.setBounds(165, 94, 88, 16);
		frmChangePassword.getContentPane().add(lblNewPassword);
		
		JLabel lblConfirmNewPassword = new JLabel("Confirm new Password"); //confirm password field label
		lblConfirmNewPassword.setBounds(165, 128, 144, 16);
		frmChangePassword.getContentPane().add(lblConfirmNewPassword);
		
		JLabel lblAnswerToSecurity = new JLabel("Answer to Security Question"); //question answer field label
		lblAnswerToSecurity.setBounds(165, 189, 189, 16);
		frmChangePassword.getContentPane().add(lblAnswerToSecurity);
		
		textField_3 = new JTextField(); //Answer to security question
		textField_3.setBounds(37, 186, 116, 22);
		frmChangePassword.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnNewButton = new JButton("Change");//Confirm (change) button
		
		btnNewButton.addActionListener(new ActionListener() { //If you read this, I am sorry. I hope the comments help.
			
			public void actionPerformed(ActionEvent e) {
				//<Field initializations>
				oldPass = String.valueOf(passwordField.getPassword());
				newPass1 = String.valueOf(passwordField_1.getPassword());
				newPass2 = String.valueOf(passwordField_2.getPassword());
				oldAnswer = textField_3.getText();
				newAnswer = textField.getText();
				//</Field initializations>
				if( question == null || question.equals("Please choose a security question below") == true) { 
					question =  null;
					//this if statement makes sure the question is null if the user has not chosen a vaild new question
				}

				//Old password validation
				PasswordValidation a = new PasswordValidation(oldPass);
				try{  
					if(a.isValidPassword(currentUser, oldPass) == true) { 
						passCheck = true;
					}
					else { 
						passCheck = false;
					}
				}
				catch ( NoSuchAlgorithmException k){ 
					k.printStackTrace();
				}
				//Making sure the user puts stuff in.
				if(newPass1 == null || newPass2 == null || oldAnswer == null && (question == null || newAnswer == null)){
					JOptionPane.showMessageDialog(null, "One or more fields left empty", "Change Password", 0);
					//If all fields are empty
				}
				else if ( passCheck == false) {  //if current password is wrong
					JOptionPane.showMessageDialog(null, "Current password is incorrect.", "Change Password", 0);
				}
				else if ( newPass1.equals(newPass2) && newPass2.equals(oldPass)) { //if the new password matches the old one
					JOptionPane.showMessageDialog(null, "New password cannot match the old password.", "Change Password", 0);
				}
				else if (newPass1.equals(newPass2) == false) { //if the new password and it's confirm don't match
					JOptionPane.showMessageDialog(null, "Check to make sure the new passwords match.", "Change Password", 0);
				}
				else if (question != null && newAnswer != null && passCheck == true && ((newPass1 == null && newPass2 == null && oldAnswer == null) ||
						(newPass1.equals("")== true && newPass2.equals("") == true && oldAnswer.equals("") == true ))) {
					//if there is something in for the new question and new answer, the current password is right, and all the password change fields are blank
					currentUser.setSecurityQuestion(question);
					currentUser.setSecurityAnswer(newAnswer);
					//TODO GET TO THE DATABASE!
					frmChangePassword.dispose();
				}
				//Makes sure the new passwords match each other, the old password and security q answer is correct
				else if(newPass1.equals(newPass2) && passCheck ==true && a.minStandard(newPass2) == true && oldAnswer.equals(currentUser.getSecurityAnswer())){
				 //if the new password stuff is right (newPass1 == newPass2) the new password passes the min security level,
					//and the security answer that was input is correct
					PasswordHasher p = null; // might have issues with the null initializations here.
					try {
						p = new PasswordHasher();
					} catch (NoSuchAlgorithmException e1) {
							e1.printStackTrace();
					}
					String newPass1 = p.hashPassword(newPass2, currentUser.getPasswordSalt() );
					currentUser.setPasswordHash(newPass1);
					//TODO Get the updated user to the database!
					frmChangePassword.dispose();
				}
				else if (question != null && newAnswer != null && oldAnswer.equals(currentUser.getSecurityAnswer()) && a.minStandard(newPass2) 
							&& newPass1.equals(newPass2) && passCheck == true) {
					//If ALL fields are true and valid
					currentUser.setSecurityQuestion(question);
					currentUser.setSecurityAnswer(newAnswer);
					PasswordHasher p = null; // might have issues with the null initializations here.
					try {
						p = new PasswordHasher();
					} catch (NoSuchAlgorithmException e1) {
							e1.printStackTrace();
					}
					String newPass1 = p.hashPassword(newPass2, currentUser.getPasswordSalt() );
					currentUser.setPasswordHash(newPass1);
					//TODO Get the updated user to the database!
					frmChangePassword.dispose();
					}
				
				//Yells at user if the above if has a false in it
				else{ 
					JOptionPane.showMessageDialog(null, "Ensure your current security question answer is correct and \n that all fields for the subcatagory you are changing are filled in.", "Change Password", 0);
				}
				}
		});
		btnNewButton.setBounds(63, 320, 97, 25);
		frmChangePassword.getContentPane().add(btnNewButton);
		
		JButton btnCancel = new JButton("Cancel"); //Cancel button
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmChangePassword.dispose();
			}
		});
		btnCancel.setBounds(198, 320, 97, 25);
		frmChangePassword.getContentPane().add(btnCancel);
		
		JTextPane txtpnThisIsWhere = new JTextPane(); //Field that displays currentUser's security question.
		txtpnThisIsWhere.setEditable(false);
		txtpnThisIsWhere.setText(currentUser.getSecurityQuestion());
		txtpnThisIsWhere.setBounds(37, 154, 309, 22);
		frmChangePassword.getContentPane().add(txtpnThisIsWhere);
		
		passwordField = new JPasswordField(); //Password field for current password
		passwordField.setToolTipText("Current user password must be entered to change passwords or security question/answer");
		passwordField.setBounds(39, 27, 116, 22);
		frmChangePassword.getContentPane().add(passwordField);
		
		passwordField_1 = new JPasswordField(); //Password field for newPass1
		passwordField_1.setToolTipText("Password that you want to change to.");
		passwordField_1.setBounds(37, 91, 116, 22);
		frmChangePassword.getContentPane().add(passwordField_1);
		
		passwordField_2 = new JPasswordField(); //Password field for newPass2
		passwordField_2.setToolTipText("Confirmation of the new password.");
		passwordField_2.setBounds(37, 125, 116, 22);
		frmChangePassword.getContentPane().add(passwordField_2);
		
		JLabel lblChangeSecurityQuestion = new JLabel("New Security Question"); //Label for new sec question
		lblChangeSecurityQuestion.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblChangeSecurityQuestion.setBounds(101, 218, 194, 16);
		frmChangePassword.getContentPane().add(lblChangeSecurityQuestion);
		
		textField = new JTextField(); //NEW SECURITY QUESTION ANSWER BOX
		textField.setBounds(37, 276, 116, 22);
		frmChangePassword.getContentPane().add(textField);
		textField.setColumns(10);
		
		JComboBox comboBox = new JComboBox(); //New security question combo box. 
		comboBox.setToolTipText("Current password and a new security question and answer are REQUIRED to change security questions.");
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				question = (String) comboBox.getSelectedItem();
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Please choose a security question below", "What is the maiden name of your mother?", "What is name of your pet", "Where is your favorite city?"}));
		comboBox.setBounds(37, 241, 309, 22);
		frmChangePassword.getContentPane().add(comboBox);
		
		JLabel lblNewLabel = new JLabel("New Security Question Answer"); //Label for answer text field
		lblNewLabel.setBounds(165, 279, 189, 16);
		frmChangePassword.getContentPane().add(lblNewLabel);
		
		JLabel lblNewPassword_1 = new JLabel("New Password"); //label for new password field
		lblNewPassword_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewPassword_1.setBounds(117, 62, 114, 16);
		frmChangePassword.getContentPane().add(lblNewPassword_1);
	}
}
