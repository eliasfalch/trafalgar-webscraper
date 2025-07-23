package at.trafalgar.gui;

import at.trafalgar.Main;
import at.trafalgar.scraper.Scraper;
import at.trafalgar.utils.Thread;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class GUI {
    private static DefaultListModel<String> urlListModel;

    public static void createGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Trafalgar WebScraper");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            JPanel main = new JPanel(new BorderLayout(15, 15));
            main.setBorder(new EmptyBorder(15, 15, 15, 15));
            frame.setContentPane(main);

            JPanel urlPanel = new JPanel(new BorderLayout(10, 10));
            urlPanel.setBorder(BorderFactory.createTitledBorder("URLs"));

            JPanel topControls = new JPanel(new BorderLayout(10, 0));
            JTextField newUrlField = new JTextField();
            topControls.add(newUrlField, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new GridLayout(1, 2, 5, 0));
            JButton addButton = new JButton("+");
            JButton removeButton = new JButton("–");
            btnPanel.add(addButton);
            btnPanel.add(removeButton);
            topControls.add(btnPanel, BorderLayout.EAST);

            urlPanel.add(topControls, BorderLayout.NORTH);

            urlListModel = new DefaultListModel<>();
            JList<String> urlList = new JList<>(urlListModel);
            urlList.setVisibleRowCount(6);
            JScrollPane listScroll = new JScrollPane(urlList);
            urlPanel.add(listScroll, BorderLayout.CENTER);

            main.add(urlPanel, BorderLayout.NORTH);

            JPanel controlPanel = new JPanel(new GridBagLayout());
            controlPanel.setBorder(BorderFactory.createTitledBorder("Konfiguration"));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0; gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.LINE_END;
            controlPanel.add(new JLabel("Präfixe:"), gbc);

            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
            controlPanel.add(Main.patternField = new JTextField("mailto:,tel:"), gbc);

            gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            JButton startButton = new JButton("Starten");
            controlPanel.add(startButton, gbc);

            main.add(controlPanel, BorderLayout.CENTER);

            Main.logArea = new JTextPane();
            Main.logArea.setEditable(false);
            Main.logArea.setFont(Main.font);
            JScrollPane logScroll = new JScrollPane(Main.logArea);
            logScroll.setBorder(BorderFactory.createTitledBorder("Log"));
            logScroll.setPreferredSize(new Dimension(800, 250));
            main.add(logScroll, BorderLayout.SOUTH);

            addButton.addActionListener(e -> {
                String url = newUrlField.getText().trim();
                if (!url.isEmpty() && !urlListModel.contains(url)) {
                    urlListModel.addElement(url);
                    newUrlField.setText("");
                }
            });

            removeButton.addActionListener(e -> {
                List<String> sel = urlList.getSelectedValuesList();
                sel.forEach(urlListModel::removeElement);
            });

            startButton.addActionListener(e -> {
                List<String> urls = new ArrayList<>();
                for (int i = 0; i < urlListModel.size(); i++) {
                    urls.add(urlListModel.getElementAt(i));
                }
                Thread.runAsync(() -> Scraper.runMultiple(urls));
            });

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
