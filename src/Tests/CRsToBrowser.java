package Tests;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;

public class CRsToBrowser extends JPanel {
	
	private static final long serialVersionUID = 360566738158526563L;
	private ArrayList<String> listaCRs;
	private JButton btnAbrirCRs;
	private JButton btnColar;
	private JScrollPane jScrollPane1;
	private JTextArea jTextArea1;
	private Scanner scanner;
	private JButton btnClear;

	/**
	 * Create the panel.
	 */
	public CRsToBrowser() {
		setMaximumSize(new Dimension(210, 32767));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 200, 70, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Insert the CRs");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 24));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.weighty = 1.0;
		gbc_lblNewLabel.weightx = 1.0;
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(2, 2, 5, 2);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		jScrollPane1 = new JScrollPane();
		jScrollPane1.setPreferredSize(new Dimension(151, 600));
		jScrollPane1.setMinimumSize(new Dimension(150, 500));
		jScrollPane1.setMaximumSize(new Dimension(152, 32767));
		GridBagConstraints gbc_jScrollPane1 = new GridBagConstraints();
		gbc_jScrollPane1.weightx = 1.0;
		gbc_jScrollPane1.weighty = 30.0;
		gbc_jScrollPane1.fill = GridBagConstraints.VERTICAL;
		gbc_jScrollPane1.insets = new Insets(2, 2, 5, 0);
		gbc_jScrollPane1.gridx = 0;
		gbc_jScrollPane1.gridy = 1;
		add(jScrollPane1, gbc_jScrollPane1);
		
		jTextArea1 = new JTextArea();
		jTextArea1.setFont(new Font("Arial", Font.PLAIN, 14));
		jTextArea1.setTabSize(4);
		jScrollPane1.setViewportView(jTextArea1);
		jTextArea1.setMargin(new Insets(5, 5, 5, 5));
		
		btnClear = new JButton("Clear");
		btnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				jTextArea1.setText("");
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnClear.setPreferredSize(new Dimension(100, 50));
		btnClear.setMinimumSize(new Dimension(100, 50));
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.weighty = 1.0;
		gbc_btnClear.weightx = 10.0;
		gbc_btnClear.fill = GridBagConstraints.VERTICAL;
		gbc_btnClear.insets = new Insets(3, 3, 20, 0);
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 2;
		add(btnClear, gbc_btnClear);
		
		btnColar = new JButton("Paste");
		btnColar.setPreferredSize(new Dimension(100, 50));
		btnColar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnColar.setMinimumSize(new Dimension(100, 50));
		btnColar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnColarActionPerformed();
			}
		});
		GridBagConstraints gbc_btnColar = new GridBagConstraints();
		gbc_btnColar.insets = new Insets(3, 3, 20, 0);
		gbc_btnColar.fill = GridBagConstraints.VERTICAL;
		gbc_btnColar.weighty = 1.0;
		gbc_btnColar.weightx = 1.0;
		gbc_btnColar.gridx = 0;
		gbc_btnColar.gridy = 3;
		add(btnColar, gbc_btnColar);
		
		btnAbrirCRs = new JButton("Open");
		btnAbrirCRs.setPreferredSize(new Dimension(100, 50));
		btnAbrirCRs.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAbrirCRs.setMinimumSize(new Dimension(100, 50));
		btnAbrirCRs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnAbrirCRsActionPerformed();
			}
		});
		GridBagConstraints gbc_btnAbrirCRs = new GridBagConstraints();
		gbc_btnAbrirCRs.insets = new Insets(3, 3, 20, 0);
		gbc_btnAbrirCRs.fill = GridBagConstraints.VERTICAL;
		gbc_btnAbrirCRs.weighty = 1.0;
		gbc_btnAbrirCRs.weightx = 10.0;
		gbc_btnAbrirCRs.gridx = 0;
		gbc_btnAbrirCRs.gridy = 4;
		add(btnAbrirCRs, gbc_btnAbrirCRs);
	}
	
	
	
	
	private void btnAbrirCRsActionPerformed() {
		for (String s : this.jTextArea1.getText().split("\n")) {
			try {
				Desktop.getDesktop().browse(new URI("http://idart.mot.com/browse/" + s));
				Thread.sleep(500);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Exception: " + ex.getMessage());
			}
		}
	}

	private void btnColarActionPerformed() {
		this.listaCRs = new ArrayList<String>();
		this.jTextArea1.setText(null);

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		try {
			String string = (String) clipboard.getData(DataFlavor.stringFlavor);

			scanner = new Scanner(string);
			String str;
			while (scanner.hasNext()) {
				str = scanner.nextLine();
				this.listaCRs.add(str);
				this.jTextArea1.append(str + "\n");
			}
		} catch (Exception ex) {
			JOptionPane
					.showMessageDialog(this, "Exception: " + ex.getMessage());
		}
	}
}
