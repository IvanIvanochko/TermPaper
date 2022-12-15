import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EnhancedMethods {
    private static final DateTimeFormatter fastFormatter = DateTimeFormatter.ofPattern("ddMMuuuu");
    private static final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    public static void enhanceDatePickers(DatePicker datePicker) {
        datePicker.setValue(LocalDate.now());
        datePicker.setConverter(new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate object) {
                return object.format(defaultFormatter);
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return LocalDate.parse(string, fastFormatter);
                } catch (DateTimeParseException dtp) {
                }

                return LocalDate.parse(string, defaultFormatter);
            }
        });
        TextField textField = datePicker.getEditor();
        textField.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            if (!"0123456789/".contains(event.getCharacter())) {
                event.consume();
                //return;
            }
            if ("/".equals(event.getCharacter()) && (textField.getText().isEmpty() || textField.getText().charAt(textField.getCaretPosition()-1)=='/')) {
                //If the users types slash again after it has been added, cancels it.
                event.consume();
            }
            textField.selectForward();
            if (!event.getCharacter().equals("/") && textField.getSelectedText().equals("/")) {
                textField.cut();
                textField.selectForward();
            }
            textField.cut();

            Platform.runLater(() -> {
                String textUntilHere = textField.getText(0, textField.getCaretPosition());

                if (textUntilHere.matches("\\d\\d") || textUntilHere.matches("\\d\\d/\\d\\d")) {
                    String textAfterHere = "";
                    try { textAfterHere = textField.getText(textField.getCaretPosition()+1, textField.getText().length()); } catch (Exception ignored) {}
                    int caretPosition = textField.getCaretPosition();
                    textField.setText(textUntilHere + "/" + textAfterHere);
                    textField.positionCaret(caretPosition+1);
                }
            });
        });
        addTextLimiterDP(datePicker, 10);
    }

    public static void addTextLimiterDP(final DatePicker datePicker, final int maxLength) {
        TextField tf = datePicker.getEditor();
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
    public static void addTextLimiterTF(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }
    public static void onlySpecifiedCharactersTF(TextField tf, String str){
        tf.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            if (!str.contains(event.getCharacter()))
                event.consume();
        });
    }
}
