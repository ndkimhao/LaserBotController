package laserbot_galileo.gui;

import java.awt.Component;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Đối tượng hỗ trợ cho việc validate các control trên Form
 *
 * @author KimHao
 */
public class Validate {

    /**
     * Mảng hai chiều chứa thông tin của các control, được gán giá trị lúc khởi
     * tạo đối tượng bằng contrustor hoặc bằng bộ getter và setter
     */
    private Object[][] objectToValidate;

    /**
     * Contrustor khởi tạo validate
     *
     * @param objectToValidate là các control cần validate, là các mảng Object,
     * phần tử số 1 là biến control, phần tử 2 là tên control, các phần tử còn
     * lại là các lớp hiện thực của ValidateFunction để chỉ định các hình thức
     * kiểm tra (như bắt buộc, RegEx, ...)
     */
    public Validate(Object[]... objectToValidate) {
        this.objectToValidate = objectToValidate.clone();
    }

    /**
     * Lấy thông tin của toàn bộ control
     *
     * @return Toàn bộ các object được quản lý
     */
    public Object[][] getObjectToValidate() {
        return objectToValidate;
    }

    /**
     * Lấy thông tin của control được chỉ định
     *
     * @param index là index của control cần lấy
     * @return Thông tin của control được chỉ định
     */
    public Object[] getObjectToValidate(int index) {
        return objectToValidate[index];
    }

    /**
     * Set lại các control cần validate
     *
     * @param objectToValidate là các control cần validate, là các mảng Object,
     * phần tử số 1 là biến control, phần tử 2 là tên control, các phần tử còn
     * lại là các lớp hiện thực của ValidateFunction để chỉ định các hình thức
     * kiểm tra (như bắt buộc, RegEx, ...)
     */
    public void setObjectToValidate(Object[]... objectToValidate) {
        this.objectToValidate = objectToValidate.clone();
    }

    /**
     * Kiểm tra lỗi và trả về thông báo lỗi (nếu có) toàn bộ control có khai báo
     * trong contrustor
     *
     * @return - Rỗng nếu không có lỗi<br/>
     * - Chuỗi thông báo lỗi nếu có lỗi
     */
    public String getErrorMessage() {
        String errorMessage = "";
        boolean isFocused = false;
        for (Object[] arrObject : objectToValidate) {
            Object control = arrObject[0];
            String controlName = arrObject[1].toString();
            String messageControl;

            if (arrObject.length > 2) {
                Object[] arrObjectTemp = new Object[arrObject.length - 2];
                System.arraycopy(arrObject, 2, arrObjectTemp, 0, arrObject.length - 2);
                messageControl = getErrorMessageControl(control, controlName, arrObjectTemp);
            } else {
                messageControl = getErrorMessageControl(control, controlName);
            }
            errorMessage += messageControl;

            if (!isFocused && !"".equals(messageControl)) {
                requestFocus(control);
                isFocused = true;
            }
        }
        return errorMessage;
    }

    /**
     * Kiểm tra lỗi và hiện thông báo lỗi (nếu có) toàn bộ control có khai báo
     * trong contrustor
     *
     * @param cmpnt tham số của hàm JOptionPane.showMessageDialog
     * @return - True nếu không có lỗi<br/>
     * - False thông báo lỗi nếu có lỗi
     */
    public boolean validate(Component cmpnt) {
        String errMessage = getErrorMessage();
        if (errMessage.isEmpty()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(cmpnt, errMessage);
            return false;
        }
    }

    /**
     * Reset toàn bộ control có khai báo trong contrustor
     */
    public void reset() {
        for (Object[] objectToValidate1 : objectToValidate) {
            resetControl(objectToValidate1[0]);
        }
    }

    /**
     * Validate control được chỉ định (không có khai báo trong contrustor) và
     * hiển thị thông báo lỗi (nếu có)
     *
     * @param control là control cần được validate
     * @param name là tên control cần được validate
     * @param cmpnt tham số của hàm JOptionPane.showMessageDialog
     * @param arrVf là các lớp hiện thực của ValidateFunction hoặc là mảng các
     * lớp hiện thực của ValidateFunction để chỉ định các hình thức kiểm tra
     * (như bắt buộc, RegEx, ...)
     * @return - True nếu không có lỗi<br/>
     * - False thông báo lỗi nếu có lỗi
     */
    public static boolean validateControl(Object control, String name, Component cmpnt, Object... arrVf) {
        String errMessage = getErrorMessageControl(control, name, arrVf);
        if (errMessage.isEmpty()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(cmpnt, errMessage);
            return false;
        }
    }

