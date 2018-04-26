package ishopgo.com.exhibition.ui.widget;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xuanhong on 3/13/18. HappyCoding!
 */

public class DateFormattingTextWatcher implements TextWatcher {
    private @NonNull
    EditText editText;

    private SimpleDateFormat defaultFormatter;
    private Date ceilDate;

    public DateFormattingTextWatcher(@NonNull EditText editText) {
        this.editText = editText;

        Calendar instance = Calendar.getInstance();
        instance.clear();
        ceilDate = instance.getTime();

        defaultFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        defaultFormatter.setLenient(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public synchronized void afterTextChanged(Editable s) {
        editText.removeTextChangedListener(this);

        if (s.length() == 0) {
            editText.setError(null);
            editText.addTextChangedListener(this);
            return;
        }
        String cleanString = s.toString().replaceAll("[-.]", "/");

        Date parse = null;
        try {
            parse = defaultFormatter.parse(cleanString);
            if (parse.before(ceilDate)) throw new Exception();
            editText.setError(null);
        } catch (Exception e) {
            editText.setError("Không hợp lệ");
        }

        if (parse != null) {
            editText.setText(cleanString);
            editText.setSelection(cleanString.length());
        }

        editText.addTextChangedListener(this);
    }
}

