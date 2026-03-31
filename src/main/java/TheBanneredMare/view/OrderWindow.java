package TheBanneredMare.view;

import TheBanneredMare.model.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderWindow extends JFrame {

    // Меню таверны
    private JCheckBox fireSauceCheckBox;      // "Огненный соус"
    private JCheckBox doubleFireCheckBox;     // "Двойная порция оленины"
    private JCheckBox snowBerriesCheckBox;    // "Снежные ягоды"
    private JCheckBox flatbreadCheckBox;      // "Нордская лепешка"
    private JButton orderButton;              // Кнопка заказа

    private JTable ordersTable;               // Руническая скрижаль заказов
    private OrdersTableModel tableModel;      // Язык рун (модель данных)
    private JLabel totalLabel;                // Сундук с золотом
    private JLabel baseStatsLabel;            // Учёт базовых блюд
    private JLabel modifiersStatsLabel;       // Учёт добавок

    private List<Order> ordersHistory;        // Свиток истории заказов
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    public OrderWindow() {
        setTitle("The Bannered Mare — Гарцующая кобыла: Руническая скрижаль заказов");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Как Хульда ставит столы в центре зала

        ordersHistory = new ArrayList<>();
        initComponents();
        updateStatistics();

        System.out.println("Скрижаль готова»");
    }

    private void initComponents() {
        JPanel mainHall = new JPanel(new BorderLayout(15, 15));
        mainHall.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainHall.setBackground(new Color(245, 235, 210)); // Цвет старого дуба

        JPanel counter = createTavernCounter();
        mainHall.add(counter, BorderLayout.NORTH);

        JScrollPane ordersWall = createOrdersWall();
        mainHall.add(ordersWall, BorderLayout.CENTER);

        JPanel ledger = createLedger();
        mainHall.add(ledger, BorderLayout.SOUTH);

        add(mainHall);
    }

    private JPanel createTavernCounter() {
        JPanel counter = new JPanel(new BorderLayout(10, 10));
        counter.setBackground(new Color(200, 175, 125));
        counter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 70, 30), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel sign = new JLabel("ТАВЕРНА «ГАРЦУЮЩАЯ КОБЫЛА»", SwingConstants.CENTER);
        sign.setFont(new Font("Dialog", Font.BOLD, 16));
        sign.setForeground(new Color(210, 180, 80));
        sign.setBackground(new Color(60, 40, 20));
        sign.setOpaque(true);
        counter.add(sign, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(200, 175, 125));

        JPanel dishPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dishPanel.setBackground(new Color(200, 175, 125));
        JLabel dishLabel = new JLabel("ФИРМЕННОЕ ЯСТВО: НОРДСКОЕ РАГУ — 50 СЕПТИМОВ");
        dishLabel.setFont(new Font("Dialog", Font.BOLD, 13));
        dishLabel.setForeground(new Color(60, 40, 20));
        dishPanel.add(dishLabel);
        centerPanel.add(dishPanel, BorderLayout.NORTH);


        JPanel toppingsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        toppingsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 70, 30), 2),
                "ДОБАВКИ (НЕ БОЛЕЕ ТРЁХ — МИСКА НЕ РЕЗИНОВАЯ!)️",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12),
                new Color(16, 108, 5)
        ));
        toppingsPanel.setBackground(new Color(200, 175, 125));


        fireSauceCheckBox = new JCheckBox("Огненный соус (+10 септимов)");
        doubleFireCheckBox = new JCheckBox("Двойная порция оленины (+20 септимов)");
        snowBerriesCheckBox = new JCheckBox("Снежные ягоды (+5 септимов)");
        flatbreadCheckBox = new JCheckBox("Нордская лепешка (+7 септимов)");

        JCheckBox[] checkboxes = {fireSauceCheckBox, doubleFireCheckBox, snowBerriesCheckBox, flatbreadCheckBox};
        for (JCheckBox cb : checkboxes) {
            cb.setBackground(new Color(200, 175, 125));
            cb.setFont(new Font("Dialog", Font.PLAIN, 11));
            cb.addActionListener(e -> enforceThreeToppingsRule());
        }

        toppingsPanel.add(fireSauceCheckBox);
        toppingsPanel.add(doubleFireCheckBox);
        toppingsPanel.add(snowBerriesCheckBox);
        toppingsPanel.add(flatbreadCheckBox);

        centerPanel.add(toppingsPanel, BorderLayout.CENTER);

        counter.add(centerPanel, BorderLayout.CENTER);

        orderButton = new JButton("ОФОРМИТЬ ЗАКАЗ");
        orderButton.setFont(new Font("Dialog", Font.BOLD, 14));
        orderButton.setBackground(new Color(100, 70, 30));     
        orderButton.setForeground(new Color(255, 215, 0));    
        orderButton.setFocusPainted(false);                  
        orderButton.setOpaque(true);                       
        orderButton.setBorderPainted(false);
        orderButton.setContentAreaFilled(true);
        orderButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        orderButton.addActionListener(e -> placeOrder());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(200, 175, 125));
        buttonPanel.add(orderButton);

        counter.add(buttonPanel, BorderLayout.SOUTH);

        return counter;
    }


    private void enforceThreeToppingsRule() {
        int selectedCount = countSelectedCheckboxes();

        if (selectedCount >= 3) {
            fireSauceCheckBox.setEnabled(fireSauceCheckBox.isSelected());
            doubleFireCheckBox.setEnabled(doubleFireCheckBox.isSelected());
            snowBerriesCheckBox.setEnabled(snowBerriesCheckBox.isSelected());
            flatbreadCheckBox.setEnabled(flatbreadCheckBox.isSelected());

            if (selectedCount == 3) {
                System.out.println("Три добавки — больше не лезет! Миска не резиновая!»");
            }
        } else {

            fireSauceCheckBox.setEnabled(true);
            doubleFireCheckBox.setEnabled(true);
            snowBerriesCheckBox.setEnabled(true);
            flatbreadCheckBox.setEnabled(true);
        }
    }

    private int countSelectedCheckboxes() {
        int count = 0;
        if (fireSauceCheckBox.isSelected()) count++;
        if (doubleFireCheckBox.isSelected()) count++;
        if (snowBerriesCheckBox.isSelected()) count++;
        if (flatbreadCheckBox.isSelected()) count++;
        return count;
    }


    private JScrollPane createOrdersWall() {
        tableModel = new OrdersTableModel();
        ordersTable = new JTable(tableModel);


        ordersTable.setRowHeight(30);
        ordersTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ordersTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        ordersTable.getTableHeader().setBackground(new Color(100, 70, 30));
        ordersTable.getTableHeader().setForeground(new Color(255, 215, 0));

        ordersTable.getColumnModel().getColumn(0).setPreferredWidth(90);
        ordersTable.getColumnModel().getColumn(1).setPreferredWidth(500);
        ordersTable.getColumnModel().getColumn(2).setPreferredWidth(90);


        ordersTable.setDefaultRenderer(Object.class, new TavernTableRenderer());

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 70, 30), 2),
                "РУНИЧЕСКАЯ СКРИЖАЛЬ ЗАКАЗОВ",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12),
                new Color(210, 180, 80)
        ));

        return scrollPane;
    }


    private JPanel createLedger() {
        JPanel ledger = new JPanel(new GridLayout(3, 1, 8, 8));
        ledger.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 70, 30), 2),
                "СЧЁТ ЗОЛОТА: ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Dialog", Font.BOLD, 12),
                new Color(210, 180, 80)
        ));
        ledger.setBackground(new Color(245, 235, 210));

        totalLabel = new JLabel("Общая выручка: 0 септимов");
        totalLabel.setFont(new Font("Dialog", Font.BOLD, 13));

        baseStatsLabel = new JLabel("Нордское рагу: 0 шт. | Сумма: 0 септимов");
        baseStatsLabel.setFont(new Font("Dialog", Font.PLAIN, 12));

        modifiersStatsLabel = new JLabel("Добавки: 0 шт. | Сумма: 0 септимов");
        modifiersStatsLabel.setFont(new Font("Dialog", Font.PLAIN, 12));

        ledger.add(totalLabel);
        ledger.add(baseStatsLabel);
        ledger.add(modifiersStatsLabel);

        return ledger;
    }


    private void placeOrder() {
        Order order = new Ragout();

        if (fireSauceCheckBox.isSelected()) {
            order = new FireSauce(order);
            System.out.println("«Огненный соус!»");
        }
        if (doubleFireCheckBox.isSelected()) {
            order = new DoubleFireSauce(order);
            System.out.println("«Двойная порция оленины!»");
        }
        if (snowBerriesCheckBox.isSelected()) {
            order = new SnowBerries(order);
            System.out.println("«Снежные ягоды!»");
        }
        if (flatbreadCheckBox.isSelected()) {
            order = new NordicFlatbread(order);
            System.out.println("«Нордская лепешка!»");
        }

        ordersHistory.add(order);


        tableModel.fireTableDataChanged();
        updateStatistics();

        clearOrderCounter();

        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║  «НОВЫЙ ЗАКАЗ! " + order.getFullName() + "!»");
        System.out.println("║  «К оплате: " + order.getPrice() + " септимов!»");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        JOptionPane.showMessageDialog(this,
                "═══════════════════════════════════════════\n" +
                        "ЗАКАЗ ОФОРМЛЕН! \n" +
                        "═══════════════════════════════════════════\n\n" +
                        " " + order.getFullName() + "\n\n" +
                        "Итоговая сумма: " + order.getPrice() + " септимов\n\n" +
                        "═══════════════════════════════════════════\n" +
                        "Приятного аппетита!\n" +
                        "═══════════════════════════════════════════",
                "The Bannered Mare — Гарцующая кобыла",
                JOptionPane.INFORMATION_MESSAGE);
    }


    private void clearOrderCounter() {
        fireSauceCheckBox.setSelected(false);
        doubleFireCheckBox.setSelected(false);
        snowBerriesCheckBox.setSelected(false);
        flatbreadCheckBox.setSelected(false);

        fireSauceCheckBox.setEnabled(true);
        doubleFireCheckBox.setEnabled(true);
        snowBerriesCheckBox.setEnabled(true);
        flatbreadCheckBox.setEnabled(true);
    }

    /**
     * Подсчёт золота в сундуке Хульды
     */
    private void updateStatistics() {
        int totalSum = 0;
        int baseCount = 0;
        int baseSum = 0;
        int modifiersCount = 0;
        int modifiersSum = 0;

        for (Order order : ordersHistory) {
            int orderPrice = order.getPrice();
            totalSum += orderPrice;

            baseCount++;
            baseSum += 50;

            int modifiersPrice = orderPrice - 50;
            if (modifiersPrice > 0) {
                modifiersCount += countModifiers(order);
                modifiersSum += modifiersPrice;
            }
        }

        totalLabel.setText("Общая выручка: " + totalSum + " септимов");
        baseStatsLabel.setText("Нордское рагу (база): " + baseCount + " шт. | Сумма: " + baseSum + " септимов");
        modifiersStatsLabel.setText("Добавки: " + modifiersCount + " шт. | Сумма: " + modifiersSum + " септимов");
    }


    private int countModifiers(Order order) {
        if (order instanceof OrderDecorator) {
            return 1 + countModifiers(((OrderDecorator) order).getWrappedOrder());
        }
        return 0;
    }

    /**
     * Модель данных для рунической скрижали
     */
    private class OrdersTableModel extends AbstractTableModel {
        private final String[] COLUMN_NAMES = {"ВРЕМЯ", "ОПИСАНИЕ БЛЮДА", "ЦЕНА"};

        @Override
        public int getRowCount() {
            return ordersHistory.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Order order = ordersHistory.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return LocalTime.now().format(timeFormatter);
                case 1:
                    return order.getFullName();
                case 2:
                    return order.getPrice();
                default:
                    return null;
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 2) return Integer.class;
            return String.class;
        }
    }


    private class TavernTableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                if (row % 2 == 0) {
                    c.setBackground(new Color(245, 240, 215)); 
                } else {
                    c.setBackground(new Color(225, 215, 185)); 
                }
            }

            if (column == 2) {
                setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }

            return c;
        }
    }
}