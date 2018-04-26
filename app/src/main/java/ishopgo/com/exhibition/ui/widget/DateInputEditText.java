package ishopgo.com.exhibition.ui.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;

/**
 * Created by xuanhong on 11/16/17. HappyCoding!
 */

public class DateInputEditText extends TextInputEditText {

    public DateInputEditText(Context context) {
        super(context);

        init();
    }

    public DateInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public DateInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
        addTextChangedListener(new DateFormattingTextWatcher(this));

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(10); // dd/MM/yyyy
        setFilters(fArray);
    }

    public boolean isEmpty() {
        return getText().toString().isEmpty();
    }

    public boolean isError() {
        return getError() != null;
    }

}