    /**
     * Validate control được chỉ định (có khai báo trong contrustor) và hiển thị
     * thông báo lỗi (nếu có)
     *
     * @param control là control cần được validate
     * @param cmpnt tham số của hàm JOptionPane.showMessageDialog
     * @return
     */
    public boolean validateControl(Object control, Component cmpnt) {
        String errMessage = getErrorMessageControl(control);
        if (errMessage.isEmpty()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(cmpnt, errMessage);
            return false;
        }
    }

    /**
     * Validate nhiều control được chỉ định (có khai báo trong contrustor) và
     * hiển thị thông báo lỗi (nếu có)
     *
     * @param cmpnt tham số của hàm JOptionPane.showMessageDialog
     * @param control là các control hoặc là mảng các control cần được validate
     * @return
     */
    public boolean validateControls(Component cmpnt, Object... control) {
        String errMessage = getErrorMessageControls(control);
        if (errMessage.isEmpty()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(cmpnt, errMessage);
            return false;
        }
    }

    /**
     * Validate control được chỉ định (không có khai báo trong contrustor) và và
     * trả về thông báo lỗi (nếu có)
     *
     * @param control là control cần được validate
     * @param name là tên control cần được validate
     * @param arrVf là các lớp hiện thực của ValidateFunction hoặc là mảng các
     * lớp hiện thực của ValidateFunction để chỉ định các hình thức kiểm tra
     * (như bắt buộc, RegEx, ...)
     * @return - Rỗng nếu không có lỗi<br/>
     * - Chuỗi thông báo lỗi nếu có lỗi
     */
    public static String getErrorMessageControl(Object control, String name, Object... arrVf) {
        String messageControl = "";
        try {
            Object[] objTemp;
            if (arrVf[0] instanceof Object[]) {
                objTemp = (Object[]) arrVf[0];
            } else {
                objTemp = arrVf;
            }
            for (Object objTemp1 : objTemp) {
                String message = ((ValidateFunction) objTemp1).valid(control, name);
                if (!message.isEmpty()) {
                    messageControl += message + "\n";
                }
                if (message.endsWith("is empty !") || message.startsWith("You are not select ")) {
                    break;
                }
            }
        } catch (Exception ex) {
        }
        if ((!messageControl.endsWith("\n")) && (!messageControl.isEmpty())) {
            messageControl += "\n";
        }
        return messageControl;
    }

    /**
     * Validate control được chỉ định (có khai báo trong contrustor) và và trả
     * về thông báo lỗi (nếu có)
     *
     * @param control là control cần được validate
     * @return - Rỗng nếu không có lỗi<br/>
     * - Chuỗi thông báo lỗi nếu có lỗi
     */
    public String getErrorMessageControl(Object control) {
        String errorMessage = "";
        for (Object[] objectToValidate1 : objectToValidate) {
            if (objectToValidate1[0] == control) {
                Object[] arrObject = objectToValidate1;
                String controlName = arrObject[1].toString();
                String messageControl;
                if (arrObject.length > 2) {
                    Object[] arrObjectTemp = new Object[arrObject.length - 2];
                    System.arraycopy(arrObject, 2, arrObjectTemp, 0, arrObject.length - 2);
                    messageControl = getErrorMessageControl(control, controlName, arrObjectTemp);
                } else {
                    messageControl = getErrorMessageControl(control, controlName);
                }
                if (!messageControl.isEmpty()) {
                    errorMessage += messageControl;
                }
            }
        }
        return errorMessage;
    }

    /**
     * Validate control được chỉ định (có khai báo trong contrustor) và và trả
     * về thông báo lỗi (nếu có)
     *
     * @param control là các control hoặc là mảng các control cần được validate
     * @return - Rỗng nếu không có lỗi<br/>
     * - Chuỗi thông báo lỗi nếu có lỗi
     */
    public String getErrorMessageControls(Object... control) {
        String errorMessage = "";
        Object[] objTemp;
        if (control[0] instanceof Object[]) {
            objTemp = (Object[]) control[0];
        } else {
            objTemp = control;
        }
        for (int j = 0; j < control.length; j++) {
            Object object = objTemp[j];
            String error = getErrorMessageControl(object);
            if (!error.isEmpty()) {
                errorMessage += error;
            }
        }
        return errorMessage;
    }

