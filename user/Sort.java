package user;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class Sort {
    private JTable personalTable;

    // Click counts for each column
    private int[] clickCounts = new int[10];

    public Sort(JTable personalTable) {
        this.personalTable = personalTable;
        addMouseListener();
    }

    private void addMouseListener() {
        personalTable.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JTableHeader header = (JTableHeader) e.getSource();
                int columnIndex = header.columnAtPoint(e.getPoint());

                // Increment click count for the clicked column
                clickCounts[columnIndex]++;

                // Sorting based on the clicked column
                sortData(columnIndex);
            }
        });
    }

    private void sortData(int columnIndex) {
        int clickCount = clickCounts[columnIndex] % 3;

        switch (clickCount) {
            case 1: // Ascending
                performSort(columnIndex, true);
                break;
            case 2: // Descending
                performSort(columnIndex, false);
                break;
            default: // Original
                resetSorting();
        }
    }

    private void performSort(int columnIndex, boolean ascending) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(personalTable.getModel());
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(columnIndex, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        personalTable.setRowSorter(sorter);
    }

    private void resetSorting() {
        personalTable.setRowSorter(null);
    }
}
