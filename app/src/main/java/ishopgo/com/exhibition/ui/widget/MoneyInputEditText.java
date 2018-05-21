package ishopgo.com.exhibition.ui.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;

/**
 * Created by xuanhong on 5/21/18. HappyCoding!
 */
public class MoneyInputEditText extends TextInputEditText {
    public MoneyInputEditText(Context context) {
        super(context);

        init();
    }

    public MoneyInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MoneyInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private int MAX_LENGTH = 19;

    public long getMoney() {
        String cleanString = super.getText().toString();
        cleanString = cleanString.replaceAll("[$,.]", "");
        long money = 0;
        try {
            money = Long.parseLong(cleanString);
        } catch (NumberFormatException e) {
            money = 0L;
        }
        return money;
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        addTextChangedListener(new MoneyNumberFormattingTextWatcher(this, MAX_LENGTH));

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(MAX_LENGTH);
        setFilters(fArray);
    }
}
