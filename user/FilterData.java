package user;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class FilterData {

      public static void filterData(String query, JTable table,JFrame frame) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + query);
        sorter.setRowFilter(rowFilter);

        // Check if any rows are left after filtering
        if (table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(frame, "No matches found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
