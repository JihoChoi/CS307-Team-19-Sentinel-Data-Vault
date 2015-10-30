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

public class PasswordChangeView {

	private JFrame frmChangePassword;
	private JTextField textField_3;
	
	
	private String oldPass;
	private String newPass1;
	private String newPass2;
	private String answer;
	private boolean passCheck; 
	/**
	 * Launch the application.
	 */
	public User currentUser;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	public static void main(String[] args) { //Main for testing
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Make a  user for testing
					PasswordHasher l = new PasswordHasher();
					SaltGenerator twitchChat = new SaltGenerator();
					String salt = twitchChat.generateSalt();
					User u = new User("Ben", l.hashPassword("password", salt), salt, "This is my data key", "This is my sec question", "answer", null);
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
	public PasswordChangeView(User user) {
		this.currentUser = user; //get the user from SettingsView
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		frmChangePassword = new JFrame();
		frmChangePassword.setTitle("Change Password");
		frmChangePassword.setBounds(100, 100, 450, 300);
		frmChangePassword.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmChangePassword.getContentPane().setLayout(null);
		
		JLabel lblOldPassword = new JLabel("Old Password");
		lblOldPassword.setBounds(163, 40, 86, 16);
		frmChangePassword.getContentPane().add(lblOldPassword);
		
		JLabel lblNewPassword = new JLabel("New Password");
		lblNewPassword.setBounds(163, 80, 88, 16);
		frmChangePassword.getContentPane().add(lblNewPassword);
		
		JLabel lblConfirmNewPassword = new JLabel("Confirm new Password");
		lblConfirmNewPassword.setBounds(163, 114, 144, 16);
		frmChangePassword.getContentPane().add(lblConfirmNewPassword);
		
		JLabel lblAnswerToSecurity = new JLabel("Answer to Security Question");
		lblAnswerToSecurity.setBounds(163, 175, 189, 16);
		frmChangePassword.getContentPane().add(lblAnswerToSecurity);
		
		textField_3 = new JTextField(); //Answer to security question
		textField_3.setBounds(35, 172, 116, 22);
		frmChangePassword.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnNewButton = new JButton("Change");//Confirm (change) button
		
		btnNewButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				oldPass = String.valueOf(passwordField.getPassword());
				newPass1 = String.valueOf(passwordField_1.getPassword());
				newPass2 = String.valueOf(passwordField_2.getPassword());
				answer = textField_3.getText();
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
				if(newPass1 == null || newPass2 == null || answer == null){
					JOptionPane.showMessageDialog(null, "One or more fields left empty", "Change Password", 0);
				}
				//Makes sure the new passwords match each other, the old password and security q answer is correct
				else if(newPass1.equals(newPass2) && passCheck ==true && a.minStandard(newPass2) == true && answer.equals(currentUser.getSecurityAnswer())){
				
					PasswordHasher p = null; // might have issues with the null initializations here.
					try {
						p = new PasswordHasher();
					} catch (NoSuchAlgorithmException e1) {
							e1.printStackTrace();
					}
					String newPass1 = p.hashPassword(newPass2, currentUser.getPasswordSalt() );
					currentUser.setPasswordHash(newPass1);
					frmChangePassword.dispose();
				}
				//Yells at user if the above if has a false in it
				else{ 
					JOptionPane.showMessageDialog(null, "New passwords do not match. Check your security question answer.", "Change Password", 0);
					System.out.println(newPass1 + "\n" + newPass2 + "\n" + answer + "\n");
					if(passCheck == true) { 
						System.out.println("Old pass correct");
					}
					else{ System.out.println("Old pass wrong"); }
				}
				}
		});
		btnNewButton.setBounds(54, 220, 97, 25);
		frmChangePassword.getContentPane().add(btnNewButton);
		
		JButton btnCancel = new JButton("Cancel"); //Cancel button
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmChangePassword.dispose();
			}
		});
		btnCancel.setBounds(189, 220, 97, 25);
		frmChangePassword.getContentPane().add(btnCancel);
		
		JTextPane txtpnThisIsWhere = new JTextPane(); //Field that displays currentUser's security question.
		txtpnThisIsWhere.setEditable(false);
		txtpnThisIsWhere.setText(currentUser.getSecurityQuestion());
		txtpnThisIsWhere.setBounds(35, 140, 309, 22);
		frmChangePassword.getContentPane().add(txtpnThisIsWhere);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(35, 34, 116, 22);
		frmChangePassword.getContentPane().add(passwordField);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(35, 77, 116, 22);
		frmChangePassword.getContentPane().add(passwordField_1);
		
		passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(35, 111, 116, 22);
		frmChangePassword.getContentPane().add(passwordField_2);
	}
}
