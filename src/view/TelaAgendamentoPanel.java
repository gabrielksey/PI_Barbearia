package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

import controller.AgendamentoController;
import controller.ClienteController;
import controller.ServicoController;
import controller.UsuarioController;
import controller.helper.AgendaHelper;
import controller.helper.ClienteHelper;
import model.Cliente;
import model.Servico;
import model.Usuario;
import dao.AgendaDao;
import dao.ClienteDao;
import dao.ExceptionDao;
import dao.UsuarioDao;
import model.Agendamento;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.event.ItemEvent;

public class TelaAgendamentoPanel extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private PlaceholderTextField txtNomeCliente;
    private PlaceholderTextField txtCpfCliente;
    private JFormattedTextField txtValor;
    private JFormattedTextField txtDataAgenda;
    private JComboBox<String> jboxHoraAgenda; // Alterar para JComboBox
    private PlaceholderTextField txtObs;
    private JComboBox<Object> jboxServico;
    private JComboBox<Usuario> jboxBarbeiro;
    private AgendamentoController controller;
    private JTextField txtHoraAgenda;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaAgendamentoPanel frame = new TelaAgendamentoPanel(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TelaAgendamentoPanel(Agendamento agendamentoSelecionado) throws ExceptionDao {
        setBounds(100, 100, 655, 450);
        
        MaskFormatter dataMask = null;
		MaskFormatter horaMask = null;
		MaskFormatter precoMask = null;

		try {
			horaMask = new MaskFormatter("##:##");
			dataMask = new MaskFormatter("##/##/####");
			precoMask = new MaskFormatter("##.##");
		} catch (ParseException e) {
		
		}

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblAgenda = new JLabel("Agenda");
        lblAgenda.setForeground(new Color(255, 255, 255));
        lblAgenda.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        lblAgenda.setBounds(273, 59, 116, 32);
        contentPane.add(lblAgenda);

        txtNomeCliente = new PlaceholderTextField("Cliente");
        txtNomeCliente.setHorizontalAlignment(SwingConstants.CENTER);
        txtNomeCliente.setForeground(new Color(128, 128, 128));
        txtNomeCliente.setFont(new Font("Arial Black", Font.PLAIN, 12));
        txtNomeCliente.setColumns(10);
        txtNomeCliente.setBounds(74, 147, 134, 20);
        contentPane.add(txtNomeCliente);

        txtCpfCliente = new PlaceholderTextField("CPF");
        txtCpfCliente.setHorizontalAlignment(SwingConstants.CENTER);
        txtCpfCliente.setForeground(new Color(128, 128, 128));
        txtCpfCliente.setFont(new Font("Arial Black", Font.PLAIN, 12));
        txtCpfCliente.setColumns(10);
        txtCpfCliente.setBounds(74, 113, 134, 20);
        contentPane.add(txtCpfCliente);
        
        txtValor = new JFormattedTextField(precoMask);
        txtValor.setText("Valor (R$)");
        txtValor.setHorizontalAlignment(SwingConstants.CENTER);
        txtValor.setForeground(new Color(128, 128, 128));
        txtValor.setFont(new Font("Arial Black", Font.PLAIN, 12));
        txtValor.setColumns(10);
        txtValor.setBounds(74, 215, 134, 20);
        contentPane.add(txtValor);

        txtDataAgenda = new JFormattedTextField(dataMask);
        txtDataAgenda.setText("Data");
        txtDataAgenda.setHorizontalAlignment(SwingConstants.CENTER);
        txtDataAgenda.setForeground(new Color(128, 128, 128));
        txtDataAgenda.setFont(new Font("Arial Black", Font.PLAIN, 12));
        txtDataAgenda.setColumns(10);
        txtDataAgenda.setBounds(74, 246, 134, 20);
        contentPane.add(txtDataAgenda);

        txtObs = new PlaceholderTextField("Observações:");
        txtObs.setToolTipText("");
        txtObs.setHorizontalAlignment(SwingConstants.LEFT);
        txtObs.setForeground(new Color(128, 128, 128));
        txtObs.setFont(new Font("Arial Black", Font.PLAIN, 12));
        txtObs.setColumns(10);
        txtObs.setBounds(247, 113, 320, 189);
        contentPane.add(txtObs);
        
        JButton btnAgendar = new JButton("Agendar");
        btnAgendar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Obter os dados da tela
            	Servico servico = (Servico) jboxServico.getSelectedItem();
                String cpfCliente = txtCpfCliente.getText();
                String nomeUsuario = (String) jboxBarbeiro.getSelectedItem();
                String precoServico = txtValor.getText(); // String para consistência

                try {  
                    controller.cadastrarAgendamento(servico, cpfCliente, nomeUsuario,
                            txtDataAgenda.getText(), txtHoraAgenda.getText(), precoServico);
                    AgendaHelper agendaHelper = new AgendaHelper(TelaAgendamentoPanel.this);
                    agendaHelper.limparAgendamentoConcluido();
                } catch (ParseException | ExceptionDao | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao agendar: " + ex.getMessage());
                }
            }
        });
        btnAgendar.setPreferredSize(new Dimension(80, 80));
        btnAgendar.setBackground(UIManager.getColor("Button.background"));
        btnAgendar.setBounds(74, 323, 104, 27);
        contentPane.add(btnAgendar);

        JButton btnReagendar = new JButton("Reagendar");
        btnReagendar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Servico servico = (Servico) jboxServico.getSelectedItem();
                String cpfCliente = txtCpfCliente.getText();
                String nomeUsuario = (String) jboxBarbeiro.getSelectedItem();
                String precoServico = txtValor.getText(); 
                try {
                    controller.atualizarAgendamento(servico, cpfCliente, nomeUsuario, txtDataAgenda.getText(), txtHoraAgenda.getText(), precoServico);
                    AgendaHelper agendaHelper = new AgendaHelper(TelaAgendamentoPanel.this);
                    agendaHelper.limparAgendamentoConcluido();
                } catch (ParseException | ExceptionDao | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao atualizar o agendamento: " + ex.getMessage());
                }
            }
        });
        btnReagendar.setPreferredSize(new Dimension(80, 80));
        btnReagendar.setBackground(UIManager.getColor("Button.background"));
        btnReagendar.setBounds(203, 323, 104, 27);
        contentPane.add(btnReagendar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String cpfCliente = txtCpfCliente.getText();
                String dataAgendamento = txtDataAgenda.getText();

                // Converter CPF para código de cliente usando o ClienteController
                int codCliente = -1;
                try {
                    codCliente = ClienteController.buscarCodigoClientePorCPF(cpfCliente);
                } catch (ExceptionDao e1) {
                    // Lida com a exceção aqui
                    e1.printStackTrace();
                }
                
                if (codCliente != -1) {
                    // Chama o método do controlador para cancelar o agendamento
                	try {
						controller.cancelarAgendamento(codCliente, dataAgendamento);
					} catch (ExceptionDao e1) {
						e1.printStackTrace();
					}
                } else {
                    // Lidar com o caso em que o cliente não foi encontrado
                    System.out.println("Cliente não encontrado.");
                }
            }	
        	
        });
        btnCancelar.setPreferredSize(new Dimension(80, 80));
        btnCancelar.setBackground(UIManager.getColor("Button.background"));
        btnCancelar.setBounds(463, 323, 104, 27);
        contentPane.add(btnCancelar);

        jboxServico = new JComboBox<Object>();
        jboxServico.setEditable(true);
        jboxServico.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                controller = new AgendamentoController(TelaAgendamentoPanel.this);
                controller.atualizaValor();
            }
        });
        jboxServico.setToolTipText("");
        jboxServico.setBounds(74, 182, 134, 22);
        setIconifiable(true);
        setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        setBorder(null);
        setMaximizable(true);
        setClosable(true);
        setRootPaneCheckingEnabled(false);
        contentPane.add(jboxServico);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.setPreferredSize(new Dimension(80, 80));
        btnPesquisar.setBackground(UIManager.getColor("Button.background"));
        btnPesquisar.setBounds(330, 323, 104, 27);
        contentPane.add(btnPesquisar);

        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	AgendaHelper agendaHelper = new AgendaHelper(TelaAgendamentoPanel.this);
				agendaHelper.limparTelaAgendamento();
                String cpf = txtCpfCliente.getText();
                try {
                    Cliente cliente = ClienteDao.consultarClientePorCPF(cpf); 
                    if (cliente != null) {
                        preencherCampos(cliente);
                    } else {
                        JOptionPane.showMessageDialog(null, "Cliente não encontrado!");
                    }
                } catch (ExceptionDao ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        		
        jboxBarbeiro = new JComboBox<>();
        jboxBarbeiro.setToolTipText("");
        jboxBarbeiro.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        jboxBarbeiro.setName("");
        jboxBarbeiro.setForeground(new Color(128, 128, 128));
        jboxBarbeiro.setFont(new Font("Arial Black", Font.PLAIN, 12));
        jboxBarbeiro.setEditable(true);
        jboxBarbeiro.setModel(new DefaultComboBoxModel(new String[] {"João Paulo", "Fabricio Vieira", "Matheus Inacio", "Anthony Oliveira", "Sergio Ramos"}));
        jboxBarbeiro.setBounds(74, 80, 134, 22);
        contentPane.add(jboxBarbeiro);
        
        txtHoraAgenda = new JFormattedTextField(horaMask);
        txtHoraAgenda.setText("Hora");
        txtHoraAgenda.setHorizontalAlignment(SwingConstants.CENTER);
        txtHoraAgenda.setForeground(new Color(128, 128, 128));
        txtHoraAgenda.setFont(new Font("Arial Black", Font.PLAIN, 12));
        txtHoraAgenda.setText("Hora");
        txtHoraAgenda.setBounds(74, 277, 134, 20);
        contentPane.add(txtHoraAgenda);
        txtHoraAgenda.setColumns(10);
        
        JPictureBox pictureBox = new JPictureBox();
        pictureBox.setIcon(new ImageIcon(TelaAgendamentoPanel.class.getResource("/icones/wallpaper_telas_maior.png")));
        pictureBox.setBounds(0, -15, 655, 438);
        contentPane.add(pictureBox);
        
        iniciar();
    }

    static class CenteredComboBoxRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setHorizontalAlignment(SwingConstants.CENTER); // Define o alinhamento do texto ao centro
            return label;
        }
    }
	private void preencherCampos(Cliente clienteSelecionado) throws ExceptionDao {
        txtNomeCliente.setText(clienteSelecionado.getNomeCliente());
        txtCpfCliente.setText(clienteSelecionado.getCpfCliente());
    }
    
    private void iniciar() throws ExceptionDao {
        controller = new AgendamentoController(this);
        controller.atualizaServico();
        controller.atualizaValor();
    }
    
    public JComboBox<Object> getJboxServico() {
        return jboxServico;
    }
    
    public JComboBox<Usuario> getJboxBarbeiro() {
        return jboxBarbeiro;
    }
    
    public void setJboxServico(JComboBox<Object> jboxServico) {
        this.jboxServico = jboxServico;
    }

    public JFormattedTextField getTxtValor() {
        return txtValor;
    }

    public void setTxtValor(JFormattedTextField txtValor) {
        this.txtValor = txtValor;
    }

	public PlaceholderTextField getTxtCpfCliente() {
		return txtCpfCliente;
	}

	public void setTxtNomeCliente(PlaceholderTextField txtNomeCliente) {
		this.txtNomeCliente = txtNomeCliente;
	}

	public void setTxtCpfCliente(PlaceholderTextField txtCpfCliente) {
		this.txtCpfCliente = txtCpfCliente;
	}


	public void setJboxBarbeiro(JComboBox<Usuario> jboxBarbeiro) {
		this.jboxBarbeiro = jboxBarbeiro;
	}

	public JTextField getTxtHoraAgenda() {
		return txtHoraAgenda;
	}

	public void setTxtHoraAgenda(JTextField txtHoraAgenda) {
		this.txtHoraAgenda = txtHoraAgenda;
	}

	public JFormattedTextField getTxtDataAgenda() {
		return txtDataAgenda;
	}

	public void setTxtDataAgenda(JFormattedTextField txtDataAgenda) {
		this.txtDataAgenda = txtDataAgenda;
	}

	public JTextComponent getTxtNomeCliente() {
		return txtNomeCliente;
	}
}