    /**
     * Reset control được chỉ định (có hoặc không có khai báo trong contrustor)
     *
     * @param control là control cần được validate
     */
    public static void resetControl(Object control) {
        if (control instanceof JTextField) {
            ((JTextField) control).setText("");
        } else if (control instanceof JTextArea) {
            ((JTextArea) control).setText("");
        } else if (control instanceof JPasswordField) {
            ((JPasswordField) control).setText("");
        } else if (control instanceof JComboBox) {
            ((JComboBox) control).setSelectedIndex(0);
        } else if (control instanceof JList) {
            ((JList) control).clearSelection();
        } else if (control instanceof JTable) {
            ((JTable) control).clearSelection();
        } else if (control instanceof ButtonGroup) {
            ((ButtonGroup) control).clearSelection();
        }
    }

    /**
     * Gán giá trị cho control (có khai báo trong contrustor, là Text Field,
     * Text Area, Password Field, Combo Box)
     *
     * @param i là index của control
     * @param str là giá trị cần set
     */
    public void setText(int i, String str) {
        Object control = objectToValidate[i][0];
        if (control instanceof JTextField) {
            ((JTextField) control).setText(str);
        } else if (control instanceof JTextArea) {
            ((JTextArea) control).setText(str);
        } else if (control instanceof JPasswordField) {
            ((JPasswordField) control).setText(str);
        } else if (control instanceof JComboBox) {
            ((JComboBox) control).setSelectedItem(str);
        }
    }

    /**
     * Request Focus cho control được chỉ định (có hoặc không có khai báo trong
     * contrustor)
     *
     * @param control là control cần được validate
     */
    public static void requestFocus(Object control) {
        if (control instanceof JTextField) {
            ((JTextField) control).requestFocusInWindow();
        } else if (control instanceof JTextArea) {
            ((JTextArea) control).requestFocusInWindow();
        } else if (control instanceof JPasswordField) {
            ((JPasswordField) control).requestFocusInWindow();
        } else if (control instanceof JComboBox) {
            ((JComboBox) control).requestFocusInWindow();
        } else if (control instanceof JList) {
            ((JList) control).requestFocusInWindow();
        } else if (control instanceof JTable) {
            ((JTable) control).requestFocusInWindow();
        }
    }

    /**
     * Set Editable control (có khai báo trong contrustor, là Text Field, Text
     * Area, Password Field, Combo Box)
     *
     * @param i là index của control
     * @param state là giá trị cần set (true/false)
     */
    public void setEditable(int i, boolean state) {
        Object control = objectToValidate[i][0];
        if (control instanceof JTextField) {
            ((JTextField) control).setEditable(state);
        } else if (control instanceof JTextArea) {
            ((JTextArea) control).setEditable(state);
        } else if (control instanceof JPasswordField) {
            ((JPasswordField) control).setEditable(state);
        } else if (control instanceof JComboBox) {
            ((JComboBox) control).setEditable(state);
        }
    }

    /**
     * Set Editable control (có khai báo trong contrustor, là Text Field, Text
     * Area, Password Field, Combo Box)<br />
     * Tương tự như void setEditable(int i, boolean state), nhưng nếu control là
     * Combo Box thì set enabled chứ không phải là editable
     *
     * @param i là index của control
     * @param state là giá trị cần set (true/false)
     */
    public void setEditableSpecial(int i, boolean state) {
        Object control = objectToValidate[i][0];
        if (control instanceof JTextField) {
            ((JTextField) control).setEditable(state);
        } else if (control instanceof JTextArea) {
            ((JTextArea) control).setEditable(state);
        } else if (control instanceof JPasswordField) {
            ((JPasswordField) control).setEditable(state);
        } else if (control instanceof JComboBox) {
            ((JComboBox) control).setEnabled(state);
        }
    }

    /**
     * Set Enabled control (có khai báo trong contrustor, là Text Field, Text
     * Area, Password Field, Combo Box)
     *
     * @param i là index của control
     * @param state là giá trị cần set (true/false)
     */
    public void setEnabled(int i, boolean state) {
        Object control = objectToValidate[i][0];
        if (control instanceof JTextField) {
            ((JTextField) control).setEnabled(state);
        } else if (control instanceof JTextArea) {
            ((JTextArea) control).setEnabled(state);
        } else if (control instanceof JPasswordField) {
            ((JPasswordField) control).setEnabled(state);
        } else if (control instanceof JComboBox) {
            ((JComboBox) control).setEnabled(state);
        }
    }

