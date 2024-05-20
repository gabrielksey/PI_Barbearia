package view;

import java.awt.EventQueue;

import javax.swing.JInternalFrame;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.ComponentOrientation;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SpringLayout;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import controller.ClienteController;
import controller.UsuarioController;
import dao.ExceptionDao;
import dao.ModuloConexao;
import model.Usuario;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.awt.Cursor;
import javax.swing.JFormattedTextField;


public class TelaUsuario extends JInternalFrame {

	public Connection conexao = null;
	public PreparedStatement pst = null;
	public ResultSet rs = null;

	private static final long serialVersionUID = 1L;
	private JTextField txtNomeUsuario;
	private JTextField txtSalarioUsuario;
	private JTextField txtUserUsuario;
	private JTextField txtSenhaUsuario;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaUsuario frame = new TelaUsuario(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaUsuario(Usuario usuarioSelecionado) {
		getContentPane().setBackground(new Color(232, 227, 225));
		
		setResizable(true);
		getContentPane().setSize(new Dimension(450, 480));
		getContentPane().setPreferredSize(new Dimension(450, 480));
		getContentPane().setLayout(null);
		
		JLabel lblFormularioUsuario = new JLabel("Formulário Usuario");
		lblFormularioUsuario.setBounds(217, 11, 280, 32);
		getContentPane().add(lblFormularioUsuario);
		lblFormularioUsuario.setFont(new Font("Arial Black", Font.PLAIN, 22));
	
		JLabel lblNomeusuario = new JLabel("Nome");
		lblNomeusuario.setBounds(197, 86, 60, 21);
		getContentPane().add(lblNomeusuario);
		lblNomeusuario.setFont(new Font("Arial Black", Font.PLAIN, 14));
		
		txtNomeUsuario = new JTextField();
		txtNomeUsuario.setBounds(261, 88, 176, 20);
		getContentPane().add(txtNomeUsuario);
		txtNomeUsuario.setColumns(10);
		
		JLabel lblSalarioUsuario = new JLabel("Salario");
		lblSalarioUsuario.setBounds(185, 179, 68, 21);
		getContentPane().add(lblSalarioUsuario);
		lblSalarioUsuario.setFont(new Font("Arial Black", Font.PLAIN, 14));
		
		txtSalarioUsuario = new JTextField();
		txtSalarioUsuario.setBounds(261, 181, 176, 20);
		getContentPane().add(txtSalarioUsuario);
		txtSalarioUsuario.setColumns(10);
		
		JLabel lblDataNascimentoUsuario = new JLabel("Data Nascimento");
		lblDataNascimentoUsuario.setBounds(121, 149, 136, 21);
		getContentPane().add(lblDataNascimentoUsuario);
		lblDataNascimentoUsuario.setFont(new Font("Arial Black", Font.PLAIN, 14));
		
		JLabel lblCpfUsuario = new JLabel("CPF");
		lblCpfUsuario.setBounds(204, 117, 49, 21);
		getContentPane().add(lblCpfUsuario);
		lblCpfUsuario.setFont(new Font("Arial Black", Font.PLAIN, 14));
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(202, 218, 49, 21);
		getContentPane().add(lblEmail);
		lblEmail.setFont(new Font("Arial Black", Font.PLAIN, 14));
			
		txtUserUsuario = new JTextField();
		txtUserUsuario.setColumns(10);
		txtUserUsuario.setBounds(261, 220, 176, 20);
		getContentPane().add(txtUserUsuario);
		
		JLabel lblSenhaUsuario = new JLabel("Senha");
		lblSenhaUsuario.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblSenhaUsuario.setBounds(202, 250, 49, 21);
		getContentPane().add(lblSenhaUsuario);
		
		txtSenhaUsuario = new JTextField();
		txtSenhaUsuario.setColumns(10);
		txtSenhaUsuario.setBounds(261, 252, 176, 20);
		getContentPane().add(txtSenhaUsuario);
		
		JLabel lblPerfilUsuario = new JLabel("Perfil");
		lblPerfilUsuario.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblPerfilUsuario.setBounds(202, 283, 49, 21);
		getContentPane().add(lblPerfilUsuario);
		
		final JComboBox cbPerfilUsuario = new JComboBox();
		cbPerfilUsuario.setModel(new DefaultComboBoxModel(new String[] {"Administrador", "Funcionário"}));
		cbPerfilUsuario.setBounds(261, 284, 176, 22);
		getContentPane().add(cbPerfilUsuario);
		
		MaskFormatter cpfMask = null;
		MaskFormatter dataMask = null;
		
		try {
			cpfMask = new MaskFormatter("###.###.###-##");
			dataMask = new MaskFormatter("##/##/####");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		final JFormattedTextField ftxtCpfUsuario = new JFormattedTextField(cpfMask);
		ftxtCpfUsuario.setText("");
		ftxtCpfUsuario.setBounds(261, 119, 176, 20);
		getContentPane().add(ftxtCpfUsuario);
		
		final JFormattedTextField ftxtDataNascimentoUsuario = new JFormattedTextField(dataMask);
		ftxtDataNascimentoUsuario.setText("");
		ftxtDataNascimentoUsuario.setBounds(261, 150, 176, 20);
		getContentPane().add(ftxtDataNascimentoUsuario);
		
		JLabel lblConsultarUsuario = new JLabel("ConsultarUsuario");
		lblConsultarUsuario.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblConsultarUsuario.setBounds(469, 86, 161, 21);
		getContentPane().add(lblConsultarUsuario);
		
		JLabel lblStatusUsuario = new JLabel("Status");
		lblStatusUsuario.setFont(new Font("Arial Black", Font.PLAIN, 14));
		lblStatusUsuario.setBounds(202, 312, 49, 21);
		getContentPane().add(lblStatusUsuario);
		
		JComboBox cbStatusUsuario = new JComboBox();
		cbStatusUsuario.setModel(new DefaultComboBoxModel(new String[] {"Ativo", "Inativo"}));
		cbStatusUsuario.setBounds(261, 313, 176, 22);
		getContentPane().add(cbStatusUsuario);
		
		JButton btnCadastrarUsuario = new JButton(usuarioSelecionado==null?"Cadastrar":"Alterar");
		btnCadastrarUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCadastrarUsuario.setBackground(new Color(240, 240, 240));
		btnCadastrarUsuario.setIcon(new ImageIcon(TelaUsuario.class.getResource("/icones/addicon.png")));
		btnCadastrarUsuario.setPreferredSize(new Dimension(80, 80));
		btnCadastrarUsuario.setBounds(507, 374, 117, 68);
		getContentPane().add(btnCadastrarUsuario);
		btnCadastrarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double salarioUsuario = Integer.parseInt(txtSalarioUsuario.getText());
				UsuarioController usuarioController = new UsuarioController();
				try {
					System.out.println(ftxtDataNascimentoUsuario.getText());
					usuarioController.cadastrarUsuario(txtNomeUsuario.getText(),ftxtCpfUsuario.getText(),ftxtDataNascimentoUsuario.getText(),salarioUsuario,txtUserUsuario.getText(),txtSenhaUsuario.getText(),cbPerfilUsuario.getSelectedItem().toString(),cbStatusUsuario.getSelectedItem().toString());
				} catch (ParseException | ExceptionDao e1) {
					JOptionPane.showMessageDialog(null, e1 + "Não foi possível converter os dados captados");
					e1.printStackTrace();
				}
			}
		});
		
		JButton btnConsultarUsuario = new JButton("");
		btnConsultarUsuario.setIcon(new ImageIcon(TelaUsuario.class.getResource("/icones/findicon.png")));
		btnConsultarUsuario.setBounds(507, 11, 117, 68);
		getContentPane().add(btnConsultarUsuario);
		btnConsultarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		
		setIconifiable(true);
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setBorder(null);
		setPreferredSize(new Dimension(640, 480));
		setMaximizable(true);
		setClosable(true);
		setRootPaneCheckingEnabled(false);
		setMinimumSize(new Dimension(450, 480));
		setTitle("Usuario");
		setAlignmentY(Component.TOP_ALIGNMENT);
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setBounds(100, 100, 640, 480);

	}
}
