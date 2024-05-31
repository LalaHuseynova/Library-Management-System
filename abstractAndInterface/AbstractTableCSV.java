package abstractAndInterface;
import javax.swing.JTable;

public abstract class AbstractTableCSV {
  public abstract void saveDataToCSV(String filePath, JTable table);

  public abstract void loadDataFromCSV(String filePath, JTable table);
}