    /**
     * Lấy giá trị của control (có khai báo trong contrustor, là Text Field,
     * Text Area, Password Field, Combo Box)
     *
     * @param i là index của control
     * @return Giá trị của control yêu cầu, nếu control không phải là Text
     * Field, Text Area, Combo Box thì trả về giá trị null
     */
    public String getText(int i) {
        Object control = objectToValidate[i][0];
        if (control instanceof JTextField) {
            return ((JTextField) control).getText();
        } else if (control instanceof JTextArea) {
            return ((JTextArea) control).getText();
        } else if (control instanceof JPasswordField) {
            return new String(((JPasswordField) control).getPassword());
        } else if (control instanceof JComboBox) {
            return ((JComboBox) control).getSelectedItem().toString();
        }
        return null;
    }

    /**
     * Tạo ra thể hiện của ValidateFunction, kiểm tra bắt buộc
     *
     * @return Thể hiện của ValidateFunction
     */
    public static ValidateFunction getRequestValidate() {
        return (Object control, String name) -> {
            String messageControl = "";
            final String strEmpty = " is empty !";
            final String strNotSelect = "You are not select ";
            if (control instanceof JTextField) {
                if (((JTextField) control).getText().isEmpty()) {
                    messageControl = name + strEmpty;
                }
            } else if (control instanceof JTextArea) {
                if (((JTextArea) control).getText().isEmpty()) {
                    messageControl = name + strEmpty;
                }
            } else if (control instanceof JPasswordField) {
                if (new String((((JPasswordField) control).getPassword())).isEmpty()) {
                    messageControl = name + strEmpty;
                }
            } else if (control instanceof JComboBox) {
                if (((JComboBox) control).getSelectedIndex() == 0) {
                    messageControl = strNotSelect + name + " !";
                }
            } else if (control instanceof JList) {
                if (((JList) control).getSelectedIndex() == -1) {
                    messageControl = strNotSelect + name + " !";
                }
            } else if (control instanceof JTable) {
                if (((JTable) control).getSelectedRow() == -1) {
                    messageControl = strNotSelect + name + " !";
                }
            } else if (control instanceof ButtonGroup) {
                if (((ButtonGroup) control).getSelection() == null) {
                    messageControl = strNotSelect + name + " !";
                }
            }
            return messageControl;
        };
    }

    /**
     * RexEx kiểm tra số nguyên dương
     */
    public final static String regExPositiveInteger = "^(\\d+)?$";

    /**
     * RexEx kiểm tra số nguyên
     */
    public final static String regExInteger = "^(-?\\d+)?$";

    /**
     * RexEx kiểm tra số nguyên âm
     */
    public final static String regExNegativeInteger = "^(-\\d+)?$";

    /**
     * RexEx kiểm tra số thực dương
     */
    public final static String regExPositiveReal = "^(\\d+(\\.\\d+)?)?$";

    /**
     * RexEx kiểm tra số thực
     */
    public final static String regExReal = "^(-?\\d+(\\.\\d+)?)?$";

    /**
     * RexEx kiểm tra số thực âm
     */
    public final static String regExNegativeReal = "^(-\\d+(\\.\\d+)?)?$";

    /**
     * RexEx kiểm tra email
     */
    public final static String regExEmail = "^([_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*(\\.[a-z]{2,4}))?$";

    /**
     * RexEx kiểm tra IP
     */
    public final static String regExIP = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    /**
     *
     * @param regEx chuỗi RegEx để validate
     * @param format là chuỗi để hiển thị thông báo lỗi
     * @return
     */
    public static ValidateFunction getRegExValidate(String regEx, String format) {
        return new RegExValidate(regEx, format);
    }
}

class RegExValidate implements ValidateFunction {

    final String strRegEx;
    final String strFormat;

    public RegExValidate(String strRegEx, String strFormat) {
        this.strRegEx = strRegEx;
        this.strFormat = strFormat;
    }

    public RegExValidate(String strRegEx) {
        this.strRegEx = strRegEx;
        this.strFormat = null;
    }

    @Override
    public String valid(Object control, String name) {
        String messageControl = "";
        String strValid;
        if (strFormat == null) {
            strValid = " is not valid format !";
        } else {
            strValid = " is not valid format (" + strFormat + ") !";
        }
        if (control instanceof JTextField) {
            if (!((JTextField) control).getText().matches(strRegEx)) {
                messageControl = name + strValid;
            }
        } else if (control instanceof JTextArea) {
            if (!((JTextArea) control).getText().matches(strRegEx)) {
                messageControl = name + strValid;
            }
        }
        return messageControl;
    }
}
